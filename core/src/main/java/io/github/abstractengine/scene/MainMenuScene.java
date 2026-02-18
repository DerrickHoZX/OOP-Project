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

    public MainMenuScene(SceneManager sceneManager, Viewport viewport) {
        super(sceneManager);
        this.viewport = viewport;
    }

    @Override
    public void onEnter() {
        bg = new Texture("mainmenu.jpeg");
        sceneManager.getIOManager().playMusic(AssetManager.MUSIC_MAIN_MENU, true);

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

        // ----- Volume UI (Music + SFX sliders) -----
        Slider.SliderStyle sliderStyle = makeSliderStyle();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        final Slider musicSlider = new Slider(0f, 1f, 0.01f, false, sliderStyle);
        final Slider sfxSlider   = new Slider(0f, 1f, 0.01f, false, sliderStyle);

        // Load saved values (IOManager loads from file in its constructor)
        musicSlider.setValue(sceneManager.getIOManager().getMusicVolume());
        sfxSlider.setValue(sceneManager.getIOManager().getSfxVolume());

        final Label musicLabel = new Label("Music: " + (int)(musicSlider.getValue() * 100) + "%", labelStyle);
        final Label sfxLabel   = new Label("SFX: " + (int)(sfxSlider.getValue() * 100) + "%", labelStyle);

        musicSlider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                float v = musicSlider.getValue();
                sceneManager.getIOManager().setMusicVolume(v); // saves to file
                musicLabel.setText("Music: " + (int)(v * 100) + "%");
            }
        });

        sfxSlider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                float v = sfxSlider.getValue();
                sceneManager.getIOManager().setSfxVolume(v); // saves to file
                sfxLabel.setText("SFX: " + (int)(v * 100) + "%");
            }
        });

        Table volumeTable = new Table();
        volumeTable.setFillParent(true);
        volumeTable.center();

        // Put sliders a bit above the buttons
        volumeTable.padBottom(worldH * 0.18f);

        volumeTable.add(musicLabel).left().padBottom(8);
        volumeTable.row();
        volumeTable.add(musicSlider).width(320).height(20).padBottom(18);
        volumeTable.row();
        volumeTable.add(sfxLabel).left().padBottom(8);
        volumeTable.row();
        volumeTable.add(sfxSlider).width(320).height(20);

        stage.addActor(volumeTable);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
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
        sliderBgTex = makeSolidTexture(300, 10, new Color(1f, 1f, 1f, 0.35f));
        sliderKnobTex = makeSolidTexture(18, 26, new Color(1f, 1f, 1f, 0.95f));

        Slider.SliderStyle ss = new Slider.SliderStyle();
        ss.background = new TextureRegionDrawable(new TextureRegion(sliderBgTex));
        ss.knob = new TextureRegionDrawable(new TextureRegion(sliderKnobTex));
        return ss;
    }
}
