package io.github.some_example_name.movement;

import io.github.some_example_name.interfaces.Movable;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class RandomMovement extends MovementComponent {
    
    private float speed;
    private float changeInterval;
    private float timeSinceLastChange;
    
    // Boundary settings
    private float screenWidth;
    private float screenHeight;
    private float entitySize; 
    
    private Random random;
    
    public RandomMovement(float speed, float changeInterval, float screenW, float screenH, float entitySize) {
        super();
        this.speed = speed;
        this.changeInterval = changeInterval;
        this.screenWidth = screenW;
        this.screenHeight = screenH;
        this.entitySize = entitySize;
        
        this.timeSinceLastChange = 0;
        this.random = new Random();
        
        pickRandomDirection();
    }
    
    @Override
    public void update(Movable entity, float dt) {
        timeSinceLastChange += dt;
        
        // 1. Change direction periodically
        if (timeSinceLastChange >= changeInterval) {
            pickRandomDirection();
            timeSinceLastChange -= changeInterval; 
        }
        
        // 2. Calculate proposed position
        Vector2 currentPos = entity.getPosition();
        float newX = currentPos.x + velocity.x * dt;
        float newY = currentPos.y + velocity.y * dt;
        
        // 3. Screen Boundary Check (Bounce Logic)
        if (newX < 0) {
            newX = 0;
            velocity.x = -velocity.x; // Bounce right
        } else if (newX > screenWidth - entitySize) {
            newX = screenWidth - entitySize;
            velocity.x = -velocity.x; // Bounce left
        }
        
        if (newY < 0) {
            newY = 0;
            velocity.y = -velocity.y; // Bounce up
        } else if (newY > screenHeight - entitySize) {
            newY = screenHeight - entitySize;
            velocity.y = -velocity.y; // Bounce down
        }
        
        // 4. Update Position
        entity.setPosition(newX, newY);
    }
    
    private void pickRandomDirection() {
        float angle = random.nextFloat() * (float)(2 * Math.PI);
        float dirX = (float)Math.cos(angle);
        float dirY = (float)Math.sin(angle);
        velocity.set(dirX, dirY).nor().scl(speed);
    }
}