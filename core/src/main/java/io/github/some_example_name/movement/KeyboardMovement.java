package io.github.some_example_name.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.some_example_name.interfaces.Movable;

public class KeyboardMovement extends MovementComponent {
    
    private float speed;
    
    // Boundary settings
    private float screenWidth;
    private float screenHeight;
    private float spriteSize;

    // Updated Constructor: Now requires screen dimensions
    public KeyboardMovement(float speed, float screenWidth, float screenHeight, float spriteSize) {
        super();
        this.speed = speed;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.spriteSize = spriteSize;
    }

    @Override
    public void update(Movable entity, float dt) {
        // Reset velocity
        velocity.set(0, 0);

        // Check Input
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity.y = speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.y = -speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x = -speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x = speed;
        }

        // Apply movement if moving
        if (velocity.x != 0 || velocity.y != 0) {
            float newX = entity.getPosition().x + (velocity.x * dt);
            float newY = entity.getPosition().y + (velocity.y * dt);
            
            // --- BOUNDARY CHECKS (CLAMP) ---
            
            // Prevent going off Left/Right edges
            if (newX < 0) {
                newX = 0;
            } else if (newX > screenWidth - spriteSize) {
                newX = screenWidth - spriteSize;
            }
            
            // Prevent going off Bottom/Top edges
            if (newY < 0) {
                newY = 0;
            } else if (newY > screenHeight - spriteSize) {
                newY = screenHeight - spriteSize;
            }

            entity.setPosition(newX, newY);
        }
    }
}