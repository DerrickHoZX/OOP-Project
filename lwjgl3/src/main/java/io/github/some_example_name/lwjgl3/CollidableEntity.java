package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class CollidableEntity extends Entity {

    // Protected attributes defined in UML
    protected Rectangle bounds;
    protected Vector2 velocity;

    public CollidableEntity(float x, float y) {
        super(x, y);
        this.isCollidable = true; // Set collision flag
        this.velocity = new Vector2(0, 0);
        // Initialize bounds (size 0,0 by default, subclasses should set size)
        this.bounds = new Rectangle(x, y, 0, 0); 
    }

    // Returns the collision box, ensuring it syncs with position
    public Rectangle getBounds() {
        bounds.setPosition(position.x, position.y);
        return bounds;
    }

    // Basic collision check against another entity
    public boolean checkCollision(Entity other) {
        if (other instanceof CollidableEntity) {
            CollidableEntity otherCollidable = (CollidableEntity) other;
            return this.getBounds().overlaps(otherCollidable.getBounds());
        }
        return false;
    }
}