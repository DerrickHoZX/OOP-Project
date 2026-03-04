package io.github.abstractengine.effects;

import com.badlogic.gdx.graphics.Color;

/**
 * ScreenFlash - Creates temporary screen overlay effects for feedback
 * Usage: Show green flash for correct answers, red flash for wrong answers
 */
public class ScreenFlash {

    private Color flashColor;
    private float flashDuration;
    private float flashTimer;
    private boolean isFlashing;

    public ScreenFlash() {
        this.flashColor = new Color(0, 0, 0, 0);
        this.flashDuration = 0.3f; // 0.3 seconds
        this.flashTimer = 0f;
        this.isFlashing = false;
    }

    /**
     * Trigger a green flash (correct answer)
     */
    public void flashGreen() {
        flash(new Color(0f, 1f, 0f, 0.35f)); // Green with 35% opacity
    }

    /**
     * Trigger a red flash (wrong answer)
     */
    public void flashRed() {
        flash(new Color(1f, 0f, 0f, 0.35f)); // Red with 35% opacity
    }

    /**
     * Trigger a custom color flash
     */
    public void flash(Color color) {
        this.flashColor = color.cpy();
        this.flashTimer = flashDuration;
        this.isFlashing = true;
    }

    /**
     * Update the flash timer (call every frame)
     */
    public void update(float dt) {
        if (isFlashing) {
            flashTimer -= dt;

            if (flashTimer <= 0) {
                isFlashing = false;
                flashTimer = 0;
            }
        }
    }

    /**
     * Get the current flash color with fading opacity
     */
    public Color getCurrentColor() {
        if (!isFlashing) {
            return new Color(0, 0, 0, 0); // Transparent
        }

        // Fade out effect: opacity decreases as timer runs out
        float fadeProgress = flashTimer / flashDuration;
        Color fadedColor = flashColor.cpy();
        fadedColor.a = flashColor.a * fadeProgress; // Fade the alpha

        return fadedColor;
    }

    /**
     * Check if currently flashing
     */
    public boolean isFlashing() {
        return isFlashing;
    }

    /**
     * Set custom flash duration
     */
    public void setFlashDuration(float seconds) {
        this.flashDuration = seconds;
    }
}