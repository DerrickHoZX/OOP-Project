package io.github.abstractengine.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.managers.AssetManager;

public class MainMenuScene extends Scene {

    private final Viewport viewport;

    private Texture bg;
    private Stage stage;

    // UI resources owned by this scene
    private BitmapFont font;
    private Texture buttonTex;

    // Slider textures (manual style)
    private Texture sliderBgTex;
    private Texture sliderKnobTex;

    // Black panel behind volume UI
    private Texture volumePanelTex;

    public MainMenuScene(SceneManager sceneManager, Viewport viewport) {
        super(sceneManager);
        this.viewport = viewport;
    }

    @Override
    public void onEnter() {
        bg = new Texture(Assets.MAIN_MENU_BG);
        sceneManager.getIOManager().playMusic(AssetManager.MUSIC_MAIN_MENU, true);

        stage = new Stage(viewport);

        // Create simple black rectangle texture for buttons
        buttonTex = makeSolidTexture(UI.BUTTON_TEX_W, UI.BUTTON_TEX_H, UI.BUTTON_TEX_COLOR);
        font = new BitmapFont();

        TextButton.TextButtonStyle style = createButtonStyle(font, buttonTex);

        TextButton grammarBtn = new TextButton(UI.GRAMMAR_LABEL, style);
        TextButton categoryBtn = new TextButton(UI.CATEGORY_LABEL, style);
        TextButton exitBtn = new TextButton(UI.EXIT_LABEL, style);

        wireButtonActions(grammarBtn, categoryBtn, exitBtn);

        // ---- Buttons bottom-middle (center stacked) ----
        stage.addActor(buildButtonTable(grammarBtn, categoryBtn, exitBtn));

        // ----- Volume UI (Music + SFX sliders) -----
        stage.addActor(buildVolumePanel());

        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
    }

