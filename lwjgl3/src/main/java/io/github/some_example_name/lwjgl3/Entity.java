package io.github.some_example_name.lwjgl3;

public abstract class Entity {
    
    // Protected fields (Inherited by children, not shadowed)
    protected float x;
    protected float y;
    protected float rotation;
    protected boolean isActive;
    protected boolean isCollidable;

    // Constructor
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
        this.rotation = 0;
        this.isActive = true;
        this.isCollidable = false; // Default
    }

    // Abstract methods: Children MUST implement these
    public abstract void update(float dt);
    public abstract void render();

    // Getters and Setters
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