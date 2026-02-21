package io.github.abstractengine.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.SceneManager;

public class PauseScene extends Scene {

    private final Viewport viewport;
    private final Scene previousScene;

    private Stage stage;

    private Texture overlayTex;
    private Texture buttonTex;
    private BitmapFont font;

    public PauseScene(SceneManager sceneManager, Viewport viewport, Scene previousScene) {
        super(sceneManager);
        this.viewport = viewport;
        this.previousScene = previousScene;
    }

    @Override
    public void onEnter() {
        stage = new Stage(viewport);

        // Full-alpha texture; we apply transparency via batch.setColor in render()
        overlayTex = makeSolidTexture(1, 1, new Color(0f, 0f, 0f, 1f));

        // Button style (simple dark)
        font = new BitmapFont();
        buttonTex = makeSolidTexture(1, 1, new Color(0.15f, 0.15f, 0.15f, 1f));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.up = new TextureRegionDrawable(buttonTex);
        style.down = new TextureRegionDrawable(buttonTex);
        style.over = new TextureRegionDrawable(buttonTex);

        TextButton resumeBtn = new TextButton("RESUME", style);
        TextButton endBtn    = new TextButton("END SESSION", style);
        TextButton menuBtn   = new TextButton("MAIN MENU", style);

        // Button size (same as main menu)
        resumeBtn.setSize(320, 60);
        endBtn.setSize(320, 60);
        menuBtn.setSize(320, 60);

        float w = viewport.getWorldWidth();
        float h = viewport.getWorldHeight();

        // Vertical stack
        resumeBtn.setPosition((w - resumeBtn.getWidth()) / 2f, h * 0.56f);
        endBtn.setPosition((w - endBtn.getWidth()) / 2f, h * 0.46f);
        menuBtn.setPosition((w - menuBtn.getWidth()) / 2f, h * 0.36f);
 
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                sceneManager.getIOManager()
                        .log(LogCategory.UI, "RESUME button clicked");

                sceneManager.popScene();
            }
        });


        endBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                sceneManager.getIOManager()
                        .log(LogCategory.UI, "END SESSION button clicked");

                sceneManager.setScene(new EndScene(sceneManager, viewport));
            }
        });


        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                sceneManager.getIOManager()
                        .log(LogCategory.UI, "MAIN MENU button clicked");

                sceneManager.setScene(new MainMenuScene(sceneManager, viewport));
            }
        });



        stage.addActor(resumeBtn);
        stage.addActor(endBtn);
        stage.addActor(menuBtn);

        // Capture input while paused
        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
    }

    @Override
    public void update(float dt) {
        if (stage != null) stage.act(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        // 1) Draw the previous scene first (so pause overlays it)
        previousScene.render(batch);

        // 2) Draw 50% transparent overlay (force blending)
        batch.begin();
        batch.enableBlending();
        batch.setColor(1f, 1f, 1f, 0.50f);
        batch.draw(overlayTex, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setColor(1f, 1f, 1f, 1f);
        batch.end();

        // 3) Draw pause UI
        if (stage != null) stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) stage.getViewport().update(width, height, true);
    }

    @Override
    public void onExit() {
        if (stage != null) stage.dispose();
        if (font != null) font.dispose();
        if (buttonTex != null) buttonTex.dispose();
        if (overlayTex != null) overlayTex.dispose();
    }

    private Texture makeSolidTexture(int w, int h, Color c) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(c);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }
    
}