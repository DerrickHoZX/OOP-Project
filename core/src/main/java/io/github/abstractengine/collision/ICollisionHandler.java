package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Entity;

/**
 * ICollisionHandler defines the contract for responding to collisions.
 * Different implementations can have different game rules.
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