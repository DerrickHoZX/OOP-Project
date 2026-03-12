package io.github.abstractengine.managers;

import com.badlogic.gdx.math.MathUtils;
import io.github.abstractengine.scene.QuestionBank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AlgorithmManager - Controls question selection to:
 * 1. Avoid repeating questions (except those answered incorrectly)
 * 2. Progressively increase difficulty over the session
 */
public class AlgorithmManager {	

    private final List<QuestionBank.Question> allQuestions;
    private final Set<String> askedCorrectly;   // questions answered right - won't be asked again
    private final Set<String> askedWrong;       // questions answered wrong - can be re-asked

    /** Number of questions answered so far. Used to ramp difficulty. */
    private int questionsAnswered;

    public AlgorithmManager(List<QuestionBank.Question> questions) {
        this.allQuestions = new ArrayList<>(questions);
        this.askedCorrectly = new HashSet<>();
        this.askedWrong = new HashSet<>();
        this.questionsAnswered = 0;
    }

    /**
     * Record the result of the last question before fetching the next.
     * Call this when the player submits an answer (correct or incorrect).
     */
    public void recordAnswer(QuestionBank.Question question, boolean wasCorrect) {
        if (question == null) return;

        String id = questionId(question);
        questionsAnswered++;

        if (wasCorrect) {
            askedCorrectly.add(id);
            askedWrong.remove(id);  // no need to retry
        } else {
            askedWrong.add(id);     // can be re-asked
        }
    }

    /**
     * Get the next question to display.
     * - Excludes questions already answered correctly (no repeat)
     * - Includes questions answered wrongly (retry for learning)
     * - Favors harder questions as the session progresses
     */
    public QuestionBank.Question getNextQuestion() {
        if (allQuestions.isEmpty()) return null;

        List<QuestionBank.Question> pool = buildEligiblePool();
        if (pool.isEmpty()) {
            // All questions asked correctly - allow any for variety (or recycle)
            pool = new ArrayList<>(allQuestions);
        }

        return selectWithDifficultyBias(pool);
    }

    /**
     * Build pool of questions that can be asked:
     * - Not in askedCorrectly (never repeat correct answers)
     * - Include askedWrong (retry mistakes)
     */
    private List<QuestionBank.Question> buildEligiblePool() {
        return allQuestions.stream()
                .filter(q -> !askedCorrectly.contains(questionId(q)))
                .collect(Collectors.toList());
    }

    /**
     * Select from pool with bias toward harder questions as session progresses.
     * Uses a soft progression: early on more easy, later more hard.
     */
    private QuestionBank.Question selectWithDifficultyBias(List<QuestionBank.Question> pool) {
        if (pool.isEmpty()) return null;
        if (pool.size() == 1) return pool.get(0);

        int maxDifficulty = pool.stream()
                .mapToInt(q -> q.difficulty)
                .max()
                .orElse(1);

        int minDifficulty = pool.stream()
                .mapToInt(q -> q.difficulty)
                .min()
                .orElse(1);

        // Progress factor: 0 at start, ~1 after ~15 questions
        float progress = Math.min(1f, questionsAnswered / 15f);

        // Target difficulty: ramps from min to max over the session
        float targetDiff = minDifficulty + (maxDifficulty - minDifficulty) * progress;

        // Weight questions by how close they are to target (with randomness)
        float[] weights = new float[pool.size()];
        float totalWeight = 0f;

        for (int i = 0; i < pool.size(); i++) {
            int d = pool.get(i).difficulty;
            float dist = Math.abs(d - targetDiff);
            float weight = 1f / (1f + dist);  // closer to target = higher weight
            weight += MathUtils.random(0.2f);  // add some randomness
            weights[i] = Math.max(0.01f, weight);
            totalWeight += weights[i];
        }

        float r = MathUtils.random(0f, totalWeight);
        for (int i = 0; i < pool.size(); i++) {
            r -= weights[i];
            if (r <= 0) return pool.get(i);
        }
        return pool.get(pool.size() - 1);
    }

    private static String questionId(QuestionBank.Question q) {
        return q.prompt + "|" + q.correct;
    }

    /** Reset state for a new game session. */
    public void reset() {
        askedCorrectly.clear();
        askedWrong.clear();
        questionsAnswered = 0;
    }
}
