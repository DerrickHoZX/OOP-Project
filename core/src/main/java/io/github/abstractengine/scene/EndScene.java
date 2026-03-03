package io.github.abstractengine.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.AssetManager;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.managers.StatisticsManager;

import java.util.List;

public class EndScene extends Scene {

    private final Viewport viewport;
    private final StatisticsManager statsManager;

    private Texture bg;
    private Stage stage;

    private ShapeRenderer shapeRenderer;

    private BitmapFont buttonFont;     // for buttons
    private BitmapFont titleFont;      // "Final Score / Mastery / High Scores"
    private BitmapFont scoreFont;      // numbers / mastery line
    private BitmapFont podiumFont;     // podium lines

    private Texture buttonTex;

    private final GlyphLayout layout = new GlyphLayout();

    public EndScene(SceneManager sceneManager, Viewport viewport, StatisticsManager statsManager) {
        super(sceneManager);
        this.viewport = viewport;
        this.statsManager = statsManager;
    }

    @Override
    public void onEnter() {
        bg = new Texture("endscene.jpg");
        sceneManager.getIOManager().playMusic(AssetManager.MUSIC_END_SCENE, true);

        stage = new Stage(viewport);
        shapeRenderer = new ShapeRenderer();

        // ---- Fonts ----
        titleFont = new BitmapFont();
        titleFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        titleFont.setUseIntegerPositions(false);
        titleFont.getData().setScale(2.0f);
        titleFont.setColor(Color.WHITE);

        scoreFont = new BitmapFont();
        scoreFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        scoreFont.setUseIntegerPositions(false);
        scoreFont.getData().setScale(2.1f);
        scoreFont.setColor(Color.WHITE);

        podiumFont = new BitmapFont();
        podiumFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        podiumFont.setUseIntegerPositions(false);
        podiumFont.getData().setScale(1.7f);
        podiumFont.setColor(Color.CYAN);

        // ---- Buttons (keep your same style) ----
        buttonFont = new BitmapFont();
        buttonTex = makeSolidTexture(1, 1, new Color(0.15f, 0.15f, 0.15f, 1f));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = buttonFont;
        style.fontColor = Color.WHITE;
        style.up = new TextureRegionDrawable(buttonTex);
        style.down = new TextureRegionDrawable(buttonTex);
        style.over = new TextureRegionDrawable(buttonTex);

        TextButton restartBtn = new TextButton("RESTART", style);
        TextButton menuBtn = new TextButton("MAIN MENU", style);

        restartBtn.setSize(320, 60);
        menuBtn.setSize(320, 60);

        float w = viewport.getWorldWidth();
        float h = viewport.getWorldHeight();

        restartBtn.setPosition((w - restartBtn.getWidth()) / 2f, h * 0.22f);
        menuBtn.setPosition((w - menuBtn.getWidth()) / 2f, h * 0.12f);

        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "RESTART button clicked");
                sceneManager.setScene(new StartScene(sceneManager, viewport)); // defaults to grammar
            }
        });

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "MAIN MENU button clicked");
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = viewport.getWorldWidth();
        float h = viewport.getWorldHeight();

        batch.setProjectionMatrix(viewport.getCamera().combined);

        // ---- Background ----
        batch.begin();
        batch.draw(bg, 0, 0, w, h);
        batch.end();

        // ---- Panels (semi-transparent, NOT full black) ----
        // Enable blending so alpha works properly
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Top stats panel (moved higher up; small height so it doesn't cover "QUIZ ENDED!")
        float topPanelW = 520f;
        float topPanelH = 105f;
        float topPanelX = (w - topPanelW) / 2f;
        float topPanelY = h - topPanelH - 20f; // <-- higher up
        shapeRenderer.setColor(0f, 0f, 0f, 0.35f);
        shapeRenderer.rect(topPanelX, topPanelY, topPanelW, topPanelH);

        // High scores panel (bigger, centered mid; softer opacity)
        float hsPanelW = 560f;
        float hsPanelH = 230f;
        float hsPanelX = (w - hsPanelW) / 2f;
        float hsPanelY = h * 0.42f; // keeps it below the sign area, above buttons
        shapeRenderer.setColor(0f, 0f, 0f, 0.30f);
        shapeRenderer.rect(hsPanelX, hsPanelY, hsPanelW, hsPanelH);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // ---- Text ----
        batch.begin();

        int finalScore = statsManager.getScore();
        int stars = statsManager.calculateStarRating();

        // Final score + mastery (centered inside top panel)
        String scoreText = "Final Score: " + finalScore;
        String masteryText = "Mastery: " + stars + " / 3 Stars";

        layout.setText(scoreFont, scoreText);
        float scoreX = (w - layout.width) / 2f;
        float scoreY = topPanelY + topPanelH - 28f;

        layout.setText(scoreFont, masteryText);
        float masteryX = (w - layout.width) / 2f;
        float masteryY = topPanelY + topPanelH - 74f;

        drawTextWithShadow(batch, scoreFont, scoreText, scoreX, scoreY);
        drawTextWithShadow(batch, scoreFont, masteryText, masteryX, masteryY);

        // High scores title
        String hsTitle = "--- HIGH SCORES ---";
        layout.setText(titleFont, hsTitle);
        float hsTitleX = (w - layout.width) / 2f;
        float hsTitleY = hsPanelY + hsPanelH - 35f;
        drawTextWithShadow(batch, titleFont, hsTitle, hsTitleX, hsTitleY);

        // Podium lines (centered)
        List<Integer> highScores = statsManager.getPodiumScores();
        float lineY = hsTitleY - 60f;

        for (int i = 0; i < highScores.size(); i++) {
            String line = (i + 1) + ". " + highScores.get(i) + " pts";
            layout.setText(podiumFont, line);
            float lineX = (w - layout.width) / 2f;
            drawTextWithShadow(batch, podiumFont, line, lineX, lineY - (i * 52f));
        }

        batch.end();

        // ---- Buttons on top ----
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) stage.getViewport().update(width, height, true);
    }

    @Override
    public void onExit() {
        if (stage != null) stage.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();

        if (bg != null) bg.dispose();

        if (buttonFont != null) buttonFont.dispose();
        if (titleFont != null) titleFont.dispose();
        if (scoreFont != null) scoreFont.dispose();
        if (podiumFont != null) podiumFont.dispose();

        if (buttonTex != null) buttonTex.dispose();

        sceneManager.getIOManager().stopMusic();
    }

    private void drawTextWithShadow(SpriteBatch batch, BitmapFont f, String text, float x, float y) {
        Color original = new Color(f.getColor());

        // shadow
        f.setColor(0f, 0f, 0f, 0.85f);
        f.draw(batch, text, x + 2f, y - 2f);

        // main
        f.setColor(original);
        f.draw(batch, text, x, y);
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