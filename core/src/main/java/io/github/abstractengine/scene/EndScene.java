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

public class EndScene extends Scene {

    private final Viewport viewport;

    private Texture bg;
    private Stage stage;

    private BitmapFont font;
    private Texture buttonTex;

    public EndScene(SceneManager sceneManager, Viewport viewport) {
        super(sceneManager);
        this.viewport = viewport;
    }

    @Override
    public void onEnter() {

        bg = new Texture("endscene.jpg"); // must be inside assets root

        stage = new Stage(viewport);

        // Button style (same as others)
        font = new BitmapFont();
        buttonTex = makeSolidTexture(1, 1, new Color(0.15f, 0.15f, 0.15f, 1f));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.up = new TextureRegionDrawable(buttonTex);
        style.down = new TextureRegionDrawable(buttonTex);
        style.over = new TextureRegionDrawable(buttonTex);

        TextButton restartBtn = new TextButton("RESTART", style);
        TextButton menuBtn    = new TextButton("MAIN MENU", style);

        restartBtn.setSize(320, 60);
        menuBtn.setSize(320, 60);

        float w = viewport.getWorldWidth();
        float h = viewport.getWorldHeight();

        // Centered near bottom
        restartBtn.setPosition((w - restartBtn.getWidth()) / 2f, h * 0.25f);
        menuBtn.setPosition((w - menuBtn.getWidth()) / 2f, h * 0.15f);

        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                sceneManager.getIOManager()
                        .log(LogCategory.UI, "RESTART button clicked");

                sceneManager.setScene(new StartScene(sceneManager, viewport));
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


        stage.addActor(restartBtn);
        stage.addActor(menuBtn);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(SpriteBatch batch) {

        batch.begin();
        batch.draw(bg, 0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());
        batch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null)
            stage.getViewport().update(width, height, true);
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
