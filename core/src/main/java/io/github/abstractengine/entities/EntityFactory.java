package io.github.abstractengine.entities;

public interface EntityFactory<T extends Entity> {
    // T is the generic return type defined when the factory is implemented
    T createEntity(float x, float y);
}