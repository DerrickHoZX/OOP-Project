package io.github.some_example_name.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.some_example_name.io.KeyCode;
import io.github.some_example_name.managers.SceneManager;

public class StartScene extends Scene {

    private final Viewport viewport;
    private Texture bg;

    public StartScene(SceneManager sceneManager, Viewport viewport) {
        super(sceneManager);
        this.viewport = viewport;
    }

    @Override
    public void onEnter() {
        bg = new Texture("startscene.png"); // your bg
    }

    @Override
    public void update(float dt) {
        // ✅ IMPORTANT: pushScene, NOT setScene
        if (sceneManager.getIOManager().isKeyJustPressed(KeyCode.ESCAPE)) {
            sceneManager.pushScene(new PauseScene(sceneManager, viewport, this));
            return;
        }

        // other updates...
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();
    }

    @Override
    public void onExit() {
        if (bg != null) bg.dispose();
    }
    
}
