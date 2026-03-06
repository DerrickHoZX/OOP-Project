package io.github.abstractengine.scene;
import io.github.abstractengine.managers.AssetManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.SceneManager;

public class UsernameScene extends Scene {

    private final Viewport viewport;

    private Stage stage;
    private Texture bg;

    private BitmapFont font;
    private BitmapFont titleFont;

    private Texture inputBgTex;
    private Texture invisibleTex;

    private TextField usernameField;

    public UsernameScene(SceneManager sceneManager, Viewport viewport) {
        super(sceneManager);
        this.viewport = viewport;
    }

    @Override
    public void onEnter() {
        stage = new Stage(viewport);

        bg = new Texture(Assets.USERNAME_BG);

        font = new BitmapFont();
        font.getData().setScale(2.0f);

        font.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );

        titleFont = new BitmapFont();
        titleFont.getData().setScale(1.6f);

        inputBgTex = makeSolidTexture(600, 90, new Color(0.95f, 0.95f, 0.90f, 1f));
        invisibleTex = makeSolidTexture(4, 4, new Color(1f, 1f, 1f, 0f));

        createUsernameInput();
        createButtons();

        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
    }

    private void createUsernameInput() {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = new Color(0.20f, 0.15f, 0.10f, 1f);
        textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(
                makeSolidTexture(3, 55, new Color(0.25f, 0.20f, 0.15f, 1f))
        ));
        textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(inputBgTex));

        usernameField = new TextField("", textFieldStyle);
        usernameField.setMessageText("Enter username");
        usernameField.setMaxLength(16);
        usernameField.setSize(UI.INPUT_W, UI.INPUT_H);
        usernameField.setPosition(UI.INPUT_X, UI.INPUT_Y);

        stage.addActor(usernameField);
        stage.setKeyboardFocus(usernameField);
    }

    private void createButtons() {
        TextButton.TextButtonStyle invisibleStyle = new TextButton.TextButtonStyle();
        TextureRegionDrawable invisibleDrawable = new TextureRegionDrawable(new TextureRegion(invisibleTex));
        invisibleStyle.font = font;
        invisibleStyle.fontColor = new Color(1f, 1f, 1f, 0f);
        invisibleStyle.up = invisibleDrawable;
        invisibleStyle.down = invisibleDrawable;
        invisibleStyle.over = invisibleDrawable;

        TextButton confirmBtn = new TextButton("", invisibleStyle);
        confirmBtn.setSize(UI.CONFIRM_W, UI.CONFIRM_H);
        confirmBtn.setPosition(UI.CONFIRM_X, UI.CONFIRM_Y);

        TextButton cancelBtn = new TextButton("", invisibleStyle);
        cancelBtn.setSize(UI.CANCEL_W, UI.CANCEL_H);
        cancelBtn.setPosition(UI.CANCEL_X, UI.CANCEL_Y);

        confirmBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText().trim();

                if (username.isEmpty()) {
                    sceneManager.getIOManager().getLogging().info(LogCategory.UI, "Username empty");
                    return;
                }

                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "Username entered: " + username);
                sceneManager.setScene(new GameModeScene(sceneManager, viewport));
            }
        });

        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "Username entry cancelled");
                sceneManager.setScene(new MainMenuScene(sceneManager, viewport));
            }
        });

        stage.addActor(confirmBtn);
        stage.addActor(cancelBtn);
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
        if (titleFont != null) titleFont.dispose();
        if (inputBgTex != null) inputBgTex.dispose();
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
        static final String USERNAME_BG = "username_screen.png";
        private Assets() {}
    }

    private static final class UI {

        static final float INPUT_W = 520f;
        static final float INPUT_H = 60f;

        static final float INPUT_X = 380f;
        static final float INPUT_Y = 315f;

        static final float CONFIRM_W = 130f;
        static final float CONFIRM_H = 95f;
        static final float CONFIRM_X = 470f;
        static final float CONFIRM_Y = 185f;

        static final float CANCEL_W = 130f;
        static final float CANCEL_H = 95f;
        static final float CANCEL_X = 675f;
        static final float CANCEL_Y = 185f;

    }
}