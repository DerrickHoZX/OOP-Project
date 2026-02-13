package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.math.Rectangle; // Ensure you have this dependency

public class CollidableEntity extends Entity {

    protected float width;
    protected float height;

    public CollidableEntity(float x, float y, float w, float h) {
        super(x, y);       // Pass x, y to parent
        this.width = w;
        this.height = h;
        this.isCollidable = true; // IMPORTANT: Flag this as collidable
    }

    @Override
    public void update(float dt) {
        // Implement physics or movement logic here
    }

    @Override
    public void render() {
        // Implement drawing logic here
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public Rectangle getCollisionBox() {
        return new Rectangle(x, y, width, height);
    }

    // Concrete implementation (was <<Abstract>> in old diagram)
    public void onCollision(Entity other) {
        // Default behavior (can be overridden by Player/Enemy classes)
        System.out.println("Collision with " + other);
    }
}