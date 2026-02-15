package io.github.some_example_name.movement;

import io.github.some_example_name.interfaces.Movable;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class RandomMovement extends MovementComponent {
    
    private float speed;              // How fast it moves
    private float changeInterval;     // How often to change direction (seconds)
    private float timeSinceLastChange; // Timer tracking when to change
    
    private Random random;
    
    public RandomMovement(float speed, float changeInterval) {
        super();
        this.speed = speed;
        this.changeInterval = changeInterval;
        this.timeSinceLastChange = 0;
        this.random = new Random();
        
        // Start with random direction
        pickRandomDirection();
    }
    
    @Override
    public void update(Movable entity, float dt) {
        // Update timer
        timeSinceLastChange += dt;
        
        // Change direction if interval has passed
        if (timeSinceLastChange >= changeInterval) {
            pickRandomDirection();
            timeSinceLastChange = 0; // Reset timer
        }
        
        // Move entity in current direction
        Vector2 currentPos = entity.getPosition();
        float newX = currentPos.x + velocity.x * dt;
        float newY = currentPos.y + velocity.y * dt;
        
        entity.setPosition(newX, newY);
    }
    
    private void pickRandomDirection() {
        // Generate random angle between 0 and 2*PI
        float angle = random.nextFloat() * (float)(2 * Math.PI);
        
        // Convert to direction vector
        float dirX = (float)Math.cos(angle);
        float dirY = (float)Math.sin(angle);
        
        // Set velocity
        velocity.set(dirX, dirY).nor().scl(speed);
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
        // Recalculate velocity with new speed
        if (velocity.len() > 0) {
            velocity.nor().scl(speed);
        }
    }
    
    public void setChangeInterval(float interval) {
        this.changeInterval = interval;
    }
    
    public float getSpeed() {
        return speed;
    }
    
    public float getChangeInterval() {
        return changeInterval;
    }
}