package io.github.abstractengine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Square extends CollidableEntity {
    
    private String answerText;
    private boolean isCorrect;
    private BitmapFont font; 
    private Texture bgTexture;
    private GlyphLayout layout;  // NEW: For measuring text width/height
    
    public Square(float x, float y, float width, float height, String answerText, boolean isCorrect) {
        super(x, y, width, height);
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        
        this.font = new BitmapFont(); 
        this.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.font.getData().setScale(1.2f);
        this.font.setColor(Color.BLACK); 
        
        this.layout = new GlyphLayout();  // NEW: Initialize layout
        
        // Create yellow background
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fill();
        this.bgTexture = new Texture(pixmap);
        pixmap.dispose();
    }
    
    public Square(float x, float y, float width, float height) {
        this(x, y, width, height, "", false); 
    }
    
    public void setAnswerDetails(String answerText, boolean isCorrect) {
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }
    
    public boolean isCorrect() {
        return isCorrect;
    }
    
    @Override
    public void update(float dt) {
    }
    
    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        // Draw yellow background
        if (bgTexture != null) {
            batch.draw(bgTexture, x, y, width, height);
        }
        
        // Draw centered text
        if (answerText != null && font != null && !answerText.isEmpty()) {
            // Measure the text dimensions
            layout.setText(font, answerText);
            
            // Calculate centered position
            float textX = x + (width - layout.width) / 2f;   // Center horizontally
            float textY = y + (height + layout.height) / 2f; // Center vertically
            
            // Draw the text
            font.draw(batch, answerText, textX, textY);
        }
    }
    
    public void dispose() {
        if (font != null) font.dispose();
        if (bgTexture != null) bgTexture.dispose();
    }
}