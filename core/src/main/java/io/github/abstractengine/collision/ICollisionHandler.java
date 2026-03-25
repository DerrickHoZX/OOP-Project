package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Entity;

/**
 * Strategy interface for collision response handling.
 * Implementations define what happens when entities collide
 * with each other or with boundaries.
 */

public interface ICollisionHandler {
    
    /**
     * Handle a collision event between two entities
     * @param info Collision information containing both entities and collision details
     */
    void handleCollision(CollisionInfo info);
    
    /**
     * Handle collision between two entities (simplified)
     * @param entity1 First entity in collision
     * @param entity2 Second entity in collision
     */
    void handleEntityCollision(Entity entity1, Entity entity2);
    
    /**
     * Handle when an entity collides with the boundary (screen edge)
     * @param entity The entity that hit the boundary
     * @param boundary The boundary that was hit
     */
    void handleBoundaryCollision(Entity entity, Boundary boundary);
}