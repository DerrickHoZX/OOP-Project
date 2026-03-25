package io.github.abstractengine.game.scenes;

import java.util.Locale;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.abstractengine.game.StatisticsManager;
import io.github.abstractengine.game.effects.StreakPowerUpRuntime;

/**
 * Renders the in-game HUD elements: score panel, streak counter,
 * timer display, question prompt, and power-up buff timers.
 * Extracted from StartScene to improve separation of concerns.
 */
public class HUDRenderer {

    private static final boolean SCORE_PANEL_TOP_LEFT = true;

    private final BitmapFont font;
    private final BitmapFont questionFont;
    private final GlyphLayout layout;

    // Score panel bounds (set during drawPanels, used during drawText)
    private float scorePanelX, scorePanelY, scorePanelW, scorePanelH;

    public HUDRenderer(BitmapFont font, BitmapFont questionFont, GlyphLayout layout) {
        this.font = font;
        this.questionFont = questionFont;
        this.layout = layout;
    }

    /**
     * Draws all HUD panel backgrounds (score, time, question).
     * Must be called inside a ShapeRenderer.Filled begin/end block.
     */
    public void drawPanels(ShapeRenderer shape, float worldW, float worldH,
                           StreakPowerUpRuntime streakPowerUpRuntime) {
        // Score/Streak panel
        drawScoreStreakPanel(shape, worldW, worldH, streakPowerUpRuntime);

        // Time panel (top-center)
        shape.setColor(0.10f, 0.16f, 0.26f, 0.95f);
        shape.rect(worldW / 2f - 100f, worldH - 60f, 200f, 45f);
        shape.setColor(0.25f, 0.45f, 0.75f, 0.95f);
        shape.rect(worldW / 2f - 100f, worldH - 60f + 42f, 200f, 3f);

        // Question panel (center-top)
        shape.setColor(1f, 1f, 1f, 0.95f);
        shape.rect(worldW / 2f - 300f, worldH - 150f, 600f, 55f);
    }

    /**
     * Draws all HUD text (score, streak, time, question, power-up timers).
     * Must be called inside a SpriteBatch begin/end block.
     */
    public void drawText(SpriteBatch batch, float worldW, float worldH,
                         StatisticsManager statsManager, String questionPrompt,
                         StreakPowerUpRuntime streakPowerUpRuntime) {
        int seconds = (int) Math.ceil(statsManager.getTimeRemaining());
        font.setColor(Color.WHITE);
        questionFont.setColor(Color.BLACK);

        // Score and streak
        drawScoreStreakText(batch, statsManager, streakPowerUpRuntime);

        // Time (centered)
        font.getData().setScale(1.8f);
        String timeText = "Time: " + seconds + "s";
        layout.setText(font, timeText);
        float timeX = (worldW - layout.width) / 2f;
        font.setColor(1f, 1f, 1f, 1f);
        drawTextWithShadow(batch, font, timeText, timeX, worldH - 32f);
        font.getData().setScale(1.5f);

        // Question (centered)
        layout.setText(questionFont, questionPrompt);
        float qX = (worldW - layout.width) / 2f;
        drawBlackTextWithShadow(batch, questionFont, questionPrompt, qX, worldH - 110f);
    }

    /**
     * Draws the points feedback label (e.g. "+15 pts" or "-5 pts").
     * Must be called inside a SpriteBatch begin/end block.
     */
    public void drawPointsFeedback(SpriteBatch batch, float worldW, float worldH,
                                   String text, Color tint) {
        float prevX = questionFont.getData().scaleX;
        float prevY = questionFont.getData().scaleY;
        questionFont.getData().setScale(2.8f);

        layout.setText(questionFont, text);
        float fx = (worldW - layout.width) / 2f;
        float fy = worldH * 0.52f;
        drawPointsFeedbackLabel(batch, questionFont, text, fx, fy, tint);

        questionFont.getData().setScale(prevX, prevY);
    }

    // ---------------------------
    // Private helpers
    // ---------------------------

    private void drawScoreStreakPanel(ShapeRenderer shape, float worldW, float worldH,
                                     StreakPowerUpRuntime streakPowerUpRuntime) {
        float padLeft = 20f;
        float padTop = 55f;
        float padBottom = 20f;
        float panelW = 185f;
        int buffLines = (streakPowerUpRuntime != null) ? streakPowerUpRuntime.countActiveBuffLines() : 0;
        float basePanelH = 72f;
        float panelH = basePanelH + (buffLines > 0 ? 14f + buffLines * 20f : 0f);

        float x = padLeft;
        float y = SCORE_PANEL_TOP_LEFT
                ? worldH - padTop - panelH
                : padBottom;

        shape.setColor(0.10f, 0.16f, 0.26f, 0.88f);
        shape.rect(x, y, panelW, panelH);
        shape.setColor(0.25f, 0.45f, 0.75f, 0.9f);
        shape.rect(x, y + panelH - 3f, panelW, 3f);

        scorePanelX = x;
        scorePanelY = y;
        scorePanelW = panelW;
        scorePanelH = panelH;
    }

