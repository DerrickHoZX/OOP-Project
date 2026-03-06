package io.github.abstractengine.entities;

public class EnemyFactory implements EntityFactory<Triangle> {
    @Override
    public Triangle createEntity(float x, float y) {
        // Assuming your Triangle takes (x,y) or adjust if it takes (x,y,width,height)
        return new Triangle(x, y, 70f, 70f); 
    }
}