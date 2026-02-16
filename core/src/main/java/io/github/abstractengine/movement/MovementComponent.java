package io.github.abstractengine.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.abstractengine.interfaces.Movable;

public abstract class MovementComponent {
    
    protected Vector2 velocity; // Speed + direction combined
    
    public MovementComponent() {
        this.velocity = new Vector2(0, 0);
    }
    
    // Abstract method: Each subclass implements its own movement logic
    public abstract void update(Movable entity, float dt);
    
    // Getter for velocity
    public Vector2 getVelocity() {
        return velocity;
    }
    
    // Setter for velocity
    public void setVelocity(float x, float y) {
        this.velocity.set(x, y);
    }
}