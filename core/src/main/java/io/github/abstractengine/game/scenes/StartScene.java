package io.github.abstractengine.game.scenes;

import java.util.List;

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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.abstractengine.collision.BasicCollisionDetector;
import io.github.abstractengine.collision.Boundary;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.game.AlgorithmManager;
import io.github.abstractengine.game.GameAssets;
import io.github.abstractengine.game.StatisticsManager;
import io.github.abstractengine.game.collision.SimulationCollisionHandler;
import io.github.abstractengine.game.effects.PointsFeedbackEffect;
import io.github.abstractengine.game.effects.ScreenFlash;
import io.github.abstractengine.game.effects.StreakPowerUpRuntime;
import io.github.abstractengine.game.entities.Circle;
import io.github.abstractengine.game.entities.PowerUpPickup;
import io.github.abstractengine.game.entities.Triangle;
import io.github.abstractengine.interfaces.GameEventListener;
import io.github.abstractengine.io.KeyCode;
import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.CollisionManager;
import io.github.abstractengine.managers.EntityManager;
import io.github.abstractengine.managers.MovementManager;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.movement.KeyboardMovement;
import io.github.abstractengine.movement.RandomMovement;
import io.github.abstractengine.scene.Scene;

/**
 * Main gameplay scene. Coordinates game loop, delegates HUD rendering
 * to HUDRenderer and entity spawning to EntitySpawner.
 */

public class StartScene extends Scene implements GameEventListener {

    private static final float PLAYER_BASE_MOVE_SPEED = 300f;
    private static final float ENEMY_PRETURN_PAUSE_START = 0.5f;
    private static final float ENEMY_PRETURN_PAUSE_END = 0.12f;

    private final Viewport viewport;
    private final GameCategory category;
    private final CategoryConfig config;

    private Texture bg;
    private CollisionManager collisionManager;
    private Circle circle;
    private EntityManager entityManager;
    private MovementManager movementManager;
    private StatisticsManager statsManager;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private BitmapFont questionFont;

    private ScreenFlash screenFlash;
    private PointsFeedbackEffect pointsFeedback;
    private StreakPowerUpRuntime streakPowerUpRuntime;

    private final GlyphLayout layout = new GlyphLayout();

    private String currentQuestionPrompt = "";
    private QuestionBank.Question currentQuestion;
    private final AlgorithmManager algorithmManager;

    // Delegated helpers
    private HUDRenderer hudRenderer;
    private EntitySpawner entitySpawner;

    private Stage stage;
    private Texture pauseButtonTex;

    public StartScene(SceneManager sceneManager, Viewport viewport) {
        this(sceneManager, viewport, GameCategory.GRAMMAR, "Player");
    }

    public StartScene(SceneManager sceneManager, Viewport viewport, GameCategory category) {
        this(sceneManager, viewport, category, "Player");
    }

    public StartScene(SceneManager sceneManager, Viewport viewport, GameCategory category, String username) {
        super(sceneManager);
        this.viewport = viewport;
        this.category = category;
        this.config = CategoryConfigFactory.get(category);
        this.entityManager = new EntityManager();
        this.movementManager = new MovementManager();
        String trimmedUsername = (username != null ? username.trim() : "");
        if (trimmedUsername.isEmpty()) {
            trimmedUsername = sceneManager.getSessionUsername().trim();
        }
        String displayUsername = trimmedUsername.isEmpty() ? "Player" : trimmedUsername;
        this.statsManager = new StatisticsManager(
                category,
                displayUsername,
                sceneManager.getIOManager(),
                60f
        );
        this.algorithmManager = new AlgorithmManager(config.questions);
    }

