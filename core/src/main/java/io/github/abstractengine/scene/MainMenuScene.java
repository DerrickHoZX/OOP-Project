package io.github.abstractengine.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.SceneManager;

public class MainMenuScene extends Scene {

    private final Viewport viewport;

    private Texture bg;

    private Stage stage;

    // UI resources owned by this scene
    private BitmapFont font;
    private Texture buttonTex;

    public MainMenuScene(SceneManager sceneManager, Viewport viewport) {
        super(sceneManager);
        this.viewport = viewport;
    }

    @Override
    public void onEnter() {
        bg = new Texture("mainmenu.jpeg");

        stage = new Stage(viewport);

        // Create simple black rectangle texture for buttons
        buttonTex = makeSolidTexture(460, 90, new Color(0.15f, 0.15f, 0.15f, 0.90f));
        font = new BitmapFont(); // default font

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.up = new TextureRegionDrawable(buttonTex);
        style.down = new TextureRegionDrawable(buttonTex); // same look, simple
        style.over = new TextureRegionDrawable(buttonTex); // same look, simple

        TextButton playBtn = new TextButton("PLAY", style);
        TextButton exitBtn = new TextButton("EXIT", style);

        // Center position using viewport world size
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        playBtn.pack();
        exitBtn.pack();

        playBtn.setSize(320, 60);
        exitBtn.setSize(320, 60);

        playBtn.setPosition((worldW - playBtn.getWidth()) / 2f, worldH * 0.34f);
        exitBtn.setPosition((worldW - exitBtn.getWidth()) / 2f, worldH * 0.22f);
        
        // added mouse click logging
        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                sceneManager.getIOManager()
                        .log(LogCategory.UI, "PLAY button clicked");

                sceneManager.setScene(new StartScene(sceneManager, viewport));
            }
        });

        // added mouse click logging
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                sceneManager.getIOManager()
                        .log(LogCategory.UI, "EXIT button clicked");

                Gdx.app.exit();
            }
        });


        stage.addActor(playBtn);
        stage.addActor(exitBtn);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        // IMPORTANT: SceneManager controls begin/end? In your current setup, Scene renders directly.
        // So each Scene should manage its own begin/end safely.

        batch.begin();
        batch.draw(bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        stage.draw();
    }

    @Override
    public void onExit() {
        if (stage != null) stage.dispose();
        if (bg != null) bg.dispose();
        if (font != null) font.dispose();
        if (buttonTex != null) buttonTex.dispose();
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
