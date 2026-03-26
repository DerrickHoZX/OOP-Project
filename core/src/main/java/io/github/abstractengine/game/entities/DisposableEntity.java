package io.github.abstractengine.game.entities;

/**
 * Optional contract for entities that own disposable resources
 * (textures, fonts, etc.).
 */
public interface DisposableEntity {
    void disposeEntity();
}

