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
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private ShapeRenderer debugRenderer;

    private BitmapFont buttonFont;
    private BitmapFont titleFont;
    private BitmapFont scoreFont;
    private BitmapFont podiumFont;

    private Texture invisibleTex;

    private final GlyphLayout layout = new GlyphLayout();

    public EndScene(SceneManager sceneManager, Viewport viewport, StatisticsManager statsManager) {
        super(sceneManager);
        this.viewport = viewport;
        this.statsManager = statsManager;
    }

    @Override
    public void onEnter() {
        bg = new Texture("endscene.png");
        sceneManager.getIOManager().playMusic(AssetManager.MUSIC_END_SCENE, true);

        stage = new Stage(viewport);
        shapeRenderer = new ShapeRenderer();
        debugRenderer = new ShapeRenderer();

        titleFont = new BitmapFont();
        titleFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        titleFont.setUseIntegerPositions(false);
        titleFont.getData().setScale(1.7f);
        titleFont.setColor(Color.BLACK);

        scoreFont = new BitmapFont();
        scoreFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        scoreFont.setUseIntegerPositions(false);
        scoreFont.getData().setScale(1.45f);
        scoreFont.setColor(Color.WHITE);

        podiumFont = new BitmapFont();
        podiumFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        podiumFont.setUseIntegerPositions(false);
        podiumFont.getData().setScale(1.25f);
        podiumFont.setColor(Color.WHITE);

        buttonFont = new BitmapFont();
        invisibleTex = makeSolidTexture(2, 2, new Color(1f, 1f, 1f, 0f));

        TextButton.TextButtonStyle invisibleStyle = createInvisibleButtonStyle();

        TextButton restartBtn = new TextButton("", invisibleStyle);
        TextButton menuBtn = new TextButton("", invisibleStyle);

        addOverlayButtons(restartBtn, menuBtn);
        wireButtonActions(restartBtn, menuBtn);

        stage.addActor(restartBtn);
        stage.addActor(menuBtn);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
    }

    private TextButton.TextButtonStyle createInvisibleButtonStyle() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        TextureRegionDrawable invisibleDrawable = new TextureRegionDrawable(invisibleTex);
        style.font = buttonFont;
        style.fontColor = new Color(1f, 1f, 1f, 0f);
        style.up = invisibleDrawable;
        style.down = invisibleDrawable;
        style.over = invisibleDrawable;
        return style;
    }

    private void addOverlayButtons(TextButton restartBtn, TextButton menuBtn) {
        float w = viewport.getWorldWidth();
        float h = viewport.getWorldHeight();

        float buttonW = w * UI.BUTTON_WIDTH_MULT;
        float buttonH = h * UI.BUTTON_HEIGHT_MULT;

        float restartX = w * UI.RESTART_X_MULT - buttonW / 2f;
        float restartY = h * UI.RESTART_Y_MULT;

        float menuX = w * UI.MENU_X_MULT - buttonW / 2f;
        float menuY = h * UI.MENU_Y_MULT;

        restartBtn.setSize(buttonW, buttonH);
        menuBtn.setSize(buttonW, buttonH);

        restartBtn.setPosition(restartX, restartY);
        menuBtn.setPosition(menuX, menuY);
    }

    private void wireButtonActions(TextButton restartBtn, TextButton menuBtn) {
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
                sceneManager.setScene(new MainMenuScene(sceneManager, viewport));
            }
        });
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

        batch.begin();
        batch.draw(bg, 0, 0, w, h);
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float panelW = w * 0.54f;
        float panelH = h * 0.48f;
        float panelX = (w - panelW) / 2f;
        float panelY = h * 0.17f;

        shapeRenderer.setColor(0f, 0f, 0f, 0.18f);
        shapeRenderer.rect(panelX, panelY, panelW, panelH);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();

        int finalScore = statsManager.getScore();
        int stars = statsManager.calculateStarRating();
        List<Integer> highScores = statsManager.getPodiumScores();

        String scoreText = "Final Score: " + finalScore;
        String masteryText = "Mastery: " + stars + " / 3 Stars";
        String hsTitle = "Top Scores";

        layout.setText(scoreFont, scoreText);
        float scoreX = (w - layout.width) / 2f;
        float scoreY = h * 0.75f;
        drawTextWithShadow(batch, scoreFont, scoreText, scoreX, scoreY);

        layout.setText(scoreFont, masteryText);
        float masteryX = (w - layout.width) / 2f;
        float masteryY = h * 0.70f;
        drawTextWithShadow(batch, scoreFont, masteryText, masteryX, masteryY);

        layout.setText(titleFont, hsTitle);
        float hsTitleX = (w - layout.width) / 2f;
        float hsTitleY = h * 0.63f;
        drawDarkTextWithShadow(batch, titleFont, hsTitle, hsTitleX, hsTitleY);

        float lineY = h * 0.56f;
        for (int i = 0; i < highScores.size(); i++) {
            String line = (i + 1) + ". " + highScores.get(i) + " pts";
            layout.setText(podiumFont, line);
            float lineX = (w - layout.width) / 2f;
            drawTextWithShadow(batch, podiumFont, line, lineX, lineY - (i * 42f));
        }

        batch.end();

        stage.draw();

        if (UI.DEBUG_HITBOXES && debugRenderer != null) {
            debugRenderer.setProjectionMatrix(viewport.getCamera().combined);
            debugRenderer.begin(ShapeRenderer.ShapeType.Line);
            debugRenderer.setColor(Color.RED);

            for (Actor actor : stage.getActors()) {
                debugRenderer.rect(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
            }

            debugRenderer.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void onExit() {
        if (stage != null) stage.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (debugRenderer != null) debugRenderer.dispose();

        if (bg != null) bg.dispose();

        if (buttonFont != null) buttonFont.dispose();
        if (titleFont != null) titleFont.dispose();
        if (scoreFont != null) scoreFont.dispose();
        if (podiumFont != null) podiumFont.dispose();

        if (invisibleTex != null) invisibleTex.dispose();

        sceneManager.getIOManager().stopMusic();
    }

    private void drawTextWithShadow(SpriteBatch batch, BitmapFont f, String text, float x, float y) {
        Color original = new Color(f.getColor());

        f.setColor(0f, 0f, 0f, 0.85f);
        f.draw(batch, text, x + 2f, y - 2f);

        f.setColor(original);
        f.draw(batch, text, x, y);
    }

    private void drawDarkTextWithShadow(SpriteBatch batch, BitmapFont f, String text, float x, float y) {
        Color original = new Color(f.getColor());

        f.setColor(1f, 1f, 1f, 0.35f);
        f.draw(batch, text, x + 2f, y - 2f);

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

    private static final class UI {
        static final boolean DEBUG_HITBOXES = false;

        static final float BUTTON_WIDTH_MULT = 0.24f;
        static final float BUTTON_HEIGHT_MULT = 0.10f;

        static final float RESTART_X_MULT = 0.38f;
        static final float RESTART_Y_MULT = 0.03f;

        static final float MENU_X_MULT = 0.65f;
        static final float MENU_Y_MULT = 0.03f;

        private UI() {}
    }
}