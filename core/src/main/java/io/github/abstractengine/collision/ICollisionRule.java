package io.github.abstractengine.collision;

/**
 * ICollisionRule represents a single game rule that should be applied
 * when two specific kinds of entities collide.
 *
 * By encapsulating rules in their own classes, we avoid long
 * procedural if/else chains in collision handlers.
 */
public interface ICollisionRule {

    /**
     * Apply this collision rule to the given collision information.
     *
     * @param info information about the collision, including both entities
     */
    void apply(CollisionInfo info);
}

