package io.github.abstractengine.managers;

import io.github.abstractengine.collision.Boundary;
import io.github.abstractengine.collision.CollisionInfo;
import io.github.abstractengine.collision.SimulationCollisionHandler;
import io.github.abstractengine.collision.ICollisionDetector;
import io.github.abstractengine.collision.ICollisionHandler;
import io.github.abstractengine.entities.Entity;

import java.util.List;

/**
 * CollisionManager orchestrates collision detection and response.
 * It uses a detector to find collisions and a handler to respond to them.
 * Called every frame from the loop.
 */
public class CollisionManager {
    
    private ICollisionDetector detector;
    private ICollisionHandler handler;
    private EntityManager entityManager;
    private Boundary boundaries;
    
    /**
     * Creates a collision manager
     * @param boundaries Screen boundaries for edge detection
     * @param entityManager Entity manager to get collidable entities
     * @param detector Collision detection algorithm
     * @param handler Collision response logic
     */
    public CollisionManager(Boundary boundaries, EntityManager entityManager,
                           ICollisionDetector detector, ICollisionHandler handler) {
        this.boundaries = boundaries;
        this.entityManager = entityManager;
        this.detector = detector;
        this.handler = handler;
    }
    
    /**
     * Update collision detection and handling (call every frame)
     * @param deltaTime Time since last frame
     */
    public void update(float deltaTime) {
        // Get all collidable entities
        List<Entity> collidables = entityManager.getCollidableEntities();
        
        // Check entity-entity collisions
        List<CollisionInfo> collisions = detector.checkCollisions(collidables);
        
        // Handle each collision
        for (CollisionInfo collision : collisions) {
            handler.handleCollision(collision);
        }
        
        // Check boundary collisions for each entity
        for (Entity entity : collidables) {
            if (detector.checkBoundary(entity, boundaries)) {
                handler.handleBoundaryCollision(entity, boundaries);
            }
        }
        
        // Update any time-based effects (like speed boost timer)
        if (handler instanceof SimulationCollisionHandler) {
            ((SimulationCollisionHandler) handler).update(deltaTime);
        }
    }
    
    // Getters
    public ICollisionDetector getDetector() {
        return detector;
    }
    
    public ICollisionHandler getHandler() {
        return handler;
    }
    
    public Boundary getBoundaries() {
        return boundaries;
    }
    
    // Setters
    public void setDetector(ICollisionDetector detector) {
        this.detector = detector;
    }
    
    public void setHandler(ICollisionHandler handler) {
        this.handler = handler;
    }
    
    public void setBoundaries(Boundary boundaries) {
        this.boundaries = boundaries;
    }
    
    /**
     * Clear all collision data (call when changing scenes)
     */
    public void clear() {
        // Can be extended to clear cached collision data if needed
    }
}