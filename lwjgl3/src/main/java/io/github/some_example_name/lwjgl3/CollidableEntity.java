package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class CollidableEntity extends Entity {

    // Protected attributes
    protected Rectangle bounds;
    protected Vector2 velocity;

    // Constructor
    public CollidableEntity(float x, float y) {
        super(x, y);
        this.isCollidable = true; // Override default
        this.velocity = new Vector2(0, 0);
        this.bounds = new Rectangle(x, y, 0, 0); // Width/Height to be set by subclass
    }

    // Methods
    public Rectangle getBounds() {
        // Ensure bounds track the entity's position
        bounds.setPosition(position.x, position.y);
        return bounds;
    }

    public boolean checkCollision(Entity other) {
        if (other instanceof CollidableEntity) {
            CollidableEntity otherEntity = (CollidableEntity) other;
            return this.getBounds().overlaps(otherEntity.getBounds());
        }
        return false;
    }
}