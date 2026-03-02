package io.github.abstractengine.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticsManager {

    private int score;
    private int currentStreak;
    private float timeRemaining;

    private List<Integer> podiumScores;
    private final int MAX_PODIUM_SPOTS = 5;

    private final int BASE_POINTS = 10;
    private final int STREAK_MULTIPLIER = 5; 
    private final int INCORRECT_PENALTY = 5;
    private final int ENEMY_PENALTY = 15;

    public StatisticsManager(float timeLimitInSeconds) {
        this.podiumScores = new ArrayList<>();
        podiumScores.add(500);
        podiumScores.add(250);
        podiumScores.add(100);
        
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

    public void registerCorrectAnswer() {
        currentStreak++;
        int pointsEarned = BASE_POINTS + (currentStreak * STREAK_MULTIPLIER);
        score += pointsEarned;
    }

    public void registerIncorrectAnswer() {
        currentStreak = 0;
        score -= INCORRECT_PENALTY;
        if (score < 0) score = 0; 
    }

    public void registerEnemyCollision() {
        currentStreak = 0;
        score -= ENEMY_PENALTY;
        if (score < 0) score = 0;
    }

    public void recordFinalScoreForPodium() {
        podiumScores.add(score);
        Collections.sort(podiumScores, Collections.reverseOrder());
        if (podiumScores.size() > MAX_PODIUM_SPOTS) {
            podiumScores = podiumScores.subList(0, MAX_PODIUM_SPOTS);
        }
    }

    public List<Integer> getPodiumScores() { return podiumScores; }
    public boolean isNewHighScore() {
        if (podiumScores.isEmpty()) return false;
        return score >= podiumScores.get(0);
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

    public void reset(float timeLimitInSeconds) {
        this.score = 0;
        this.currentStreak = 0;
        this.timeRemaining = timeLimitInSeconds;
    }
}