package io.github.abstractengine.managers;

public class StatisticsManager {

    // --- Core Stats ---
    private int score;
    private int currentStreak;
    private float timeRemaining;

    // --- Constants for Scoring Rules ---
    private final int BASE_POINTS = 10;
    private final int STREAK_MULTIPLIER = 5; // Extra points per streak level
    private final int INCORRECT_PENALTY = 5;
    private final int ENEMY_PENALTY = 15;

    /**
     * Initializes the manager with a specific time limit.
     * For a 5-minute game, pass in 300f (5 * 60 seconds).
     */
    public StatisticsManager(float timeLimitInSeconds) {
        reset(timeLimitInSeconds);
    }

    /**
     * Call this inside your main game loop's update method.
     */
    public void update(float dt) {
        if (timeRemaining > 0) {
            timeRemaining -= dt;
            if (timeRemaining < 0) {
                timeRemaining = 0; // Prevent negative time
            }
        }
    }

    // --- Scoring Events ---

    public void registerCorrectAnswer() {
        currentStreak++;
        // Formula: Base points + (Streak * Multiplier)
        int pointsEarned = BASE_POINTS + (currentStreak * STREAK_MULTIPLIER);
        score += pointsEarned;
        System.out.println("Correct! Streak: " + currentStreak + " | + " + pointsEarned + " pts");
    }

    public void registerIncorrectAnswer() {
        currentStreak = 0; // Break the streak
        score -= INCORRECT_PENALTY;
        if (score < 0) score = 0; // Optional: Prevent negative scores for kids
        System.out.println("Incorrect! Streak broken. | - " + INCORRECT_PENALTY + " pts");
    }

    public void registerEnemyCollision() {
        currentStreak = 0; // Break the streak
        score -= ENEMY_PENALTY;
        if (score < 0) score = 0;
        System.out.println("Hit by Enemy! | - " + ENEMY_PENALTY + " pts");
    }

    // --- Mastery Rating System ---

    /**
     * Calculates the star rating based on the final score.
     * You may need to tweak these thresholds based on playtesting.
     */
    public int calculateStarRating() {
        if (score >= 500) {
            return 3; // 3 Stars: High score; reflects mastery
        } else if (score >= 250) {
            return 2; // 2 Stars: Mid score; reflects proficiency
        } else if (score >= 100) {
            return 1; // 1 Star: Low score; learning phase
        } else {
            return 0; // 0 Stars: Below minimum threshold
        }
    }

    // --- Getters & Setters ---

    public int getScore() { return score; }
    public int getCurrentStreak() { return currentStreak; }
    public float getTimeRemaining() { return timeRemaining; }
    
    public boolean isTimeUp() {
        return timeRemaining <= 0;
    }

    /**
     * Resets the statistics for a new game session.
     */
    public void reset(float timeLimitInSeconds) {
        this.score = 0;
        this.currentStreak = 0;
        this.timeRemaining = timeLimitInSeconds;
    }
}