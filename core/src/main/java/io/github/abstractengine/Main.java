package io.github.abstractengine;

import com.badlogic.gdx.ApplicationAdapter;
import io.github.abstractengine.game.GameAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.abstractengine.game.scenes.MainMenuScene;
import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.IOManager;
import io.github.abstractengine.managers.SceneManager;

public class Main extends ApplicationAdapter {

	private static final float VIRTUAL_W = 1280f;
	private static final float VIRTUAL_H = 720f;

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
        GameAssets.registerAll(ioManager.getAssets());
        ioManager.loadAssets();

        ioManager.getLogging().info(LogCategory.SYSTEM, "Engine started successfully");

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
        sceneManager.resize(width, height);
    }

    @Override
    public void dispose() {

        // Log shutdown BEFORE disposing IO
        if (ioManager != null) {
        	ioManager.getLogging().info(LogCategory.SYSTEM, "Engine started successfully");
        	}

        if (sceneManager != null) sceneManager.dispose();
        if (ioManager != null) ioManager.dispose();
        if (batch != null) batch.dispose();
    }
}
