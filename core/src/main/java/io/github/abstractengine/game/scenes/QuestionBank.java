package io.github.abstractengine.game.scenes;

import java.util.ArrayList;
import java.util.List;

/**
 * Legacy fallback questions only.
 *
 * Primary source is JSON via {@link JsonQuestionRepository}.
 * These methods exist to keep the game resilient if JSON files are missing.
 */
public class QuestionBank {

    public static List<Question> getGrammarQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("She ___ to school every day.", "goes", "go", "going", 3));
        questions.add(new Question("The cat is ___ the table.", "on", "at", "in", 3));
        questions.add(new Question("They ___ playing soccer now.", "are", "is", "am", 3));
        return questions;
    }

    public static List<Question> getAntonymQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Opposite of Happy:", "sad", "excited", "smiling", 2));
        questions.add(new Question("Opposite of Big:", "small", "huge", "tall", 2));
        questions.add(new Question("Opposite of Hot:", "cold", "warm", "spicy", 2));
        return questions;
    }

    public static List<Question> getSynonymQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Synonym of Smart:", "clever", "silly", "loud", 2));
        questions.add(new Question("Synonym of Angry:", "mad", "happy", "sleepy", 2));
        questions.add(new Question("Synonym of Tiny:", "small", "huge", "wide", 2));
        return questions;
    }

    public static List<Question> getCategoryQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Find an Action Word", "Run", "Apple", "Blue", 1));
        questions.add(new Question("Find an Action Word", "Jump", "Chair", "Happy", 1));
        questions.add(new Question("Find an Action Word", "Swim", "Table", "Tall", 1));
        return questions;
    }

    public static List<Question> getAllLanguageQuestions() {
        List<Question> allQuestions = new ArrayList<>();
        allQuestions.addAll(getGrammarQuestions());
        allQuestions.addAll(getAntonymQuestions());
        allQuestions.addAll(getSynonymQuestions());
        return allQuestions;
    }

    public static class Question {
        public final String prompt;
        public final String correct;
        public final String decoy1;
        public final String decoy2;
        public final int difficulty;

        public Question(String prompt, String correct, String decoy1, String decoy2) {
            this(prompt, correct, decoy1, decoy2, 1);
        }

        public Question(String prompt, String correct, String decoy1, String decoy2, int difficulty) {
            this.prompt = prompt;
            this.correct = correct;
            this.decoy1 = decoy1;
            this.decoy2 = decoy2;
            this.difficulty = Math.max(1, Math.min(3, difficulty));
        }
    }
}