    @Override
    public void onEnter() {
        shapeRenderer = new ShapeRenderer();
        screenFlash = new ScreenFlash();
        pointsFeedback = new PointsFeedbackEffect();
        streakPowerUpRuntime = new StreakPowerUpRuntime();
        streakPowerUpRuntime.reset();

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.getData().setScale(1.5f);

        questionFont = new BitmapFont();
        questionFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        questionFont.setUseIntegerPositions(false);
        questionFont.getData().setScale(2.0f);

        // Initialize HUD renderer
        hudRenderer = new HUDRenderer(font, questionFont, layout);

        bg = new Texture(config.backgroundPath);
        sceneManager.getIOManager().playMusic(GameAssets.MUSIC_START_SCENE, true);

        // Create player
        float circleSize = 60f;
        circle = new Circle(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, circleSize, circleSize);
        circle.setMovementComponent(new KeyboardMovement(
                sceneManager.getIOManager(),
                PLAYER_BASE_MOVE_SPEED,
                viewport.getWorldWidth(),
                viewport.getWorldHeight(),
                circleSize
        ));
        entityManager.addEntity(circle);
        movementManager.register(circle);

        // Initialize entity spawner (needs player reference)
        entitySpawner = new EntitySpawner(viewport, entityManager, movementManager, circle, font);

        // Spawn initial enemies
        for (int i = 0; i < 3; i++) {
            entitySpawner.spawnEnemy();
        }

        // Set up collision system
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();
        Boundary boundary = new Boundary(0f, worldW, 0f, worldH - 180f);
        BasicCollisionDetector detector = new BasicCollisionDetector();
        SimulationCollisionHandler handler = new SimulationCollisionHandler(
                sceneManager, entityManager, viewport, circle, statsManager, this
        );
        collisionManager = new CollisionManager(boundary, entityManager, detector, handler);

        // Spawn first question
        spawnNextQuestion();
        createPauseButton();
    }

