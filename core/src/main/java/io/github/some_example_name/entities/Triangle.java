package io.github.some_example_name.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Triangle extends CollidableEntity {

    public Triangle(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        
        // Draw triangle based on the bounding box (x, y, w, h)
        // 1. Bottom-Left corner
        // 2. Top-Center point
        // 3. Bottom-Right corner
        shapeRenderer.triangle(
            x, y,                          // Point 1
            x + (width / 2f), y + height,  // Point 2 (Peak)
            x + width, y                   // Point 3
        );
    }
}