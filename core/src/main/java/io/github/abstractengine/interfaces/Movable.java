package io.github.abstractengine.interfaces;

import com.badlogic.gdx.math.Vector2;

import io.github.abstractengine.movement.MovementComponent;

public interface Movable {
    
    // Get current position
    Vector2 getPosition();
    
    // Set position (used by movement components)
    void setPosition(float x, float y);
    
    // Get the movement component attached to this entity
    MovementComponent getMovementComponent();
}