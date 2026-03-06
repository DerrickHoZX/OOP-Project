package io.github.abstractengine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Square extends CollidableEntity {

    private String answerText;
    private boolean isCorrect;
    private BitmapFont font; 
    private Texture bgTexture; 

    public Square(float x, float y, float width, float height, String answerText, boolean isCorrect) {
        super(x, y, width, height);
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        
        this.font = new BitmapFont(); 
        this.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.font.getData().setScale(1.2f);
        this.font.setColor(Color.BLACK); 
        
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
        if (bgTexture != null) {
            batch.draw(bgTexture, x, y, width, height);
        }
        
        if (answerText != null && font != null && !answerText.isEmpty()) {
            font.draw(batch, answerText, x + 15f, y + (height / 2) + 10f);
        }
    }
    
    public void dispose() {
        if (font != null) font.dispose();
        if (bgTexture != null) bgTexture.dispose();
    }
}