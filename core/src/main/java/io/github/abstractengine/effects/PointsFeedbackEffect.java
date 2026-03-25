package io.github.abstractengine.effects;

import com.badlogic.gdx.graphics.Color;

/**
 * Full-screen centered points message: 1s total with smooth fade-in then fade-out.
 */
public class PointsFeedbackEffect {

    private static final float TOTAL_DURATION = 1f;
    private static final float PEAK_AT = 0.5f;

    private String text;
    private final Color tint = new Color();
    private float timer;
    private boolean active;

    public void showGain(int points) {
        text = "+" + points + " pts";
        tint.set(0.25f, 0.95f, 0.45f, 1f);
        timer = 0f;
        active = true;
    }

    public void showLoss(int points) {
        text = "-" + points + " pts";
        tint.set(1f, 0.45f, 0.45f, 1f);
        timer = 0f;
        active = true;
    }

    public void update(float dt) {
        if (!active) {
            return;
        }
        timer += dt;
        if (timer >= TOTAL_DURATION) {
            active = false;
            timer = 0f;
        }
    }

    public boolean isActive() {
        return active;
    }

    public String getText() {
        return text;
    }

    /**
     * Multiply font alpha by this when drawing (0..1).
     */
    public float getAlpha() {
        if (!active) {
            return 0f;
        }
        if (timer <= PEAK_AT) {
            return Math.min(1f, timer / PEAK_AT);
        }
        return Math.max(0f, (TOTAL_DURATION - timer) / (TOTAL_DURATION - PEAK_AT));
    }

    public Color getTint() {
        float a = getAlpha();
        return new Color(tint.r, tint.g, tint.b, a);
    }
}