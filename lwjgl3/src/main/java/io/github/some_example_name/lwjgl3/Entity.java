package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    
    // Protected attributes as defined in UML (denoted by '#')
    protected Vector2 position;
    protected float rotation;
    protected boolean isActive;
    protected boolean isCollidable;

    // Constructor
    public Entity(float x, float y) {
        this.position = new Vector2(x, y);
        this.rotation = 0.0f;
        this.isActive = true;
        this.isCollidable = false; // Default
    }

    // Abstract methods forced to be implemented by subclasses
    public abstract void update(float deltaTime);
    public abstract void render(SpriteBatch batch);

    // Concrete methods
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public boolean isCollidable() {
        return isCollidable;
    }

    public void destroy() {
        this.isActive = false;
    }
}