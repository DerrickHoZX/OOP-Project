package io.github.abstractengine.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.abstractengine.entities.NonCollidableEntity;

/**
 * A green translucent circle that protects the player from enemy damage
 * while inside. Spawns randomly and relocates periodically.
 */
public class SafeZone extends NonCollidableEntity {

    private float radius;
    private float relocateTimer;
    private float relocateInterval;

    public SafeZone(float x, float y, float radius, float relocateInterval) {
        super(x, y);
        this.radius = radius;
        this.relocateInterval = relocateInterval;
        this.relocateTimer = relocateInterval;
    }

    @Override
    public void update(float dt) {
        relocateTimer -= dt;
    }

    public boolean needsRelocate() {
        return relocateTimer <= 0;
    }

    public void resetTimer() {
        relocateTimer = relocateInterval;
    }

    public boolean isPlayerInside(float playerCenterX, float playerCenterY) {
        float cx = x + radius;
        float cy = y + radius;
        float dx = playerCenterX - cx;
        float dy = playerCenterY - cy;
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    public float getRadius() {
        return radius;
    }

    public float getCenterX() {
        return x + radius;
    }

    public float getCenterY() {
        return y + radius;
    }

    /**
     * Checks whether this safe zone circle overlaps an axis-aligned rectangle.
     * Treats an entity as "inside" if any part of its rectangle intersects the safe zone.
     */
    public boolean overlapsRectangle(float rectX, float rectY, float rectW, float rectH, float padding) {
        float cx = getCenterX();
        float cy = getCenterY();

        float closestX = Math.max(rectX, Math.min(cx, rectX + rectW));
        float closestY = Math.max(rectY, Math.min(cy, rectY + rectH));

        float dx = cx - closestX;
        float dy = cy - closestY;

        float effectiveR = radius + Math.max(0f, padding);
        return (dx * dx + dy * dy) <= (effectiveR * effectiveR);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        // Drawn separately via ShapeRenderer in StartScene since it needs transparency
    }

    /**
     * Draws the safe zone as a green translucent circle.
     * Must be called inside a ShapeRenderer.Filled begin/end block with blending enabled.
     */
    public void drawZone(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(0f, 1f, 0f, 0.05f);
        shapeRenderer.circle(x + radius, y + radius, radius, 64);

        // Border ring
        shapeRenderer.setColor(0f, 1f, 0f, 0.1f);
        float borderThickness = 3f;
        for (float r = radius - borderThickness; r <= radius; r += 0.5f) {
            shapeRenderer.circle(x + radius, y + radius, r, 64);
        }
    }
}