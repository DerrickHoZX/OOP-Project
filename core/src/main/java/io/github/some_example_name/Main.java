package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.some_example_name.managers.IOManager;
import io.github.some_example_name.managers.SceneManager;
import io.github.some_example_name.scene.MainMenuScene;

public class Main extends ApplicationAdapter {

    private static final float VIRTUAL_W = 800f;
    private static final float VIRTUAL_H = 800f;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private IOManager ioManager;
    private SceneManager sceneManager;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_W, VIRTUAL_H, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_W / 2f, VIRTUAL_H / 2f, 0);
        camera.update();

        ioManager = new IOManager();
        sceneManager = new SceneManager(ioManager);

        sceneManager.setScene(new MainMenuScene(sceneManager, viewport));
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        ioManager.update();

        sceneManager.update(dt);
        sceneManager.render(batch);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        sceneManager.resize(width, height); // if you have this method
    }

    @Override
    public void dispose() {
        if (sceneManager != null) sceneManager.dispose(); // if you have this method
        if (batch != null) batch.dispose();
    }
}