    private void wireButtonActions(TextButton grammarBtn, TextButton categoryBtn, TextButton exitBtn) {
        grammarBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "GRAMMAR button clicked");
                sceneManager.setScene(new StartScene(sceneManager, viewport, GameCategory.GRAMMAR));
            }
        });

        categoryBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "CATEGORIZATION button clicked");
                sceneManager.setScene(new StartScene(sceneManager, viewport, GameCategory.CATEGORIZATION));
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "EXIT button clicked");
                Gdx.app.exit();
            }
        });
    }

    private Table buildButtonTable(TextButton grammarBtn, TextButton categoryBtn, TextButton exitBtn) {
        float worldH = viewport.getWorldHeight();

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.center();

        // push buttons lower so "WELCOME TO WORDNITE" is visible
        buttonTable.padTop(worldH * UI.BUTTONS_PADTOP_MULT);

        buttonTable.add(grammarBtn).width(UI.BUTTON_W).height(UI.BUTTON_H).padBottom(UI.BUTTON_GAP);
        buttonTable.row();
        buttonTable.add(categoryBtn).width(UI.BUTTON_W).height(UI.BUTTON_H).padBottom(UI.BUTTON_GAP);
        buttonTable.row();
        buttonTable.add(exitBtn).width(UI.BUTTON_W).height(UI.BUTTON_H);

        return buttonTable;
    }

    private Table buildVolumePanel() {
        Slider.SliderStyle sliderStyle = makeSliderStyle();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        final Slider musicSlider = new Slider(0f, 1f, 0.01f, false, sliderStyle);
        final Slider sfxSlider   = new Slider(0f, 1f, 0.01f, false, sliderStyle);

        // Load saved values
        musicSlider.setValue(sceneManager.getIOManager().getMusicVolume());
        sfxSlider.setValue(sceneManager.getIOManager().getSfxVolume());

        final Label musicLabel = new Label("Music: " + (int)(musicSlider.getValue() * 100) + "%", labelStyle);
        final Label sfxLabel   = new Label("SFX: " + (int)(sfxSlider.getValue() * 100) + "%", labelStyle);

        musicSlider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                float v = musicSlider.getValue();
                sceneManager.getIOManager().setMusicVolume(v);
                musicLabel.setText("Music: " + (int)(v * 100) + "%");
            }
        });

        sfxSlider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                float v = sfxSlider.getValue();
                sceneManager.getIOManager().setSfxVolume(v);
                sfxLabel.setText("SFX: " + (int)(v * 100) + "%");
            }
        });

        // Panel only behind the sliders (NOT full screen)
        Table volumeTable = new Table();

        volumePanelTex = makeSolidTexture(UI.VOL_PANEL_W, UI.VOL_PANEL_H, UI.VOL_PANEL_COLOR);
        volumeTable.setBackground(new TextureRegionDrawable(new TextureRegion(volumePanelTex)));

        volumeTable.pad(UI.VOL_PANEL_INNER_PAD);
        volumeTable.defaults().padBottom(UI.VOL_ROW_GAP);

        volumeTable.add(musicLabel).left();
        volumeTable.row();
        volumeTable.add(musicSlider).width(UI.SLIDER_W).height(UI.SLIDER_H);

        volumeTable.row().padTop(UI.VOL_SECTION_GAP);
        volumeTable.add(sfxLabel).left();
        volumeTable.row();
        volumeTable.add(sfxSlider).width(UI.SLIDER_W).height(UI.SLIDER_H);

        volumeTable.pack();

        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        // Centered on bus, slightly higher
        volumeTable.setPosition(
                (worldW - volumeTable.getWidth()) / 2f,
                worldH * UI.VOL_Y_MULT
        );

        return volumeTable;
    }

    private TextButton.TextButtonStyle createButtonStyle(BitmapFont font, Texture buttonTex) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.up = new TextureRegionDrawable(buttonTex);
        style.down = new TextureRegionDrawable(buttonTex);
        style.over = new TextureRegionDrawable(buttonTex);
        return style;
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
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

        if (sliderBgTex != null) sliderBgTex.dispose();
        if (sliderKnobTex != null) sliderKnobTex.dispose();

        if (volumePanelTex != null) volumePanelTex.dispose();

        sceneManager.getIOManager().stopMusic();
    }

    private Texture makeSolidTexture(int w, int h, Color c) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(c);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    private Slider.SliderStyle makeSliderStyle() {
        sliderBgTex = makeSolidTexture(UI.SLIDER_BG_W, UI.SLIDER_BG_H, UI.SLIDER_BG_COLOR);
        sliderKnobTex = makeSolidTexture(UI.SLIDER_KNOB_W, UI.SLIDER_KNOB_H, UI.SLIDER_KNOB_COLOR);

        Slider.SliderStyle ss = new Slider.SliderStyle();
        ss.background = new TextureRegionDrawable(new TextureRegion(sliderBgTex));
        ss.knob = new TextureRegionDrawable(new TextureRegion(sliderKnobTex));
        return ss;
    }

    // ---------------------------
    // SOLID helpers (same file)
    // ---------------------------

    private static final class Assets {
        static final String MAIN_MENU_BG = "mainmenu.jpeg";
        private Assets() {}
    }

    private static final class UI {
        // Labels
        static final String GRAMMAR_LABEL = "GRAMMAR";
        static final String CATEGORY_LABEL = "CATEGORIZATION";
        static final String EXIT_LABEL = "EXIT";

        // Button texture
        static final int BUTTON_TEX_W = 460;
        static final int BUTTON_TEX_H = 90;
        static final Color BUTTON_TEX_COLOR = new Color(0.15f, 0.15f, 0.15f, 0.90f);

        // Button layout
        static final float BUTTON_W = 340f;
        static final float BUTTON_H = 70f;
        static final float BUTTON_GAP = 18f;
        static final float BUTTONS_PADTOP_MULT = 0.55f;

        // Volume panel
        static final int VOL_PANEL_W = 600;
        static final int VOL_PANEL_H = 200;
        static final Color VOL_PANEL_COLOR = new Color(0f, 0f, 0f, 0.45f);
        static final float VOL_PANEL_INNER_PAD = 18f;
        static final float VOL_ROW_GAP = 12f;
        static final float VOL_SECTION_GAP = 14f;

        // Slider sizing
        static final float SLIDER_W = 420f;
        static final float SLIDER_H = 26f;

        // Slider textures
        static final int SLIDER_BG_W = 300;
        static final int SLIDER_BG_H = 10;
        static final Color SLIDER_BG_COLOR = new Color(1f, 1f, 1f, 0.35f);

        static final int SLIDER_KNOB_W = 18;
        static final int SLIDER_KNOB_H = 26;
        static final Color SLIDER_KNOB_COLOR = new Color(1f, 1f, 1f, 0.95f);

        // Vertical position multiplier for volume panel (higher = bigger)
        static final float VOL_Y_MULT = 0.62f;

        private UI() {}
    }
}