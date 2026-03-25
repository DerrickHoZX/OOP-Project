package io.github.abstractengine.entities;

/**
 * Streak milestones that spawn a pickup. Effects last 10 seconds when collected.
 */
public enum PowerUpType {
    /** Streak ≥ 3: player moves faster. */
    CHERRY(3),
    /** Streak ≥ 5: enemies move slower. */
    BANANA(5),
    /** Streak ≥ 7: enemies stop; player moves faster. */
    WATERMELON(7);

    public final int streakRequired;

    PowerUpType(int streakRequired) {
        this.streakRequired = streakRequired;
    }
}
