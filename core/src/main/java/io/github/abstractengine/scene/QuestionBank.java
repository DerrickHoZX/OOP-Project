package io.github.abstractengine.scene;

import java.util.ArrayList;
import java.util.List;

/**
 * QuestionBank - Centralized storage for all game questions
 * Separates data from game logic for easier maintenance
 */
public class QuestionBank {

    // ===========================
    // GRAMMAR QUESTIONS (20)
    // ===========================
    
    /** Difficulty 3: sentence structure, fill-in-the-blank */
    public static List<Question> getGrammarQuestions() {
        List<Question> questions = new ArrayList<>();
        int d = 3;

        questions.add(new Question("She ___ to school every day.", "goes", "go", "going", d));
        questions.add(new Question("The cat is ___ the table.", "on", "at", "in", d));
        questions.add(new Question("They ___ playing soccer now.", "are", "is", "am", d));
        questions.add(new Question("This is ___ apple.", "an", "a", "the", d));
        questions.add(new Question("He runs ___ than me.", "faster", "fast", "fastest", d));
        questions.add(new Question("I have ___ homework to do.", "much", "many", "few", d));
        questions.add(new Question("We ___ finished our project.", "have", "has", "having", d));
        questions.add(new Question("She is the ___ student in class.", "smartest", "smart", "smarter", d));
        questions.add(new Question("There ___ three dogs outside.", "are", "is", "was", d));
        questions.add(new Question("He didn't ___ his lunch.", "eat", "ate", "eating", d));
        questions.add(new Question("I am taller ___ my brother.", "than", "then", "that", d));
        questions.add(new Question("The baby ___ crying loudly.", "is", "are", "be", d));
        questions.add(new Question("She bought ___ umbrella.", "an", "a", "the", d));
        questions.add(new Question("We went to the park ___ Sunday.", "on", "in", "at", d));
        questions.add(new Question("I will call you ___ later.", "back", "much", "very", d));
        questions.add(new Question("He ___ a letter yesterday.", "wrote", "write", "writing", d));
        questions.add(new Question("That book is ___ interesting.", "very", "many", "more", d));
        questions.add(new Question("I have lived here ___ 2015.", "since", "for", "at", d));
        questions.add(new Question("She can ___ very well.", "sing", "sings", "singing", d));
        questions.add(new Question("The dog wagged ___ tail.", "its", "it's", "it", d));
        
        return questions;
    }
    
    // ===========================
    // ANTONYMS (10)
    // ===========================
    
    /** Difficulty 2: vocabulary, one-word matching */
    public static List<Question> getAntonymQuestions() {
        List<Question> questions = new ArrayList<>();
        int d = 2;

        questions.add(new Question("Opposite of Happy:", "sad", "excited", "smiling", d));
        questions.add(new Question("Opposite of Big:", "small", "huge", "tall", d));
        questions.add(new Question("Opposite of Hot:", "cold", "warm", "spicy", d));
        questions.add(new Question("Opposite of Fast:", "slow", "quick", "speedy", d));
        questions.add(new Question("Opposite of Loud:", "quiet", "noisy", "talking", d));
        questions.add(new Question("Opposite of Early:", "late", "soon", "first", d));
        questions.add(new Question("Opposite of Strong:", "weak", "tough", "big", d));
        questions.add(new Question("Opposite of Open:", "closed", "wide", "free", d));
        questions.add(new Question("Opposite of Full:", "empty", "packed", "busy", d));
        questions.add(new Question("Opposite of Bright:", "dark", "shiny", "clear", d));
        
        return questions;
    }
    
    // ===========================
    // SYNONYMS (10)
    // ===========================
    
    /** Difficulty 2: vocabulary, one-word matching */
    public static List<Question> getSynonymQuestions() {
        List<Question> questions = new ArrayList<>();
        int d = 2;

        questions.add(new Question("Synonym of Smart:", "clever", "silly", "loud", d));
        questions.add(new Question("Synonym of Angry:", "mad", "happy", "sleepy", d));
        questions.add(new Question("Synonym of Tiny:", "small", "huge", "wide", d));
        questions.add(new Question("Synonym of Begin:", "start", "end", "break", d));
        questions.add(new Question("Synonym of End:", "finish", "start", "begin", d));
        questions.add(new Question("Synonym of Brave:", "courageous", "afraid", "weak", d));
        questions.add(new Question("Synonym of Quick:", "fast", "slow", "quiet", d));
        questions.add(new Question("Synonym of Silent:", "quiet", "loud", "angry", d));
        questions.add(new Question("Synonym of Large:", "big", "tiny", "short", d));
        questions.add(new Question("Synonym of Happy:", "joyful", "sad", "tired", d));
        
        return questions;
    }
    
    // ===========================
    // CATEGORY GAME (10)
    // ===========================
    
    /** Difficulty 1: simple categorization, single correct answer */
    public static List<Question> getCategoryQuestions() {
        List<Question> questions = new ArrayList<>();
        int d = 1;

        // Action words (correct) vs Non-action words (decoys)
        questions.add(new Question("Find an Action Word", "Run", "Apple", "Blue", d));
        questions.add(new Question("Find an Action Word", "Jump", "Chair", "Happy", d));
        questions.add(new Question("Find an Action Word", "Swim", "Table", "Tall", d));
        questions.add(new Question("Find an Action Word", "Eat", "Dog", "River", d));
        questions.add(new Question("Find an Action Word", "Laugh", "School", "Pencil", d));
        questions.add(new Question("Find an Action Word", "Write", "Apple", "Blue", d));
        questions.add(new Question("Find an Action Word", "Climb", "Chair", "Happy", d));
        questions.add(new Question("Find an Action Word", "Throw", "Table", "Tall", d));
        questions.add(new Question("Find an Action Word", "Dance", "Dog", "River", d));
        questions.add(new Question("Find an Action Word", "Sing", "School", "Pencil", d));
        
        return questions;
    }
    
    // ===========================
    // COMBINED: Grammar + Synonyms + Antonyms
    // ===========================
    
    public static List<Question> getAllLanguageQuestions() {
        List<Question> allQuestions = new ArrayList<>();
        allQuestions.addAll(getGrammarQuestions());    // 20
        allQuestions.addAll(getAntonymQuestions());    // 10
        allQuestions.addAll(getSynonymQuestions());    // 10
        return allQuestions; // Total: 40 questions
    }
    
    // ===========================
    // HELPER: Question Class
    // ===========================
    
    public static class Question {
        public final String prompt;
        public final String correct;
        public final String decoy1;
        public final String decoy2;
        /** Difficulty 1 (easy) to 3 (hard). Used for progressive difficulty. */
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