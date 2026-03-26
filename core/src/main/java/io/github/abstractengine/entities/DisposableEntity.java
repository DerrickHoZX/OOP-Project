package io.github.abstractengine.entities;

/**
 * Optional contract for entities that own disposable resources
 * (textures, fonts, etc.).
 */
public interface DisposableEntity {
    void disposeEntity();
}

