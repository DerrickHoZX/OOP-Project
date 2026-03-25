package io.github.abstractengine.effects;

import io.github.abstractengine.entities.PowerUpType;

/**
 * Timed streak pickup buffs (player speed, enemy slow, freeze). Lives with other short-lived
 * gameplay feedback like {@link ScreenFlash} and {@link PointsFeedbackEffect}.
 * Collecting the same type again refreshes its duration.
 */
public class StreakPowerUpRuntime {

    public static final float DURATION = 10f;

    private static final float PLAYER_BOOST_MULT = 1.55f;
    private static final float ENEMY_SLOW_MULT = 0.35f;

    private float cherryTimer;
    private float bananaTimer;
    private float melonTimer;

    public void reset() {
        cherryTimer = 0f;
        bananaTimer = 0f;
        melonTimer = 0f;
    }

    public void activate(PowerUpType type) {
        switch (type) {
            case CHERRY:
                cherryTimer = DURATION;
                break;
            case BANANA:
                bananaTimer = DURATION;
                break;
            case WATERMELON:
                melonTimer = DURATION;
                break;
        }
    }

    public void update(float dt) {
        cherryTimer = Math.max(0f, cherryTimer - dt);
        bananaTimer = Math.max(0f, bananaTimer - dt);
        melonTimer = Math.max(0f, melonTimer - dt);
    }

    public float getPlayerSpeedMultiplier() {
        if (cherryTimer > 0f || melonTimer > 0f) {
            return PLAYER_BOOST_MULT;
        }
        return 1f;
    }

    public float getEnemySpeedMultiplier() {
        if (melonTimer > 0f) {
            return 1f;
        }
        if (bananaTimer > 0f) {
            return ENEMY_SLOW_MULT;
        }
        return 1f;
    }

    public boolean enemiesFrozen() {
        return melonTimer > 0f;
    }

    public boolean isCherryActive() {
        return cherryTimer > 0f;
    }

    public boolean isBananaActive() {
        return bananaTimer > 0f;
    }

    public boolean isWatermelonActive() {
        return melonTimer > 0f;
    }

    public float getCherryTimeLeft() {
        return cherryTimer;
    }

    public float getBananaTimeLeft() {
        return bananaTimer;
    }

    public float getWatermelonTimeLeft() {
        return melonTimer;
    }

    /** Lines to show under Score/Streak (each active buff is one line). */
    public int countActiveBuffLines() {
        int n = 0;
        if (cherryTimer > 0f) {
            n++;
        }
        if (bananaTimer > 0f) {
            n++;
        }
        if (melonTimer > 0f) {
            n++;
        }
        return n;
    }
}
