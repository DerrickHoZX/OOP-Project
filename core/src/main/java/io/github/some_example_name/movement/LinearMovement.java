package io.github.some_example_name.movement;

import io.github.some_example_name.interfaces.Movable;
import com.badlogic.gdx.math.Vector2;

public class LinearMovement extends MovementComponent {
    
    private Vector2 direction; // Which way to move (normalized)
    private float speed;       // How fast to move
    
    public LinearMovement(float directionX, float directionY, float speed) {
        super();
        this.direction = new Vector2(directionX, directionY);
        if (direction.len() > 0) {
            direction.nor(); // Normalize only if not zero
        }
        this.speed = speed;
        
        // Set initial velocity based on direction and speed
        this.velocity.set(direction).scl(speed);
    }
    
    @Override
    public void update(Movable entity, float dt) {
        // Get current position
        Vector2 currentPos = entity.getPosition();
        
        // Calculate new position: position += velocity * deltaTime
        float newX = currentPos.x + velocity.x * dt;
        float newY = currentPos.y + velocity.y * dt;
        
        // Update entity position
        entity.setPosition(newX, newY);
    }
    
    // Allow changing direction mid-movement
    public void setDirection(float x, float y) {
        this.direction.set(x, y);
        if (direction.len() > 0) {
            direction.nor();
        }
        this.velocity.set(direction).scl(speed);
    }
    
    // Allow changing speed mid-movement
    public void setSpeed(float speed) {
        this.speed = speed;
        this.velocity.set(direction).scl(speed);
    }
    
    public Vector2 getDirection() {
        return direction;
    }
    
    public float getSpeed() {
        return speed;
    }
}