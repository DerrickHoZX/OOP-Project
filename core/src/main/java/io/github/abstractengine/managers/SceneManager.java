package io.github.abstractengine.managers;

import java.util.ArrayDeque;
import java.util.Deque;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.abstractengine.scene.Scene;

public class SceneManager {

    private final IOManager ioManager;
    private final Deque<Scene> stack = new ArrayDeque<>();

    public SceneManager(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    public IOManager getIOManager() {
        return ioManager;
    }

    // Replace everything (main menu / new session)
    public void setScene(Scene next) {
        while (!stack.isEmpty()) {
            stack.pop().onExit();
        }
        stack.push(next);
        next.onEnter();
    }

    // Overlay (pause) without exiting the underlying scene
    public void pushScene(Scene overlay) {
        stack.push(overlay);
        overlay.onEnter();
    }

    // Resume (remove top overlay)
    public void popScene() {
        if (stack.isEmpty()) return;
        stack.pop().onExit();
    }

    public void update(float dt) {
        Scene top = stack.peek();
        if (top != null) top.update(dt);
    }

    public void render(SpriteBatch batch) {
        Scene top = stack.peek();
        if (top != null) top.render(batch);
    }

    public void resize(int w, int h) {
        Scene top = stack.peek();
        if (top != null) top.resize(w, h);
    }

    public void dispose() {
        while (!stack.isEmpty()) {
            stack.pop().onExit();
        }
    }
    
}
