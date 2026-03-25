package io.github.abstractengine.collision;

/**
 * Encapsulates a single collision response rule for a specific
 * pair of entity types. Avoids long if/else chains in handlers
 * by isolating each rule into its own class.
 */

public interface ICollisionRule {

    /**
     * Apply this collision rule to the given collision information.
     *
     * @param info information about the collision, including both entities
     */
    void apply(CollisionInfo info);
}

