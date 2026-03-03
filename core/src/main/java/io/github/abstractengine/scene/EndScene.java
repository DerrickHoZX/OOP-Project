package io.github.abstractengine.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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
import io.github.abstractengine.managers.AssetManager;
import io.github.abstractengine.managers.StatisticsManager; // ADDED

import java.util.List; // ADDED

public class EndScene extends Scene {

    private final Viewport viewport;

    private Texture bg;
    private Stage stage;

    private BitmapFont font; // For buttons
    private BitmapFont textFont; // For Score/Mastery
    private BitmapFont podiumFont; // For Arcade Podium
    private Texture buttonTex;
    
    private StatisticsManager statsManager; // ADDED

    // UPDATED CONSTRUCTOR: Now requires the StatisticsManager
    public EndScene(SceneManager sceneManager, Viewport viewport, StatisticsManager statsManager) {
        super(sceneManager);
        this.viewport = viewport;
        this.statsManager = statsManager;
    }

    @Override
    public void onEnter() {

        bg = new Texture("endscene.jpg"); // must be inside assets root
        sceneManager.getIOManager().playMusic(AssetManager.MUSIC_END_SCENE, true);

        stage = new Stage(viewport);

        // --- SETUP FONTS FOR STATS ---
        textFont = new BitmapFont();
        textFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        textFont.setUseIntegerPositions(false);
        textFont.getData().setScale(2.5f);
        textFont.setColor(Color.WHITE);

        podiumFont = new BitmapFont();
        podiumFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        podiumFont.setUseIntegerPositions(false);
        podiumFont.getData().setScale(1.5f);
        podiumFont.setColor(Color.CYAN);

        // --- SETUP BUTTON UI ---
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
            	sceneManager.getIOManager().getLogging().info(LogCategory.UI, "RESTART button clicked");
            	sceneManager.setScene(new StartScene(sceneManager, viewport));
            }
        });

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	sceneManager.getIOManager().getLogging().info(LogCategory.UI, "MAIN MENU button clicked");
            	// NOTE: Ensure MainMenuScene doesn't require extra parameters in its constructor!
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
        // Clear screen to prevent UI smearing
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        
        // Draw Background
        batch.draw(bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // --- DRAW STATS ON TOP OF BACKGROUND ---
        float centerX = viewport.getWorldWidth() / 2f;
        float topY = viewport.getWorldHeight() - 100f;

        // Draw Score & Mastery
        int finalScore = statsManager.getScore();
        int stars = statsManager.calculateStarRating();
        textFont.draw(batch, "Final Score: " + finalScore, centerX - 180f, topY);
        textFont.draw(batch, "Mastery: " + stars + " / 3 Stars", centerX - 210f, topY - 70f);

        // Draw Arcade Podium
        textFont.draw(batch, "--- HIGH SCORES ---", centerX - 220f, topY - 180f);
        List<Integer> highScores = statsManager.getPodiumScores();
        float podiumY = topY - 250f;
        
        for (int i = 0; i < highScores.size(); i++) {
            String rank = (i + 1) + ". ";
            podiumFont.draw(batch, rank + highScores.get(i) + " pts", centerX - 90f, podiumY - (i * 45f));
        }

        batch.end();

        // Draw the UI Stage last so buttons appear on top of everything
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
        if (textFont != null) textFont.dispose(); // ADDED
        if (podiumFont != null) podiumFont.dispose(); // ADDED
        if (buttonTex != null) buttonTex.dispose();
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
}