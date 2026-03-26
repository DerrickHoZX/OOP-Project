package io.github.abstractengine.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.abstractengine.game.scenes.GameCategory;
import io.github.abstractengine.managers.IOManager;

public class StatisticsManager {

    public static final class LeaderboardEntry {
        public final String username;
        public final int score;

        public LeaderboardEntry(String username, int score) {
            this.username = username;
            this.score = score;
        }
    }

    private int score;
    private int currentStreak;
    private float timeRemaining;
    /** Length of the current round in seconds (set in {@link #reset(float)}). */
    private float matchDurationSeconds;

    private GameCategory category;
    private String username;

    private List<LeaderboardEntry> podiumEntries;
    private final int MAX_PODIUM_SPOTS = 5;

    /** One commit per play session; avoids double insert when timer and end screen both record. */
    private boolean finalScoreRecordedForSession;

    private final int BASE_POINTS = 10;
    private final int STREAK_MULTIPLIER = 5; 
    private final int INCORRECT_PENALTY = 5;
    private final int ENEMY_PENALTY = 15;

    // UPDATED: Constructor now matches the 4 arguments passed in StartScene
    public StatisticsManager(GameCategory category, String username, IOManager ioManager, float timeLimitInSeconds) {
        this.category = category;
        this.username = username;
        
        this.podiumEntries = new ArrayList<>();
        podiumEntries.add(new LeaderboardEntry("Lancea", 500));
        podiumEntries.add(new LeaderboardEntry("Wileen", 250));
        podiumEntries.add(new LeaderboardEntry("Derrick", 100));
        
        reset(timeLimitInSeconds);
    }

    public void update(float dt) {
        if (timeRemaining > 0) {
            timeRemaining -= dt;
            if (timeRemaining <= 0) {
                timeRemaining = 0;
                recordFinalScoreForPodium(); 
            }
        }
    }

    /** @return Points added to the score this call (for UI feedback). */
    public int registerCorrectAnswer() {
        currentStreak++;
        int pointsEarned = BASE_POINTS + (currentStreak * STREAK_MULTIPLIER);
        score += pointsEarned;
        return pointsEarned;
    }

    /** @return Points actually deducted (may be less than penalty if score was low). */
    public int registerIncorrectAnswer() {
        currentStreak = 0;
        int before = score;
        score -= INCORRECT_PENALTY;
        if (score < 0) {
            score = 0;
        }
        return before - score;
    }

    /** @return Points actually deducted (may be less than penalty if score was low). */
    public int registerEnemyCollision() {
        currentStreak = 0;
        int before = score;
        score -= ENEMY_PENALTY;
        if (score < 0) {
            score = 0;
        }
        return before - score;
    }

    public void recordFinalScoreForPodium() {
        if (finalScoreRecordedForSession) {
            return;
        }
        finalScoreRecordedForSession = true;
        podiumEntries.add(new LeaderboardEntry(username, score));
        Collections.sort(podiumEntries, (a, b) -> Integer.compare(b.score, a.score));
        if (podiumEntries.size() > MAX_PODIUM_SPOTS) {
            podiumEntries = new ArrayList<>(podiumEntries.subList(0, MAX_PODIUM_SPOTS));
        }
    }

    public List<LeaderboardEntry> getLeaderboard(GameCategory cat) {
        if (cat != category) {
            return new ArrayList<>();
        }
        return new ArrayList<>(podiumEntries);
    }

    public GameCategory getCategory() {
        return category;
    }

    public String getUsername() {
        return username;
    }

    public boolean isNewHighScore() {
        if (podiumEntries.isEmpty()) return false;
        return score >= podiumEntries.get(0).score;
    }

    public int calculateStarRating() {
        if (score >= 500) return 3;
        else if (score >= 250) return 2;
        else if (score >= 100) return 1;
        else return 0;
    }

    public int getScore() { return score; }
    public int getCurrentStreak() { return currentStreak; }
    public float getTimeRemaining() { return timeRemaining; }
    public boolean isTimeUp() { return timeRemaining <= 0; }

    /** Total seconds for this match (e.g. 60); used for difficulty scaling. */
    public float getMatchDurationSeconds() {
        return matchDurationSeconds;
    }

    /** Seconds since round start (0 at beginning of match). */
    public float getRoundElapsedSeconds() {
        return Math.max(0f, matchDurationSeconds - timeRemaining);
    }

    public void reset(float timeLimitInSeconds) {
        this.score = 0;
        this.currentStreak = 0;
        this.matchDurationSeconds = Math.max(0.01f, timeLimitInSeconds);
        this.timeRemaining = timeLimitInSeconds;
        this.finalScoreRecordedForSession = false;
    }
}