package io.github.some_example_name.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer; // Import added

public abstract class Entity {
    
    protected float x;
    protected float y;
    protected float rotation;
    protected boolean isActive;
    protected boolean isCollidable;

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
        this.rotation = 0;
        this.isActive = true;
        this.isCollidable = false;
    }

    public abstract void update(float dt);
    
    // UPDATED: Now accepts ShapeRenderer
    public abstract void render(ShapeRenderer shapeRenderer);

    public float getX() { return x; }
    public float getY() { return y; }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean isCollidable() {
        return isCollidable;
    }

    public void destroy() {
        this.isActive = false;
    }
    
    public boolean isActive() {
        return isActive;
    }
}