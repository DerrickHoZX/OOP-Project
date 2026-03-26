package io.github.abstractengine.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.abstractengine.entities.CollidableEntity;
import io.github.abstractengine.entities.DisposableEntity;

public class Circle extends CollidableEntity implements DisposableEntity {

    private Texture playerTexture;

    public Circle(float x, float y, float width, float height) {
        super(x, y, width, height);
        try {
            playerTexture = new Texture("Player.png");
        } catch (Exception e) {
            System.out.println("Could not load Player.png! Using fallback blue square.");
            playerTexture = null;
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.BLUE);
            pixmap.fill();
            playerTexture = new Texture(pixmap);
            pixmap.dispose();
        }
    }
    
    public float getRadius() {
        return width / 2f;
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        // Draw the sprite! 
        // Note: batch.begin() must be called in your Scene before calling this
        batch.draw(playerTexture, x, y, width, height);
        
        // (Optional) You can still use shapeRenderer here to draw debug hitboxes if needed
    }
    
    // Good OOP practice: Clean up textures when the entity is destroyed
    public void dispose() {
        if (playerTexture != null) {
            playerTexture.dispose();
            playerTexture = null;
        }
    }

    @Override
    public void disposeEntity() {
        dispose();
    }
}