package io.github.abstractengine.interfaces;

import io.github.abstractengine.entities.Entity;

/**
 * Callback interface for game events triggered by collisions.
 * Decouples collision rules from specific scene implementations,
 * allowing the engine's collision system to remain game-agnostic.
 */
public interface GameEventListener {
    void onCorrectAnswer(int pointsGained);
    void onWrongAnswer(int pointsLost);
    void onEnemyHit(int pointsLost);
    void onAnswerSubmitted(boolean wasCorrect);
    void onEnemyDestroyed();
    void onItemCollected(Entity item);
    boolean isPlayerSafe();
}