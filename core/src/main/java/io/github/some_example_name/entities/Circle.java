package io.github.some_example_name.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Circle extends CollidableEntity {

    public Circle(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.GREEN);
        
        // Calculate radius based on the width provided
        // We add half-width to x/y to draw from the center
        float radius = width / 2f;
        shapeRenderer.circle(x + radius, y + radius, radius);
    }
}