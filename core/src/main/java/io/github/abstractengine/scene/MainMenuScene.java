package io.github.abstractengine.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    private BitmapFont font;

    private Texture transparentTex;
    private Texture sliderBgTex;
    private Texture sliderKnobTex;
    private Texture volumePanelTex;

    private Table volumeTable;
    private ShapeRenderer debugRenderer;

    public MainMenuScene(SceneManager sceneManager, Viewport viewport) {
        super(sceneManager);
        this.viewport = viewport;
    }

    @Override
    public void onEnter() {
        bg = new Texture(Assets.MAIN_MENU_BG);
        sceneManager.getIOManager().playMusic(AssetManager.MUSIC_MAIN_MENU, true);

        stage = new Stage(viewport);
        debugRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font.getData().setScale(1.3f);

        transparentTex = makeSolidTexture(2, 2, new Color(1f, 1f, 1f, 0f));

        TextButton.TextButtonStyle invisibleStyle = createInvisibleButtonStyle(font, transparentTex);

        TextButton playBtn = new TextButton("", invisibleStyle);
        TextButton settingsBtn = new TextButton("", invisibleStyle);
        TextButton exitBtn = new TextButton("", invisibleStyle);

        wireButtonActions(playBtn, settingsBtn, exitBtn);
        addOverlayButtons(playBtn, settingsBtn, exitBtn);

        volumeTable = buildVolumePanel();
        volumeTable.setVisible(false);
        stage.addActor(volumeTable);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
    }

    private void wireButtonActions(TextButton playBtn, TextButton settingsBtn, TextButton exitBtn) {
        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "PLAY button clicked");
                sceneManager.setScene(new UsernameScene(sceneManager, viewport));
            }
        });

        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "SETTINGS button clicked");
                if (volumeTable != null) {
                    volumeTable.setVisible(!volumeTable.isVisible());
                }
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

    private void addOverlayButtons(TextButton playBtn, TextButton settingsBtn, TextButton exitBtn) {
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        float buttonW = worldW * UI.BUTTON_WIDTH_MULT;
        float buttonH = worldH * UI.BUTTON_HEIGHT_MULT;
        float buttonX = worldW * UI.BUTTON_X_MULT - buttonW / 2f;

        float playY = worldH * UI.PLAY_Y_MULT;
        float settingsY = worldH * UI.SETTINGS_Y_MULT;
        float exitY = worldH * UI.EXIT_Y_MULT;

        playBtn.setSize(buttonW, buttonH);
        settingsBtn.setSize(buttonW, buttonH);
        exitBtn.setSize(buttonW, buttonH);

        playBtn.setPosition(buttonX, playY);
        settingsBtn.setPosition(buttonX, settingsY);
        exitBtn.setPosition(buttonX, exitY);

        stage.addActor(playBtn);
        stage.addActor(settingsBtn);
        stage.addActor(exitBtn);
    }

    private Table buildVolumePanel() {
        Slider.SliderStyle sliderStyle = makeSliderStyle();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        final Slider musicSlider = new Slider(0f, 1f, 0.01f, false, sliderStyle);
        final Slider sfxSlider = new Slider(0f, 1f, 0.01f, false, sliderStyle);

        musicSlider.setValue(sceneManager.getIOManager().getMusicVolume());
        sfxSlider.setValue(sceneManager.getIOManager().getSfxVolume());

        final Label musicLabel = new Label("Music: " + (int) (musicSlider.getValue() * 100) + "%", labelStyle);
        final Label sfxLabel = new Label("SFX: " + (int) (sfxSlider.getValue() * 100) + "%", labelStyle);

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float v = musicSlider.getValue();
                sceneManager.getIOManager().setMusicVolume(v);
                musicLabel.setText("Music: " + (int) (v * 100) + "%");
            }
        });

        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float v = sfxSlider.getValue();
                sceneManager.getIOManager().setSfxVolume(v);
                sfxLabel.setText("SFX: " + (int) (v * 100) + "%");
            }
        });

        Table table = new Table();

        volumePanelTex = makeSolidTexture(UI.VOL_PANEL_W, UI.VOL_PANEL_H, UI.VOL_PANEL_COLOR);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(volumePanelTex)));

        table.pad(UI.VOL_PANEL_INNER_PAD);
        table.defaults().padBottom(UI.VOL_ROW_GAP);

        table.add(musicLabel).left();
        table.row();
        table.add(musicSlider).width(UI.SLIDER_W).height(UI.SLIDER_H);

        table.row().padTop(UI.VOL_SECTION_GAP);
        table.add(sfxLabel).left();
        table.row();
        table.add(sfxSlider).width(UI.SLIDER_W).height(UI.SLIDER_H);

        table.pack();

        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        table.setPosition(
                (worldW - table.getWidth()) / 2f,
                worldH * UI.VOL_Y_MULT
        );

        return table;
    }

    private TextButton.TextButtonStyle createInvisibleButtonStyle(BitmapFont font, Texture invisibleTexture) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        TextureRegionDrawable invisibleDrawable = new TextureRegionDrawable(new TextureRegion(invisibleTexture));

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
        batch.begin();
        batch.draw(bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        stage.draw();

        if (UI.DEBUG_HITBOXES && debugRenderer != null) {
            debugRenderer.setProjectionMatrix(viewport.getCamera().combined);
            debugRenderer.begin(ShapeRenderer.ShapeType.Line);
            debugRenderer.setColor(Color.RED);

            for (Actor actor : stage.getActors()) {
                if (actor == volumeTable && !volumeTable.isVisible()) {
                    continue;
                }
                debugRenderer.rect(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
            }

            debugRenderer.end();
        }
    }

    @Override
    public void onExit() {
        if (stage != null) stage.dispose();
        if (bg != null) bg.dispose();
        if (font != null) font.dispose();
        if (transparentTex != null) transparentTex.dispose();
        if (sliderBgTex != null) sliderBgTex.dispose();
        if (sliderKnobTex != null) sliderKnobTex.dispose();
        if (volumePanelTex != null) volumePanelTex.dispose();
        if (debugRenderer != null) debugRenderer.dispose();
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

    private static final class Assets {
        static final String MAIN_MENU_BG = "mainmenu.png";

        private Assets() {}
    }

    private static final class UI {
        static final boolean DEBUG_HITBOXES = false;

        static final float BUTTON_WIDTH_MULT = 0.255f;
        static final float BUTTON_HEIGHT_MULT = 0.105f;
        static final float BUTTON_X_MULT = 0.680f;

        static final float PLAY_Y_MULT = 0.380f;
        static final float SETTINGS_Y_MULT = 0.235f;
        static final float EXIT_Y_MULT = 0.1f;

        static final int VOL_PANEL_W = 600;
        static final int VOL_PANEL_H = 200;
        static final Color VOL_PANEL_COLOR = new Color(0f, 0f, 0f, 0.45f);
        static final float VOL_PANEL_INNER_PAD = 18f;
        static final float VOL_ROW_GAP = 12f;
        static final float VOL_SECTION_GAP = 14f;

        static final float SLIDER_W = 420f;
        static final float SLIDER_H = 26f;

        static final int SLIDER_BG_W = 300;
        static final int SLIDER_BG_H = 10;
        static final Color SLIDER_BG_COLOR = new Color(1f, 1f, 1f, 0.35f);

        static final int SLIDER_KNOB_W = 18;
        static final int SLIDER_KNOB_H = 26;
        static final Color SLIDER_KNOB_COLOR = new Color(1f, 1f, 1f, 0.95f);

        static final float VOL_Y_MULT = 0.60f;

        private UI() {}
    }
}