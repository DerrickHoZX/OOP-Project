package io.github.abstractengine.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.abstractengine.managers.SceneManager;

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
