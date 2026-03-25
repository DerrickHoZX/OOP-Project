package io.github.abstractengine.game.scenes;

import java.util.ArrayList;
import java.util.List;

/**
 * QuestionBank - Centralized storage for all game questions
 * Separates data from game logic for easier maintenance
 */
public class QuestionBank {

    // ===========================
    // GRAMMAR QUESTIONS (145)
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
        questions.add(new Question("If it ___ tomorrow, we'll stay home.", "rains", "rain", "rained", d));
        questions.add(new Question("My sister ___ medicine at university.", "studies", "study", "studying", d));
        questions.add(new Question("They have ___ to the beach many times.", "been", "be", "being", d));
        questions.add(new Question("The movie was ___ than I expected.", "better", "good", "best", d));
        questions.add(new Question("Neither Tom nor Jerry ___ here today.", "is", "are", "be", d));
        questions.add(new Question("She asked me ___ I could help her.", "if", "that", "what", d));
        questions.add(new Question("We need ___ water for the trip.", "some", "any", "many", d));
        questions.add(new Question("The book ___ on the shelf yesterday.", "was", "is", "are", d));
        questions.add(new Question("He speaks English ___ than before.", "better", "well", "good", d));
        questions.add(new Question("The children ___ in the garden now.", "are playing", "is playing", "playing", d));
        questions.add(new Question("She ___ her keys yesterday.", "lost", "lose", "losing", d));
        questions.add(new Question("Would you like ___ tea?", "some", "any", "many", d));
        questions.add(new Question("He ___ to work by bus every day.", "goes", "go", "going", d));
        questions.add(new Question("The door ___ closed when I left.", "was", "is", "were", d));
        questions.add(new Question("I wish I ___ speak French.", "could", "can", "will", d));
        questions.add(new Question("She made ___ mistakes on the test.", "many", "much", "lot", d));
        questions.add(new Question("___ you like to join us?", "Would", "Do", "Are", d));
        questions.add(new Question("They ___ dinner when I arrived.", "were having", "had", "have", d));
        questions.add(new Question("The weather ___ nice yesterday.", "was", "is", "are", d));
        questions.add(new Question("I ___ to the cinema last weekend.", "went", "go", "going", d));
        questions.add(new Question("She has ___ finished her homework.", "already", "yet", "still", d));
        questions.add(new Question("We ___ to leave early tomorrow.", "need", "needs", "needing", d));
        questions.add(new Question("The train ___ at 6 PM every day.", "departs", "depart", "departing", d));
        questions.add(new Question("___ your homework before playing.", "Finish", "Finishes", "Finishing", d));
        questions.add(new Question("She ___ never been to Japan.", "has", "have", "had", d));
        questions.add(new Question("The students ___ the exam tomorrow.", "will take", "take", "takes", d));
        questions.add(new Question("I ___ breakfast at 7 AM every day.", "have", "has", "having", d));
        questions.add(new Question("The movie ___ interesting.", "was", "were", "are", d));
        questions.add(new Question("They ___ football in the park.", "play", "plays", "playing", d));
        questions.add(new Question("My brother ___ taller than me.", "is", "are", "be", d));
        questions.add(new Question("We ___ the house last year.", "bought", "buy", "buying", d));
        questions.add(new Question("She ___ to music while studying.", "listens", "listen", "listening", d));
        questions.add(new Question("The dog ___ in the garden.", "is sleeping", "sleep", "sleeps", d));
        questions.add(new Question("I ___ my phone at home.", "left", "leave", "leaving", d));
        questions.add(new Question("___ we go to the cinema?", "Shall", "Do", "Are", d));
        questions.add(new Question("He ___ football since he was a child.", "has played", "play", "plays", d));
        questions.add(new Question("The shop ___ at 9 PM.", "closes", "close", "closing", d));
        questions.add(new Question("She ___ a doctor when she grows up.", "wants to be", "want to be", "wanted to be", d));
        questions.add(new Question("We ___ for the bus when it started raining.", "were waiting", "waited", "wait", d));
        questions.add(new Question("___ you seen my glasses?", "Have", "Has", "Do", d));
        questions.add(new Question("The cake ___ delicious.", "tastes", "taste", "tasting", d));
        questions.add(new Question("I ___ him at the party last night.", "saw", "see", "seeing", d));
        questions.add(new Question("They ___ to Paris next month.", "will travel", "travel", "travelled", d));
        questions.add(new Question("She ___ her grandmother every Sunday.", "visits", "visit", "visiting", d));
        questions.add(new Question("The baby ___ all night.", "cried", "cry", "cries", d));
        questions.add(new Question("We ___ pizza for dinner.", "ordered", "order", "ordering", d));
        questions.add(new Question("He ___ to the gym three times a week.", "goes", "go", "going", d));
        questions.add(new Question("The meeting ___ at 3 PM.", "starts", "start", "starting", d));
        questions.add(new Question("I ___ tired. I need to rest.", "am", "is", "are", d));
        questions.add(new Question("She ___ a book when I called her.", "was reading", "read", "reads", d));
        questions.add(new Question("They ___ their homework yet.", "haven't finished", "didn't finish", "don't finish", d));
        questions.add(new Question("The teacher ___ us a test tomorrow.", "will give", "gives", "gave", d));
        questions.add(new Question("My parents ___ in London.", "live", "lives", "living", d));
        questions.add(new Question("He ___ his car to work every day.", "drives", "drive", "drove", d));
        questions.add(new Question("We ___ the movie yesterday.", "watched", "watch", "watching", d));
        questions.add(new Question("The sun ___ in the east.", "rises", "rise", "rising", d));
        questions.add(new Question("She ___ her bag on the bus.", "forgot", "forget", "forgets", d));
        questions.add(new Question("I ___ to the party if I have time.", "will go", "go", "went", d));
        questions.add(new Question("They ___ each other for five years.", "have known", "know", "knew", d));
        questions.add(new Question("The dog ___ when someone knocks.", "barks", "bark", "barking", d));
        questions.add(new Question("We ___ a new house next year.", "will buy", "buy", "bought", d));
        questions.add(new Question("He ___ soccer when he was young.", "played", "play", "plays", d));
        questions.add(new Question("The children ___ to school by bus.", "go", "goes", "going", d));
        questions.add(new Question("She ___ her birthday last week.", "celebrated", "celebrate", "celebrates", d));
        questions.add(new Question("I ___ you at the station.", "will meet", "meet", "met", d));
        questions.add(new Question("They ___ the project by Friday.", "will finish", "finish", "finished", d));
        questions.add(new Question("The phone ___ while I was cooking.", "rang", "ring", "rings", d));
        questions.add(new Question("We ___ to the concert last week.", "went", "go", "going", d));
        questions.add(new Question("He ___ English fluently.", "speaks", "speak", "spoke", d));
        questions.add(new Question("She ___ in the pool right now.", "is swimming", "swim", "swims", d));
        questions.add(new Question("My sister ___ a new job.", "got", "get", "gets", d));
        questions.add(new Question("The flight ___ at 10 AM.", "departs", "depart", "departed", d));
        questions.add(new Question("I have never ___ such a beautiful sunset.", "seen", "see", "saw", d));
        questions.add(new Question("They ___ their grandparents next week.", "will visit", "visit", "visited", d));
        questions.add(new Question("The cat ___ on the sofa.", "is sleeping", "sleep", "sleeps", d));
        questions.add(new Question("We ___ dinner when the lights went out.", "were having", "had", "have", d));
        questions.add(new Question("He ___ his homework every evening.", "does", "do", "did", d));
        questions.add(new Question("She ___ to university in Oxford.", "went", "go", "goes", d));
        questions.add(new Question("The bus ___ every 15 minutes.", "comes", "come", "came", d));
        questions.add(new Question("I ___ my keys. Can you help me look?", "lost", "lose", "losing", d));
        questions.add(new Question("They ___ football in the rain.", "were playing", "played", "play", d));
        questions.add(new Question("The restaurant ___ at midnight.", "closes", "close", "closed", d));
        questions.add(new Question("We ___ the house at 8 AM.", "left", "leave", "leaving", d));
        questions.add(new Question("He ___ to school by bike.", "cycles", "cycle", "cycled", d));
        questions.add(new Question("She ___ her exam last month.", "passed", "pass", "passes", d));
        questions.add(new Question("My brother ___ his room yesterday.", "cleaned", "clean", "cleans", d));
        questions.add(new Question("The movie ___ for two hours.", "lasts", "last", "lasted", d));
        questions.add(new Question("I ___ coffee in the morning.", "drink", "drinks", "drank", d));
        questions.add(new Question("They ___ the game by 3 goals.", "won", "win", "wins", d));
        questions.add(new Question("The plane ___ in an hour.", "takes off", "take off", "took off", d));
        questions.add(new Question("She ___ her hair every morning.", "washes", "wash", "washed", d));
        questions.add(new Question("We ___ the meeting at 2 PM.", "have", "has", "had", d));
        questions.add(new Question("He ___ a letter to his friend.", "wrote", "write", "writes", d));
        questions.add(new Question("The baby ___ to walk.", "is learning", "learn", "learns", d));
        questions.add(new Question("I ___ to bed early last night.", "went", "go", "going", d));
        questions.add(new Question("They ___ the problem together.", "solved", "solve", "solves", d));
        questions.add(new Question("The store ___ on Sundays.", "is closed", "closed", "closes", d));
        questions.add(new Question("She ___ her brother at the airport.", "met", "meet", "meets", d));
        questions.add(new Question("We ___ a lot of rain last week.", "had", "have", "has", d));
        questions.add(new Question("He ___ in New York for 10 years.", "has lived", "live", "lived", d));
        questions.add(new Question("The teacher ___ the lesson clearly.", "explained", "explain", "explains", d));
        questions.add(new Question("My father ___ the newspaper every morning.", "reads", "read", "reading", d));
        questions.add(new Question("I ___ my friends at the café.", "am meeting", "meet", "met", d));
        questions.add(new Question("They ___ to the beach last summer.", "went", "go", "goes", d));
        questions.add(new Question("The dog ___ its tail.", "wagged", "wag", "wags", d));
        questions.add(new Question("She ___ the piano beautifully.", "plays", "play", "played", d));
        questions.add(new Question("We ___ the bus to work.", "take", "takes", "took", d));
        questions.add(new Question("He ___ his hand when he saw me.", "waved", "wave", "waves", d));
        questions.add(new Question("The concert ___ next Saturday.", "is", "are", "be", d));
        questions.add(new Question("I ___ a new car last month.", "bought", "buy", "buys", d));
        questions.add(new Question("They ___ football every weekend.", "play", "plays", "played", d));
        questions.add(new Question("The children ___ TV right now.", "are watching", "watch", "watches", d));
        questions.add(new Question("She ___ her passport.", "has lost", "lose", "lost", d));
        questions.add(new Question("We ___ the project next month.", "will complete", "complete", "completed", d));
        questions.add(new Question("He ___ to work yesterday.", "didn't go", "don't go", "doesn't go", d));
        questions.add(new Question("The book ___ on the desk.", "lies", "lie", "lay", d));
        questions.add(new Question("My mother ___ a cake for my birthday.", "baked", "bake", "bakes", d));
        questions.add(new Question("I ___ my homework when she called.", "was doing", "did", "do", d));
        questions.add(new Question("They ___ the house before we arrived.", "had left", "left", "leave", d));
        questions.add(new Question("The bird ___ across the sky.", "flew", "fly", "flies", d));
        questions.add(new Question("She ___ to become a teacher.", "hopes", "hope", "hoped", d));
        questions.add(new Question("We ___ to London next year.", "will move", "move", "moved", d));
        questions.add(new Question("He ___ the door and left.", "closed", "close", "closes", d));
        questions.add(new Question("The river ___ through the valley.", "flows", "flow", "flowed", d));
        questions.add(new Question("I ___ him since 2010.", "haven't seen", "didn't see", "don't see", d));
        questions.add(new Question("They ___ the mountain last year.", "climbed", "climb", "climbs", d));
        questions.add(new Question("The wind ___ strongly last night.", "blew", "blow", "blows", d));
        questions.add(new Question("She ___ her room every week.", "cleans", "clean", "cleaned", d));
        questions.add(new Question("We ___ a taxi to the airport.", "took", "take", "takes", d));
        questions.add(new Question("He ___ his meal and left.", "finished", "finish", "finishes", d));
        questions.add(new Question("The lights ___ out during the storm.", "went", "go", "goes", d));
        questions.add(new Question("My sister ___ medicine at university.", "is studying", "study", "studies", d));
        questions.add(new Question("I ___ to the doctor tomorrow.", "am going", "go", "went", d));
        questions.add(new Question("They ___ their breakfast.", "have eaten", "eat", "ate", d));
        questions.add(new Question("The clock ___ twelve.", "struck", "strike", "strikes", d));
        questions.add(new Question("She ___ a picture of the sunset.", "took", "take", "takes", d));
        questions.add(new Question("We ___ the news on TV.", "saw", "see", "seeing", d));
        questions.add(new Question("He ___ the window because it was hot.", "opened", "open", "opens", d));
        questions.add(new Question("The team ___ the championship.", "won", "win", "wins", d));
        questions.add(new Question("I ___ you to be careful.", "want", "wants", "wanted", d));
        questions.add(new Question("They ___ at the hotel for a week.", "stayed", "stay", "stays", d));
        questions.add(new Question("The snow ___ heavily last winter.", "fell", "fall", "falls", d));
        questions.add(new Question("She ___ her dress for the party.", "chose", "choose", "chooses", d));
        questions.add(new Question("We ___ our best.", "tried", "try", "tries", d));
        questions.add(new Question("He ___ his appointment.", "missed", "miss", "misses", d));
        questions.add(new Question("The flowers ___ beautiful.", "smell", "smells", "smelled", d));
        questions.add(new Question("My brother ___ to music all day.", "listens", "listen", "listened", d));
        questions.add(new Question("I ___ the answer to the question.", "knew", "know", "knows", d));
        questions.add(new Question("They ___ the bridge last month.", "built", "build", "builds", d));
        questions.add(new Question("The bell ___ at 9 AM.", "rings", "ring", "rang", d));
        questions.add(new Question("She ___ her homework before dinner.", "finished", "finish", "finishes", d));
        questions.add(new Question("We ___ the museum yesterday.", "visited", "visit", "visits", d));
        questions.add(new Question("He ___ a shower every morning.", "takes", "take", "took", d));
        questions.add(new Question("The car ___ in the garage.", "is", "are", "be", d));
        questions.add(new Question("I ___ my wallet. Have you seen it?", "have lost", "lose", "lost", d));
        questions.add(new Question("They ___ English at school.", "learn", "learns", "learned", d));
        questions.add(new Question("The moon ___ at night.", "shines", "shine", "shone", d));
        
