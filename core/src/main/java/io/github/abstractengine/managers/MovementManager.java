package io.github.abstractengine.managers;

import java.util.ArrayList;
import java.util.List;

import io.github.abstractengine.interfaces.Movable;

public class MovementManager {
    
    private List<Movable> movables; // All entities that can move
    
    public MovementManager() {
        this.movables = new ArrayList<>();
    }
    
    // Call every frame - updates all registered movable entities
    public void update(float dt) {
        for (Movable movable : movables) {
            // Get the movement component and update it
            if (movable.getMovementComponent() != null) {
                movable.getMovementComponent().update(movable, dt);
            }
        }
    }
    
    // Add entity to movement system
    public void register(Movable entity) {
        if (!movables.contains(entity)) {
            movables.add(entity);
        }
    }
    
    // Remove entity from movement system
    public void unregister(Movable entity) {
        movables.remove(entity);
    }
    
    // Get all registered movables (useful for debugging)
    public List<Movable> getMovables() {
        return movables;
    }
    
    // Clear all movables (useful for scene changes)
    public void clear() {
        movables.clear();
    }
}