package io.github.abstractengine.game.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Loads questions from JSON files if present, otherwise falls back to QuestionBank.
 *
 * Expected JSON format:
 * [
 *   { "prompt":"...", "correct":"...", "decoy1":"...", "decoy2":"...", "difficulty":2 }
 * ]
 */
public class JsonQuestionRepository implements QuestionRepository {

    private static final String[] GRAMMAR_PATHS = {
            "questions/grammar_questions.json",
            "assets/questions/grammar_questions.json"
    };
    private static final String[] ANTONYM_PATHS = {
            "questions/antonym_questions.json",
            "assets/questions/antonym_questions.json"
    };
    private static final String[] SYNONYM_PATHS = {
            "questions/synonym_questions.json",
            "assets/questions/synonym_questions.json"
    };
    private static final String[] CATEGORY_PATHS = {
            "questions/category_questions.json",
            "assets/questions/category_questions.json"
    };

    @Override
    public List<QuestionBank.Question> getLanguageQuestions() {
        List<QuestionBank.Question> grammar = loadFromJson(GRAMMAR_PATHS);
        List<QuestionBank.Question> antonym = loadFromJson(ANTONYM_PATHS);
        List<QuestionBank.Question> synonym = loadFromJson(SYNONYM_PATHS);

        // Any missing/empty section means we should use hardcoded fallback to keep full set intact.
        if (grammar.isEmpty() || antonym.isEmpty() || synonym.isEmpty()) {
            return QuestionBank.getAllLanguageQuestions();
        }

        List<QuestionBank.Question> loaded = new ArrayList<>(grammar.size() + antonym.size() + synonym.size());
        loaded.addAll(grammar);
        loaded.addAll(antonym);
        loaded.addAll(synonym);
        return loaded;
    }

    @Override
    public List<QuestionBank.Question> getCategoryQuestions() {
        List<QuestionBank.Question> loaded = loadFromJson(CATEGORY_PATHS);
        if (!loaded.isEmpty()) return loaded;
        return QuestionBank.getCategoryQuestions();
    }

    private List<QuestionBank.Question> loadFromJson(String[] internalPaths) {
        List<QuestionBank.Question> out = new ArrayList<>();
        try {
            FileHandle fh = firstExisting(internalPaths);
            if (fh == null) {
                return out;
            }

            JsonValue root = new JsonReader().parse(fh);
            if (root == null || !root.isArray()) {
                return out;
            }

            for (JsonValue v = root.child; v != null; v = v.next) {
                String prompt = trimOrEmpty(v.getString("prompt", ""));
                String correct = trimOrEmpty(v.getString("correct", ""));
                String decoy1 = trimOrEmpty(v.getString("decoy1", ""));
                String decoy2 = trimOrEmpty(v.getString("decoy2", ""));
                int difficulty = v.getInt("difficulty", 1);

                if (prompt.isEmpty() || correct.isEmpty() || decoy1.isEmpty() || decoy2.isEmpty()) {
                    continue;
                }

                out.add(new QuestionBank.Question(prompt, correct, decoy1, decoy2, difficulty));
            }
        } catch (Exception ignored) {
            // Graceful fallback to hardcoded QuestionBank.
        }
        return out;
    }

    private static FileHandle firstExisting(String[] internalPaths) {
        if (internalPaths == null) return null;
        for (String p : internalPaths) {
            if (p == null || p.trim().isEmpty()) continue;
            FileHandle fh = Gdx.files.internal(p);
            if (fh != null && fh.exists()) {
                return fh;
            }
        }
        return null;
    }

    private static String trimOrEmpty(String s) {
        return s == null ? "" : s.trim();
    }
}

