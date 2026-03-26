package io.github.abstractengine.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.abstractengine.entities.CollidableEntity;
import io.github.abstractengine.movement.RandomMovement;

public class Triangle extends CollidableEntity {

    private Texture aliveTexture;
    private Texture enemy1Texture;
    private Texture enemy2Texture;
    private Texture fallbackTexture;

    public Triangle(float x, float y, float width, float height) {
        super(x, y, width, height);
        
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
    }

    private Texture tryLoadTexture(String path) {
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

        RandomMovement rm = null;
        if (getMovementComponent() instanceof RandomMovement) {
            rm = (RandomMovement) getMovementComponent();
        }

        if (rm != null && rm.isInPauseBeforeTurn()) {
            int idx = rm.getTelegraphSpriteIndex();
            if (idx == 1 && enemy1Texture != null) toDraw = enemy1Texture;
            else if (idx == 2 && enemy2Texture != null) toDraw = enemy2Texture;
            // idx==0 uses normal enemy sprite (or fallback if normal missing)
        }

        if (toDraw != null) {
            batch.draw(toDraw, x, y, width, height);
        }
    }
    
    public void dispose() {
        if (aliveTexture != null) aliveTexture.dispose();
        if (enemy1Texture != null) enemy1Texture.dispose();
        if (enemy2Texture != null) enemy2Texture.dispose();
        if (fallbackTexture != null) fallbackTexture.dispose();
    }
}