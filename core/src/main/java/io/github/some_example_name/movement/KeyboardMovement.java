package io.github.some_example_name.movement;

import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.interfaces.Movable;
import io.github.some_example_name.io.KeyCode;
import io.github.some_example_name.managers.IOManager;

public class KeyboardMovement extends MovementComponent {

    private final IOManager io;

    private final float speed;

    // Boundary settings
    private final float screenWidth;
    private final float screenHeight;
    private final float spriteSize;

    public KeyboardMovement(IOManager io,
                            float speed,
                            float screenWidth,
                            float screenHeight,
                            float spriteSize) {
        super();
        this.io = io;
        this.speed = speed;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.spriteSize = spriteSize;
    }

    @Override
    public void update(Movable entity, float dt) {
        // Reset velocity
        velocity.set(0, 0);

        // Check Input (WASD + arrow keys) via IOManager
        if (io.isKeyDown(KeyCode.W) || io.isKeyDown(KeyCode.UP)) {
            velocity.y = speed;
        }
        if (io.isKeyDown(KeyCode.S) || io.isKeyDown(KeyCode.DOWN)) {
            velocity.y = -speed;
        }
        if (io.isKeyDown(KeyCode.A) || io.isKeyDown(KeyCode.LEFT)) {
            velocity.x = -speed;
        }
        if (io.isKeyDown(KeyCode.D) || io.isKeyDown(KeyCode.RIGHT)) {
            velocity.x = speed;
        }

        // Apply movement
        Vector2 pos = entity.getPosition();
        float newX = pos.x + velocity.x * dt;
        float newY = pos.y + velocity.y * dt;

        // Clamp to world bounds
        newX = clamp(newX, 0, screenWidth - spriteSize);
        newY = clamp(newY, 0, screenHeight - spriteSize);

        entity.setPosition(newX, newY);
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
