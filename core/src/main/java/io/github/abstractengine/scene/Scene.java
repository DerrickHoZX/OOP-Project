package io.github.abstractengine.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.abstractengine.managers.SceneManager;

/**
 * Abstract base class for all scenes in the engine.
 * Scenes represent distinct application states (menus, gameplay, overlays).
 * Each scene has a clear lifecycle: onEnter, update, render, onExit.
 */

public abstract class Scene {

    protected final SceneManager sceneManager;

    protected Scene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
    public abstract void onEnter();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);
    public abstract void onExit();
    
    
    public void resize(int width, int height) {
        // default: do nothing 
    }
    
}
