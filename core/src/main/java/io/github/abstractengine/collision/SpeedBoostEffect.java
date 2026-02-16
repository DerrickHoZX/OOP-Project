package io.github.abstractengine.collision;

import io.github.abstractengine.entities.CollidableEntity;
import io.github.abstractengine.movement.KeyboardMovement;

/**
 * SpeedBoostEffect manages a temporary speed boost for an entity.
 * After a duration, the entity's speed returns to normal.
 */
public class SpeedBoostEffect {
    
    private CollidableEntity target;
    private float originalSpeed;
    private float boostMultiplier;
    private float duration;
    private float timeRemaining;
    private boolean isActive;
    
    /**
     * Creates a speed boost effect
     * @param target The entity to boost
     * @param boostMultiplier Speed multiplier (e.g., 1.4 for 40% increase)
     * @param duration How long the boost lasts in seconds
     */
    public SpeedBoostEffect(CollidableEntity target, float boostMultiplier, float duration) {
        this.target = target;
        this.boostMultiplier = boostMultiplier;
        this.duration = duration;
        this.timeRemaining = duration;
        this.isActive = false;
    }
    
    /**
     * Activate the speed boost
     */
    public void activate() {
        if (isActive) {
            // If already boosted, just reset the timer
            timeRemaining = duration;
            return;
        }
        
        // Store original speed and apply boost
        if (target.getMovementComponent() instanceof KeyboardMovement) {
            KeyboardMovement movement = (KeyboardMovement) target.getMovementComponent();
            originalSpeed = movement.getSpeed();
            movement.setSpeed(originalSpeed * boostMultiplier);
            isActive = true;
            timeRemaining = duration;
        }
    }
    
    /**
     * Update the boost timer (call every frame)
     * @param dt Delta time
     */
    public void update(float dt) {
        if (!isActive) return;
        
        timeRemaining -= dt;
        
        if (timeRemaining <= 0) {
            deactivate();
        }
    }
    
    /**
     * End the boost and restore original speed
     */
    public void deactivate() {
        if (!isActive) return;
        
        if (target.getMovementComponent() instanceof KeyboardMovement) {
            KeyboardMovement movement = (KeyboardMovement) target.getMovementComponent();
            movement.setSpeed(originalSpeed);
        }
        
        isActive = false;
        timeRemaining = 0;
    }
    
    public boolean isActive() { return isActive; }
    public float getTimeRemaining() { return timeRemaining; }
}