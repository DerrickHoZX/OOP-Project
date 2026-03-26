package io.github.abstractengine.movement;

/**
 * Minimal interface for rendering a telegraphed animation/state
 * (e.g., warning sprites during enemy direction changes).
 */
public interface TelegraphState {
    boolean isInPauseBeforeTurn();

    /**
     * @return 1 for Enemy1-like sprite, 2 for Enemy2-like sprite, 0 for normal sprite.
     */
    int getTelegraphSpriteIndex();
}

