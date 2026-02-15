package io.github.some_example_name.entities;

import com.badlogic.gdx.math.Rectangle; // Or java.awt.Rectangle depending on your library

public class CollidableEntity extends Entity {

    protected float width;
    protected float height;

    public CollidableEntity(float x, float y, float w, float h) {
        super(x, y);       
        this.width = w;
        this.height = h;
        this.isCollidable = true; // IMPORTANT: Flag this as collidable
    }

    @Override
    public void update(float dt) {
        // Implement physics/movement logic
    }

    @Override
    public void render() {
        // Implement draw logic
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public Rectangle getCollisionBox() {
        return new Rectangle(x, y, width, height);
    }

    // Concrete implementation (required because the class is not abstract)
    public void onCollision(Entity other) {
        // Default: do nothing. Override in subclasses like Player/Enemy.
        System.out.println("Collision detected with " + other);
    }
}