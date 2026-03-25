package io.github.abstractengine.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.abstractengine.interfaces.Movable;

import java.util.Random;

public class RandomMovement extends MovementComponent {

    private final float baseSpeed;
    private float speed;
    private float changeInterval;
    private float timeSinceLastChange;

    private float screenWidth;
    private float screenHeight;
    private float entitySize;

    private final Random random;
    private boolean frozen;

    public RandomMovement(float speed, float changeInterval, float screenW, float screenH, float entitySize) {
        super();
        this.baseSpeed = speed;
        this.speed = speed;
        this.changeInterval = changeInterval;
        this.screenWidth = screenW;
        this.screenHeight = screenH;
        this.entitySize = entitySize;

        this.timeSinceLastChange = 0;
        this.random = new Random();
        this.frozen = false;

        pickRandomDirection();
    }

    /**
     * Applies streak power-up modifiers. Call each frame before {@link #update(Movable, float)}.
     * When {@code frozen} is true, the entity does not move; unfreezing restores or picks a direction.
     */
    public void setEnemyModifiers(float speedMultiplier, boolean frozen) {
        boolean wasFrozen = this.frozen;
        this.frozen = frozen;
        this.speed = baseSpeed * speedMultiplier;

        if (!frozen) {
            if (wasFrozen || velocity.len2() < 1e-4f) {
                pickRandomDirection();
            } else {
                velocity.nor().scl(speed);
            }
        }
    }

    @Override
    public void update(Movable entity, float dt) {
        if (frozen) {
            return;
        }

        timeSinceLastChange += dt;

        if (timeSinceLastChange >= changeInterval) {
            pickRandomDirection();
            timeSinceLastChange -= changeInterval;
        }

        Vector2 currentPos = entity.getPosition();
        float newX = currentPos.x + velocity.x * dt;
        float newY = currentPos.y + velocity.y * dt;

        if (newX < 0) {
            newX = 0;
            velocity.x = -velocity.x;
        } else if (newX > screenWidth - entitySize) {
            newX = screenWidth - entitySize;
            velocity.x = -velocity.x;
        }

        if (newY < 0) {
            newY = 0;
            velocity.y = -velocity.y;
        } else if (newY > screenHeight - entitySize) {
            newY = screenHeight - entitySize;
            velocity.y = -velocity.y;
        }

        entity.setPosition(newX, newY);
    }

    private void pickRandomDirection() {
        float angle = random.nextFloat() * (float) (2 * Math.PI);
        float dirX = (float) Math.cos(angle);
        float dirY = (float) Math.sin(angle);
        velocity.set(dirX, dirY).nor().scl(speed);
    }
}
