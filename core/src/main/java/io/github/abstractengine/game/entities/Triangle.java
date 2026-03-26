package io.github.abstractengine.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.abstractengine.entities.CollidableEntity;
import io.github.abstractengine.movement.TelegraphState;

public class Triangle extends CollidableEntity implements EnemyEntity {

    // Shared textures (loaded once) so we don't create many Texture objects
    // as the enemy count increases over time.
    private static Texture aliveTexture;
    private static Texture enemy1Texture;
    private static Texture enemy2Texture;
    private static Texture fallbackTexture;
    private static boolean texturesLoaded = false;

    public Triangle(float x, float y, float width, float height) {
        super(x, y, width, height);

        ensureTexturesLoaded();
    }

    private static void ensureTexturesLoaded() {
        if (texturesLoaded) return;

        // Normal (default) sprite.
        try {
            aliveTexture = new Texture("Enemy.png");
        } catch (Exception e) {
            System.out.println("Could not load Enemy.png! Using fallback red square.");
            aliveTexture = null;

            // Create a custom Red Texture so we don't need ShapeRenderer
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.RED);
            pixmap.fill();
            fallbackTexture = new Texture(pixmap);
            pixmap.dispose();
        }

        // Telegraphed textures for the pre-turn pause.
        enemy1Texture = tryLoadTexture("Enemy1.png");
        enemy2Texture = tryLoadTexture("Enemy2.png");

        texturesLoaded = true;
    }

    private static Texture tryLoadTexture(String path) {
        try {
            return new Texture(path);
        } catch (Exception e) {
            System.out.println("Could not load " + path + "! Reusing normal enemy sprite.");
            return null;
        }
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        Texture toDraw = aliveTexture;
        boolean usingFallback = (toDraw == null);
        if (usingFallback && fallbackTexture != null) {
            toDraw = fallbackTexture;
        }

        TelegraphState ts = null;
        if (getMovementComponent() instanceof TelegraphState) {
            ts = (TelegraphState) getMovementComponent();
        }

        if (ts != null && ts.isInPauseBeforeTurn()) {
            int idx = ts.getTelegraphSpriteIndex();
            if (idx == 1 && enemy1Texture != null) toDraw = enemy1Texture;
            else if (idx == 2 && enemy2Texture != null) toDraw = enemy2Texture;
            // idx==0 uses normal enemy sprite (or fallback if normal missing)
        }

        if (toDraw != null) {
            batch.draw(toDraw, x, y, width, height);
        }
    }
    
    public void dispose() {
        // Shared textures are intentionally not disposed per instance.
    }
}