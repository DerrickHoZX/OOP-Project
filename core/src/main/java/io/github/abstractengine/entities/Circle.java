package io.github.abstractengine.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Circle extends CollidableEntity {

    private Texture playerTexture;

    public Circle(float x, float y, float width, float height) {
        super(x, y, width, height);
        // Load the transparent PNG from your assets folder
        playerTexture = new Texture("Player.png"); 
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
        playerTexture.dispose();
    }
}