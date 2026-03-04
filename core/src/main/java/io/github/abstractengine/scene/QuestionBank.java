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
    
    public static List<Question> getGrammarQuestions() {
        List<Question> questions = new ArrayList<>();
        
        questions.add(new Question("She ___ to school every day.", "goes", "go", "going"));
        questions.add(new Question("The cat is ___ the table.", "on", "at", "in"));
        questions.add(new Question("They ___ playing soccer now.", "are", "is", "am"));
        questions.add(new Question("This is ___ apple.", "an", "a", "the"));
        questions.add(new Question("He runs ___ than me.", "faster", "fast", "fastest"));
        questions.add(new Question("I have ___ homework to do.", "much", "many", "few"));
        questions.add(new Question("We ___ finished our project.", "have", "has", "having"));
        questions.add(new Question("She is the ___ student in class.", "smartest", "smart", "smarter"));
        questions.add(new Question("There ___ three dogs outside.", "are", "is", "was"));
        questions.add(new Question("He didn't ___ his lunch.", "eat", "ate", "eating"));
        questions.add(new Question("I am taller ___ my brother.", "than", "then", "that"));
        questions.add(new Question("The baby ___ crying loudly.", "is", "are", "be"));
        questions.add(new Question("She bought ___ umbrella.", "an", "a", "the"));
        questions.add(new Question("We went to the park ___ Sunday.", "on", "in", "at"));
        questions.add(new Question("I will call you ___ later.", "back", "much", "very"));
        questions.add(new Question("He ___ a letter yesterday.", "wrote", "write", "writing"));
        questions.add(new Question("That book is ___ interesting.", "very", "many", "more"));
        questions.add(new Question("I have lived here ___ 2015.", "since", "for", "at"));
        questions.add(new Question("She can ___ very well.", "sing", "sings", "singing"));
        questions.add(new Question("The dog wagged ___ tail.", "its", "it's", "it"));
        
        return questions;
    }
    
    // ===========================
    // ANTONYMS (10)
    // ===========================
    
    public static List<Question> getAntonymQuestions() {
        List<Question> questions = new ArrayList<>();
        
        questions.add(new Question("Opposite of Happy:", "sad", "excited", "smiling"));
        questions.add(new Question("Opposite of Big:", "small", "huge", "tall"));
        questions.add(new Question("Opposite of Hot:", "cold", "warm", "spicy"));
        questions.add(new Question("Opposite of Fast:", "slow", "quick", "speedy"));
        questions.add(new Question("Opposite of Loud:", "quiet", "noisy", "talking"));
        questions.add(new Question("Opposite of Early:", "late", "soon", "first"));
        questions.add(new Question("Opposite of Strong:", "weak", "tough", "big"));
        questions.add(new Question("Opposite of Open:", "closed", "wide", "free"));
        questions.add(new Question("Opposite of Full:", "empty", "packed", "busy"));
        questions.add(new Question("Opposite of Bright:", "dark", "shiny", "clear"));
        
        return questions;
    }
    
    // ===========================
    // SYNONYMS (10)
    // ===========================
    
    public static List<Question> getSynonymQuestions() {
        List<Question> questions = new ArrayList<>();
        
        questions.add(new Question("Synonym of Smart:", "clever", "silly", "loud"));
        questions.add(new Question("Synonym of Angry:", "mad", "happy", "sleepy"));
        questions.add(new Question("Synonym of Tiny:", "small", "huge", "wide"));
        questions.add(new Question("Synonym of Begin:", "start", "end", "break"));
        questions.add(new Question("Synonym of End:", "finish", "start", "begin"));
        questions.add(new Question("Synonym of Brave:", "courageous", "afraid", "weak"));
        questions.add(new Question("Synonym of Quick:", "fast", "slow", "quiet"));
        questions.add(new Question("Synonym of Silent:", "quiet", "loud", "angry"));
        questions.add(new Question("Synonym of Large:", "big", "tiny", "short"));
        questions.add(new Question("Synonym of Happy:", "joyful", "sad", "tired"));
        
        return questions;
    }
    
    // ===========================
    // CATEGORY GAME (10)
    // ===========================
    
    public static List<Question> getCategoryQuestions() {
        List<Question> questions = new ArrayList<>();
        
        // Action words (correct) vs Non-action words (decoys)
        questions.add(new Question("Find an Action Word", "Run", "Apple", "Blue"));
        questions.add(new Question("Find an Action Word", "Jump", "Chair", "Happy"));
        questions.add(new Question("Find an Action Word", "Swim", "Table", "Tall"));
        questions.add(new Question("Find an Action Word", "Eat", "Dog", "River"));
        questions.add(new Question("Find an Action Word", "Laugh", "School", "Pencil"));
        questions.add(new Question("Find an Action Word", "Write", "Apple", "Blue"));
        questions.add(new Question("Find an Action Word", "Climb", "Chair", "Happy"));
        questions.add(new Question("Find an Action Word", "Throw", "Table", "Tall"));
        questions.add(new Question("Find an Action Word", "Dance", "Dog", "River"));
        questions.add(new Question("Find an Action Word", "Sing", "School", "Pencil"));
        
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

        public Question(String prompt, String correct, String decoy1, String decoy2) {
            this.prompt = prompt;
            this.correct = correct;
            this.decoy1 = decoy1;
            this.decoy2 = decoy2;
        }
    }
}