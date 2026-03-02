package io.github.abstractengine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Triangle extends CollidableEntity {

    private Texture aliveTexture;
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
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (aliveTexture != null) {
            batch.draw(aliveTexture, x, y, width, height);
        } else if (fallbackTexture != null) {
            batch.draw(fallbackTexture, x, y, width, height);
        }
    }
    
    public void dispose() {
        if (aliveTexture != null) aliveTexture.dispose();
        if (fallbackTexture != null) fallbackTexture.dispose();
    }
}