package io.github.abstractengine.collision;

import com.badlogic.gdx.math.Vector2;
import io.github.abstractengine.entities.Entity;

/**
 * CollisionInfo stores information about a collision event between two entities.
 * This is passed to collision handlers to determine what action to take.
 */
public class CollisionInfo {
    
    private Entity entity1;
    private Entity entity2;
    private Vector2 collisionPoint;    // Where the collision occurred
    private Vector2 collisionNormal;   // Direction from entity1 to entity2
    private float penetrationDepth;    // How much they're overlapping
    
    /**
     * Creates collision information for entity-entity collision
     */
    public CollisionInfo(Entity entity1, Entity entity2, Vector2 collisionPoint, 
                        Vector2 collisionNormal, float penetrationDepth) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.collisionPoint = collisionPoint;
        this.collisionNormal = collisionNormal;
        this.penetrationDepth = penetrationDepth;
    }
    
    /**
     * Simplified constructor for basic collisions
     */
    public CollisionInfo(Entity entity1, Entity entity2) {
        this(entity1, entity2, new Vector2(), new Vector2(), 0f);
    }
    
    // Getters
    public Entity getEntity1() { return entity1; }
    public Entity getEntity2() { return entity2; }
    public Vector2 getCollisionPoint() { return collisionPoint; }
    public Vector2 getCollisionNormal() { return collisionNormal; }
    public float getPenetrationDepth() { return penetrationDepth; }
}