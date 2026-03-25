package io.github.abstractengine.entities;

/**
 * Generic factory interface for creating entities.
 * Implementations define how specific entity types are instantiated,
 * following the Factory design pattern.
 *
 * @param <T> the type of Entity this factory produces
 */

public interface EntityFactory<T extends Entity> {
    // T is the generic return type defined when the factory is implemented
    T createEntity(float x, float y);
}