    private void drawScoreStreakText(SpriteBatch batch, StatisticsManager statsManager,
                                    StreakPowerUpRuntime streakPowerUpRuntime) {
        float top = scorePanelY + scorePanelH;
        float scoreY = top - 14f;
        float streakY = top - 36f;

        String scoreText = "Score: " + statsManager.getScore();
        String streakText = "Streak: " + statsManager.getCurrentStreak();

        layout.setText(font, scoreText);
        float scoreX = scorePanelX + (scorePanelW - layout.width) / 2f;
        drawScorePanelText(batch, scoreText, scoreX, scoreY);

        layout.setText(font, streakText);
        float streakX = scorePanelX + (scorePanelW - layout.width) / 2f;
        drawScorePanelText(batch, streakText, streakX, streakY);

        drawPowerUpBuffTimers(batch, streakPowerUpRuntime);
    }

    private void drawPowerUpBuffTimers(SpriteBatch batch, StreakPowerUpRuntime streakPowerUpRuntime) {
        if (streakPowerUpRuntime == null || streakPowerUpRuntime.countActiveBuffLines() == 0) {
            return;
        }

        float savedScale = font.getData().scaleX;
        font.getData().setScale(1.05f);
        float lineSpacing = 18f;
        float y = (scorePanelY + scorePanelH) - 58f;

        if (streakPowerUpRuntime.isCherryActive()) {
            String t = String.format(Locale.US, "Cherry %.1fs", streakPowerUpRuntime.getCherryTimeLeft());
            font.setColor(1f, 0.45f, 0.52f, 1f);
            layout.setText(font, t);
            float tx = scorePanelX + (scorePanelW - layout.width) / 2f;
            drawScorePanelText(batch, t, tx, y);
            y -= lineSpacing;
        }
        if (streakPowerUpRuntime.isBananaActive()) {
            String t = String.format(Locale.US, "Banana %.1fs", streakPowerUpRuntime.getBananaTimeLeft());
            font.setColor(1f, 0.88f, 0.38f, 1f);
            layout.setText(font, t);
            float tx = scorePanelX + (scorePanelW - layout.width) / 2f;
            drawScorePanelText(batch, t, tx, y);
            y -= lineSpacing;
        }
        if (streakPowerUpRuntime.isWatermelonActive()) {
            String t = String.format(Locale.US, "Watermelon %.1fs", streakPowerUpRuntime.getWatermelonTimeLeft());
            font.setColor(0.35f, 0.92f, 0.52f, 1f);
            layout.setText(font, t);
            float tx = scorePanelX + (scorePanelW - layout.width) / 2f;
            drawScorePanelText(batch, t, tx, y);
        }

        font.getData().setScale(savedScale);
        font.setColor(Color.WHITE);
    }

    private void drawScorePanelText(SpriteBatch batch, String text, float x, float y) {
        Color original = font.getColor().cpy();
        font.setColor(0f, 0f, 0f, 0.4f);
        font.draw(batch, text, x + 1f, y - 1f);
        font.setColor(original);
        font.draw(batch, text, x, y);
    }

    private void drawTextWithShadow(SpriteBatch batch, BitmapFont f, String text, float x, float y) {
        Color original = f.getColor().cpy();
        f.setColor(0f, 0f, 0f, 0.85f);
        f.draw(batch, text, x + 2f, y - 2f);
        f.setColor(original);
        f.draw(batch, text, x, y);
    }

    private void drawBlackTextWithShadow(SpriteBatch batch, BitmapFont f, String text, float x, float y) {
        Color original = f.getColor().cpy();
        f.setColor(0.6f, 0.6f, 0.6f, 0.6f);
        f.draw(batch, text, x + 2f, y - 2f);
        f.setColor(original);
        f.draw(batch, text, x, y);
    }

    private void drawPointsFeedbackLabel(SpriteBatch batch, BitmapFont f, String text, float x, float y, Color c) {
        Color original = f.getColor().cpy();
        f.setColor(0f, 0f, 0f, c.a * 0.55f);
        f.draw(batch, text, x + 4f, y - 4f);
        f.setColor(c);
        f.draw(batch, text, x, y);
        f.setColor(original);
    }
}