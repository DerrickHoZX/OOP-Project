package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Entity;

import java.util.Objects;

/**
 * CollisionPairKey is a helper value object used to look up
 * the appropriate {@link ICollisionRule} for a pair of entities.
 *
 * The ordering of the two types does not matter – (A, B) is
 * considered equal to (B, A).
 */
final class CollisionPairKey {

    private final Class<? extends Entity> typeA;
    private final Class<? extends Entity> typeB;

    private CollisionPairKey(Class<? extends Entity> typeA,
                             Class<? extends Entity> typeB) {
        this.typeA = typeA;
        this.typeB = typeB;
    }

    /**
     * Creates an unordered key for the two entity types.
     */
    static CollisionPairKey of(Class<? extends Entity> t1,
                               Class<? extends Entity> t2) {
        if (t1 == null || t2 == null) {
            throw new IllegalArgumentException("Types must not be null");
        }

        // Ensure a stable ordering so (A, B) == (B, A)
        if (t1.getName().compareTo(t2.getName()) <= 0) {
            return new CollisionPairKey(t1, t2);
        }
        return new CollisionPairKey(t2, t1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollisionPairKey that = (CollisionPairKey) o;
        return typeA.equals(that.typeA) && typeB.equals(that.typeB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeA, typeB);
    }
}