        return questions;
    }
    
    // ===========================
    // ANTONYMS (77)
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
        questions.add(new Question("Opposite of Hard:", "soft", "solid", "rough", d));
        questions.add(new Question("Opposite of Clean:", "dirty", "wet", "dark", d));
        questions.add(new Question("Opposite of Young:", "old", "teen", "child", d));
        questions.add(new Question("Opposite of Up:", "down", "low", "flat", d));
        questions.add(new Question("Opposite of Begin:", "end", "stop", "start", d));
        questions.add(new Question("Opposite of Inside:", "outside", "inner", "within", d));
        questions.add(new Question("Opposite of Sweet:", "sour", "nice", "kind", d));
        questions.add(new Question("Opposite of Rich:", "poor", "wealthy", "full", d));
        questions.add(new Question("Opposite of Win:", "lose", "gain", "get", d));
        questions.add(new Question("Opposite of Love:", "hate", "like", "care", d));
        questions.add(new Question("Opposite of Day:", "night", "noon", "dawn", d));
        questions.add(new Question("Opposite of True:", "false", "real", "right", d));
        questions.add(new Question("Opposite of Give:", "take", "send", "offer", d));
        questions.add(new Question("Opposite of Buy:", "sell", "get", "trade", d));
        questions.add(new Question("Opposite of Enter:", "exit", "leave", "go", d));
        questions.add(new Question("Opposite of Forward:", "backward", "ahead", "onward", d));
        questions.add(new Question("Opposite of Long:", "short", "tall", "wide", d));
        questions.add(new Question("Opposite of Wide:", "narrow", "broad", "big", d));
        questions.add(new Question("Opposite of Heavy:", "light", "weight", "solid", d));
        questions.add(new Question("Opposite of Thick:", "thin", "fat", "wide", d));
        questions.add(new Question("Opposite of New:", "old", "fresh", "modern", d));
        questions.add(new Question("Opposite of Right:", "left", "correct", "wrong", d));
        questions.add(new Question("Opposite of East:", "west", "north", "south", d));
        questions.add(new Question("Opposite of North:", "south", "east", "west", d));
        questions.add(new Question("Opposite of Top:", "bottom", "peak", "high", d));
        questions.add(new Question("Opposite of Front:", "back", "rear", "behind", d));
        questions.add(new Question("Opposite of First:", "last", "beginning", "start", d));
        questions.add(new Question("Opposite of Near:", "far", "close", "next", d));
        questions.add(new Question("Opposite of Here:", "there", "where", "away", d));
        questions.add(new Question("Opposite of In:", "out", "inside", "within", d));
        questions.add(new Question("Opposite of War:", "peace", "fight", "battle", d));
        questions.add(new Question("Opposite of Life:", "death", "living", "alive", d));
        questions.add(new Question("Opposite of Push:", "pull", "press", "move", d));
        questions.add(new Question("Opposite of Build:", "destroy", "make", "create", d));
        questions.add(new Question("Opposite of Start:", "stop", "begin", "go", d));
        questions.add(new Question("Opposite of Add:", "subtract", "plus", "sum", d));
        questions.add(new Question("Opposite of Multiply:", "divide", "times", "add", d));
        questions.add(new Question("Opposite of Increase:", "decrease", "grow", "rise", d));
        questions.add(new Question("Opposite of Expand:", "shrink", "grow", "spread", d));
        questions.add(new Question("Opposite of Attack:", "defend", "fight", "strike", d));
        questions.add(new Question("Opposite of Question:", "answer", "ask", "query", d));
        questions.add(new Question("Opposite of Problem:", "solution", "issue", "trouble", d));
        questions.add(new Question("Opposite of Arrive:", "depart", "come", "reach", d));
        questions.add(new Question("Opposite of Arrival:", "departure", "coming", "leaving", d));
        questions.add(new Question("Opposite of Alive:", "dead", "living", "lively", d));
        questions.add(new Question("Opposite of Asleep:", "awake", "sleeping", "tired", d));
        questions.add(new Question("Opposite of Empty:", "full", "blank", "vacant", d));
        questions.add(new Question("Opposite of Female:", "male", "woman", "girl", d));
        questions.add(new Question("Opposite of Male:", "female", "man", "boy", d));
        questions.add(new Question("Opposite of Safe:", "dangerous", "secure", "sound", d));
        questions.add(new Question("Opposite of Same:", "different", "alike", "equal", d));
        questions.add(new Question("Opposite of Similar:", "different", "alike", "same", d));
        questions.add(new Question("Opposite of Natural:", "artificial", "real", "normal", d));
        questions.add(new Question("Opposite of Possible:", "impossible", "likely", "probable", d));
        questions.add(new Question("Opposite of Legal:", "illegal", "lawful", "valid", d));
        questions.add(new Question("Opposite of Polite:", "rude", "kind", "nice", d));
        questions.add(new Question("Opposite of Patient:", "impatient", "calm", "waiting", d));
        questions.add(new Question("Opposite of Honest:", "dishonest", "true", "fair", d));
        questions.add(new Question("Opposite of Kind:", "cruel", "nice", "gentle", d));
        questions.add(new Question("Opposite of Careful:", "careless", "cautious", "safe", d));
        questions.add(new Question("Opposite of Certain:", "uncertain", "sure", "definite", d));
        questions.add(new Question("Opposite of Visible:", "invisible", "seen", "clear", d));
        questions.add(new Question("Opposite of Perfect:", "imperfect", "flawless", "ideal", d));
        questions.add(new Question("Opposite of Regular:", "irregular", "normal", "usual", d));
        
        return questions;
    }
    
    // ===========================
    // SYNONYMS (78)
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
        questions.add(new Question("Synonym of Beautiful:", "pretty", "ugly", "plain", d));
        questions.add(new Question("Synonym of Tired:", "exhausted", "awake", "lazy", d));
        questions.add(new Question("Synonym of Funny:", "hilarious", "sad", "boring", d));
        questions.add(new Question("Synonym of Scared:", "afraid", "brave", "calm", d));
        questions.add(new Question("Synonym of Difficult:", "hard", "easy", "simple", d));
        questions.add(new Question("Synonym of Big:", "huge", "tiny", "little", d));
        questions.add(new Question("Synonym of Small:", "little", "giant", "tall", d));
        questions.add(new Question("Synonym of Sad:", "unhappy", "joyful", "cheerful", d));
        questions.add(new Question("Synonym of Easy:", "simple", "hard", "tough", d));
        questions.add(new Question("Synonym of Wrong:", "incorrect", "right", "true", d));
        questions.add(new Question("Synonym of Right:", "correct", "wrong", "bad", d));
        questions.add(new Question("Synonym of Help:", "assist", "hurt", "ignore", d));
        questions.add(new Question("Synonym of Choose:", "select", "drop", "lose", d));
        questions.add(new Question("Synonym of Try:", "attempt", "quit", "stop", d));
        questions.add(new Question("Synonym of Find:", "discover", "lose", "drop", d));
        questions.add(new Question("Synonym of Hide:", "conceal", "show", "reveal", d));
        questions.add(new Question("Synonym of Buy:", "purchase", "sell", "give", d));
        questions.add(new Question("Synonym of Sell:", "vend", "buy", "keep", d));
        questions.add(new Question("Synonym of Tell:", "inform", "hide", "lie", d));
        questions.add(new Question("Synonym of Ask:", "request", "answer", "ignore", d));
        questions.add(new Question("Synonym of Give:", "provide", "take", "keep", d));
        questions.add(new Question("Synonym of Take:", "receive", "give", "leave", d));
        questions.add(new Question("Synonym of Make:", "create", "destroy", "break", d));
        questions.add(new Question("Synonym of Break:", "shatter", "fix", "mend", d));
        questions.add(new Question("Synonym of Fix:", "repair", "break", "damage", d));
        questions.add(new Question("Synonym of Leave:", "depart", "arrive", "stay", d));
        questions.add(new Question("Synonym of Arrive:", "reach", "leave", "depart", d));
        questions.add(new Question("Synonym of Start:", "begin", "end", "stop", d));
        questions.add(new Question("Synonym of Stop:", "cease", "start", "continue", d));
        questions.add(new Question("Synonym of Show:", "display", "hide", "cover", d));
        questions.add(new Question("Synonym of Change:", "alter", "keep", "stay", d));
        questions.add(new Question("Synonym of Use:", "utilize", "waste", "ignore", d));
        questions.add(new Question("Synonym of Need:", "require", "want", "have", d));
        questions.add(new Question("Synonym of Want:", "desire", "hate", "reject", d));
        questions.add(new Question("Synonym of Like:", "enjoy", "hate", "dislike", d));
        questions.add(new Question("Synonym of Love:", "adore", "hate", "dislike", d));
        questions.add(new Question("Synonym of Hate:", "detest", "love", "like", d));
        questions.add(new Question("Synonym of Know:", "understand", "ignore", "forget", d));
        questions.add(new Question("Synonym of Think:", "believe", "know", "doubt", d));
        questions.add(new Question("Synonym of Remember:", "recall", "forget", "ignore", d));
        questions.add(new Question("Synonym of Forget:", "overlook", "remember", "recall", d));
        questions.add(new Question("Synonym of Work:", "labor", "rest", "play", d));
        questions.add(new Question("Synonym of Rest:", "relax", "work", "run", d));
        questions.add(new Question("Synonym of Run:", "sprint", "walk", "stop", d));
        questions.add(new Question("Synonym of Walk:", "stroll", "run", "fly", d));
        questions.add(new Question("Synonym of Talk:", "speak", "listen", "silent", d));
        questions.add(new Question("Synonym of Listen:", "hear", "ignore", "speak", d));
        questions.add(new Question("Synonym of Look:", "gaze", "ignore", "hide", d));
        questions.add(new Question("Synonym of See:", "observe", "miss", "ignore", d));
        questions.add(new Question("Synonym of Hear:", "listen", "ignore", "deaf", d));
        questions.add(new Question("Synonym of Feel:", "touch", "numb", "ignore", d));
        questions.add(new Question("Synonym of Hold:", "grasp", "drop", "release", d));
        questions.add(new Question("Synonym of Throw:", "toss", "catch", "hold", d));
        questions.add(new Question("Synonym of Catch:", "grab", "throw", "release", d));
        questions.add(new Question("Synonym of Open:", "unlock", "close", "shut", d));
        questions.add(new Question("Synonym of Close:", "shut", "open", "widen", d));
        questions.add(new Question("Synonym of Shout:", "yell", "whisper", "murmur", d));
        questions.add(new Question("Synonym of Send:", "dispatch", "receive", "keep", d));
        questions.add(new Question("Synonym of Receive:", "get", "send", "give", d));
        questions.add(new Question("Synonym of Wait:", "stay", "leave", "rush", d));
        questions.add(new Question("Synonym of Hurry:", "rush", "slow", "wait", d));
        questions.add(new Question("Synonym of Answer:", "reply", "ask", "ignore", d));
        questions.add(new Question("Synonym of Reply:", "respond", "ignore", "ask", d));
        questions.add(new Question("Synonym of Say:", "utter", "listen", "silent", d));
        questions.add(new Question("Synonym of Word:", "term", "silence", "nothing", d));
        questions.add(new Question("Synonym of Idea:", "concept", "fact", "reality", d));
        questions.add(new Question("Synonym of Thing:", "object", "nothing", "idea", d));
        questions.add(new Question("Synonym of Place:", "location", "nowhere", "time", d));
        questions.add(new Question("Synonym of Time:", "moment", "never", "space", d));
        questions.add(new Question("Synonym of Way:", "method", "block", "stop", d));
        questions.add(new Question("Synonym of Part:", "portion", "whole", "all", d));
        questions.add(new Question("Synonym of Nice:", "kind", "cruel", "mean", d));
        
        return questions;
    }
    
    // ===========================
    // CATEGORY GAME (15)
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
        questions.add(new Question("Find an Action Word", "Sleep", "Bed", "Night", d));
        questions.add(new Question("Find an Action Word", "Read", "Book", "Page", d));
        questions.add(new Question("Find an Action Word", "Think", "Brain", "Mind", d));
        questions.add(new Question("Find an Action Word", "Walk", "Shoe", "Street", d));
        
        return questions;
    }
    
    // ===========================
    // COMBINED: Grammar + Synonyms + Antonyms
    // ===========================
    
    public static List<Question> getAllLanguageQuestions() {
        List<Question> allQuestions = new ArrayList<>();
        allQuestions.addAll(getGrammarQuestions());    // 145
        allQuestions.addAll(getAntonymQuestions());    // 77
        allQuestions.addAll(getSynonymQuestions());    // 78
        return allQuestions; // Total: 300 questions
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