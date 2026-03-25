package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Entity;
import java.util.List;

/**
 * Strategy interface for collision detection algorithms.
 * Implementations can use different approaches such as
 * AABB, circle-based, or SAT collision detection.
 */

public interface ICollisionDetector {
    
    /**
     * Check all collisions between entities in the provided list
     * @param entities List of entities to check
     * @return List of collision information for all detected collisions
     */
    List<CollisionInfo> checkCollisions(List<Entity> entities);
    
    /**
     * Check collision between two circular entities (true circle collision)
     * @param circle1 First circular entity
     * @param circle2 Second circular entity
     * @return true if circles are overlapping
     */
    boolean checkCircle(Entity circle1, Entity circle2);
    
    /**
     * Check collision between two square entities (AABB)
     * @param square1 First square entity
     * @param square2 Second square entity
     * @return true if squares are overlapping
     */
    boolean checkSquare(Entity square1, Entity square2);
    
    /**
     * Check collision between a circle and a square
     * @param circle Circular entity
     * @param square Square entity
     * @return true if circle and square are overlapping
     */
    boolean checkCircleSquare(Entity circle, Entity square);
    
    /**
     * Check if an entity has collided with the boundary
     * @param entity Entity to check
     * @param boundary Screen boundaries
     * @return true if entity is touching or exceeding boundary
     */
    boolean checkBoundary(Entity entity, Boundary boundary);
}