    private void createPauseButton() {
        stage = new Stage(viewport);
        pauseButtonTex = makeSolidTexture(1, 1, new Color(1f, 1f, 1f, 1f));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.BLACK;
        style.up = new TextureRegionDrawable(pauseButtonTex);
        style.down = new TextureRegionDrawable(pauseButtonTex);
        style.over = new TextureRegionDrawable(pauseButtonTex);

        TextButton pauseBtn = new TextButton("PAUSE", style);
        pauseBtn.setSize(120, 50);
        float btnX = viewport.getWorldWidth() - pauseBtn.getWidth() - 15f;
        float btnY = viewport.getWorldHeight() - pauseBtn.getHeight() - 15f;
        pauseBtn.setPosition(btnX, btnY);

        pauseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.getIOManager().getLogging().info(LogCategory.UI, "PAUSE button clicked");
                sceneManager.pushScene(new PauseScene(sceneManager, viewport, StartScene.this, statsManager));
            }
        });

        stage.addActor(pauseBtn);
        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
    }

    private Texture makeSolidTexture(int w, int h, Color c) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(c);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    // ---------------------------
    // GameEventListener implementation
    // ---------------------------

    @Override
    public void onCorrectAnswer(int pointsGained) {
        if (pointsFeedback != null && pointsGained > 0) {
            pointsFeedback.showGain(pointsGained);
        }
        if (screenFlash != null) {
            screenFlash.flashGreen();
        }
    }

    @Override
    public void onWrongAnswer(int pointsLost) {
        if (pointsFeedback != null && pointsLost > 0) {
            pointsFeedback.showLoss(pointsLost);
        }
        if (screenFlash != null) {
            screenFlash.flashRed();
        }
    }

    @Override
    public void onEnemyHit(int pointsLost) {
        if (pointsFeedback != null && pointsLost > 0) {
            pointsFeedback.showLoss(pointsLost);
        }
        if (screenFlash != null) {
            screenFlash.flashRed();
        }
    }

    @Override
    public void onAnswerSubmitted(boolean wasCorrect) {
        if (currentQuestion != null) {
            algorithmManager.recordAnswer(currentQuestion, wasCorrect);
        }
        spawnNextQuestion();
    }

    @Override
    public void onEnemyDestroyed() {
        entitySpawner.spawnEnemy();
    }

    @Override
    public void onItemCollected(Entity item) {
        if (item instanceof PowerUpPickup) {
            PowerUpPickup pickup = (PowerUpPickup) item;
            if (pickup.isActive() && streakPowerUpRuntime != null) {
                streakPowerUpRuntime.activate(pickup.getPowerUpType());
                entitySpawner.removePowerUpPickup(pickup);
                pickup.disposeTexture();
                pickup.destroy();
                entityManager.removeEntity(pickup);
            }
        }
    }

    // ---------------------------
    // GAME LOGIC
    // ---------------------------

    private void spawnNextQuestion() {
        currentQuestion = entitySpawner.spawnNextQuestion(algorithmManager, config.questions);
        currentQuestionPrompt = currentQuestion.prompt;
    }

    private float getEnemyPreTurnPauseSeconds() {
        float total = statsManager.getMatchDurationSeconds();
        if (total <= 0.01f) {
            return ENEMY_PRETURN_PAUSE_START;
        }
        float t = MathUtils.clamp(statsManager.getRoundElapsedSeconds() / total, 0f, 1f);
        return MathUtils.lerp(ENEMY_PRETURN_PAUSE_START, ENEMY_PRETURN_PAUSE_END, t);
    }

    private void applyActivePowerUpMovement() {
        if (circle == null || streakPowerUpRuntime == null) {
            return;
        }
        KeyboardMovement km = (KeyboardMovement) circle.getMovementComponent();
        if (km != null) {
            km.setSpeed(PLAYER_BASE_MOVE_SPEED * streakPowerUpRuntime.getPlayerSpeedMultiplier());
        }

        float emult = streakPowerUpRuntime.getEnemySpeedMultiplier();
        boolean frozen = streakPowerUpRuntime.enemiesFrozen();
        float preTurnPause = getEnemyPreTurnPauseSeconds();
        for (Entity e : entityManager.getEntitiesSnapshot()) {
            if (e instanceof Triangle) {
                RandomMovement rm = (RandomMovement) ((Triangle) e).getMovementComponent();
                if (rm != null) {
                    rm.setEnemyModifiers(emult, frozen);
                    rm.setPreTurnPauseSeconds(preTurnPause);
                }
            }
        }
    }

    // ---------------------------
    // UPDATE / RENDER
    // ---------------------------

    @Override
    public void update(float dt) {
        if (sceneManager.getIOManager().isKeyJustPressed(KeyCode.ESCAPE)) {
            sceneManager.pushScene(new PauseScene(sceneManager, viewport, this, statsManager));
            return;
        }

        if (stage != null) {
            stage.act(dt);
        }

        screenFlash.update(dt);
        if (pointsFeedback != null) {
            pointsFeedback.update(dt);
        }

        statsManager.update(dt);
        if (statsManager.isTimeUp()) {
            sceneManager.setScene(new EndScene(sceneManager, viewport, statsManager));
            return;
        }

        if (streakPowerUpRuntime != null) {
            streakPowerUpRuntime.update(dt);
        }
        applyActivePowerUpMovement();

        movementManager.update(dt);
        entityManager.update(dt);
        collisionManager.update(dt);

        entitySpawner.handleStreakPowerUpSpawns(statsManager.getCurrentStreak());
    }

    public void onResume() {
        if (stage != null) {
            Gdx.input.setInputProcessor(new InputMultiplexer(stage));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        // Draw background + entities
        batch.begin();
        if (bg != null) batch.draw(bg, 0, 0, worldW, worldH);
        entityManager.render(batch, shapeRenderer);
        batch.end();

        // Draw HUD panels
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        hudRenderer.drawPanels(shapeRenderer, worldW, worldH, streakPowerUpRuntime);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Draw HUD text
        batch.begin();
        hudRenderer.drawText(batch, worldW, worldH, statsManager, currentQuestionPrompt, streakPowerUpRuntime);
        batch.end();

        // Draw pause button
        if (stage != null) {
            stage.draw();
        }

        // Draw screen flash overlay
        if (screenFlash.isFlashing()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(screenFlash.getCurrentColor());
            shapeRenderer.rect(0, 0, worldW, worldH);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        // Draw points feedback
        if (pointsFeedback != null && pointsFeedback.isActive()) {
            batch.begin();
            batch.setProjectionMatrix(viewport.getCamera().combined);
            hudRenderer.drawPointsFeedback(batch, worldW, worldH,
                    pointsFeedback.getText(), pointsFeedback.getTint());
            batch.end();
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
        entitySpawner.clearPowerUpPickups();
        if (bg != null) bg.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (font != null) font.dispose();
        if (questionFont != null) questionFont.dispose();
        if (pauseButtonTex != null) pauseButtonTex.dispose();
        if (stage != null) stage.dispose();
        movementManager.clear();
        if (collisionManager != null) collisionManager.clear();
        sceneManager.getIOManager().stopMusic();
    }

    // ---------------------------
    // CONFIGURATION
    // ---------------------------

    static final class CategoryConfig {
        final String backgroundPath;
        final List<QuestionBank.Question> questions;

        CategoryConfig(String backgroundPath, List<QuestionBank.Question> questions) {
            this.backgroundPath = backgroundPath;
            this.questions = questions;
        }
    }

    static final class CategoryConfigFactory {
        static CategoryConfig get(GameCategory category) {
            if (category == GameCategory.CATEGORIZATION) {
                return new CategoryConfig(
                    "GameMode_Categorization.png",
                    QuestionBank.getCategoryQuestions()
                );
            } else {
                return new CategoryConfig(
                    "GameMode_Grammar.png",
                    QuestionBank.getAllLanguageQuestions()
                );
            }
        }
    }
}