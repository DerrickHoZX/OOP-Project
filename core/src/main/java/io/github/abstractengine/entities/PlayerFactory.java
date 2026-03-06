package io.github.abstractengine.entities;

public class PlayerFactory implements EntityFactory<Circle> {
    @Override
    public Circle createEntity(float x, float y) {
        // Assuming your Circle takes (x,y) or adjust if it takes (x,y,width,height)
        return new Circle(x, y, 60f, 60f); 
    }
}