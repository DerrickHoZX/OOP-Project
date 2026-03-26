package io.github.abstractengine.game.movement;

/**
 * Optional capability for enemy movement components that can be modified
 * by temporary power-ups (speed multiplier, frozen state, and pre-turn pause).
 */
public interface EnemyMovementModifiable {
    void setEnemyModifiers(float speedMultiplier, boolean frozen);

    void setPreTurnPauseSeconds(float seconds);
}

