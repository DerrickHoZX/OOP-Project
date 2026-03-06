package io.github.abstractengine.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.SceneManager;

public class GameModeScene extends Scene {

    private final Viewport viewport;

    private Stage stage;
    private Texture bg;
    private BitmapFont font;
    private Texture invisibleTex;

    public GameModeScene(SceneManager sceneManager, Viewport viewport) {
        super(sceneManager);
        this.viewport = viewport;
    }

    @Override
    public void onEnter() {
        stage = new Stage(viewport);

        bg = new Texture(Assets.GAMEMODE_BG);

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );

        invisibleTex = makeSolidTexture(4, 4, new Color(1f, 1f, 1f, 0f));

        TextButton.TextButtonStyle invisibleStyle = createInvisibleButtonStyle();

        TextButton grammarBtn = new TextButton("", invisibleStyle);
        TextButton categorizationBtn = new TextButton("", invisibleStyle);

        grammarBtn.setSize(UI.GRAMMAR_W, UI.GRAMMAR_H);
        grammarBtn.setPosition(UI.GRAMMAR_X, UI.GRAMMAR_Y);

        categorizationBtn.setSize(UI.CATEGORY_W, UI.CATEGORY_H);
        categorizationBtn.setPosition(UI.CATEGORY_X, UI.CATEGORY_Y);

        grammarBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "GRAMMAR mode selected");
                sceneManager.setScene(new StartScene(sceneManager, viewport, GameCategory.GRAMMAR));
            }
        });

        categorizationBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "CATEGORIZATION mode selected");
                sceneManager.setScene(new StartScene(sceneManager, viewport, GameCategory.CATEGORIZATION));
            }
        });

        stage.addActor(grammarBtn);
        stage.addActor(categorizationBtn);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
    }

    private TextButton.TextButtonStyle createInvisibleButtonStyle() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        TextureRegionDrawable invisibleDrawable =
                new TextureRegionDrawable(new TextureRegion(invisibleTex));

        style.font = font;
        style.fontColor = new Color(1f, 1f, 1f, 0f);
        style.up = invisibleDrawable;
        style.down = invisibleDrawable;
        style.over = invisibleDrawable;

        return style;
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        if (invisibleTex != null) invisibleTex.dispose();
    }

    private Texture makeSolidTexture(int w, int h, Color c) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(c);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    private static final class Assets {
        static final String GAMEMODE_BG = "gamemode_scene.png";
        private Assets() {}
    }

    private static final class UI {
        // Left card: Grammar
        static final float GRAMMAR_X = 365f;
        static final float GRAMMAR_Y = 225f;
        static final float GRAMMAR_W = 300f;
        static final float GRAMMAR_H = 260f;

        // Right card: Categorization
        static final float CATEGORY_X = 705f;
        static final float CATEGORY_Y = 225f;
        static final float CATEGORY_W = 300f;
        static final float CATEGORY_H = 260f;

        private UI() {}
    }
}