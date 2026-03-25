package io.github.abstractengine.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.entities.PowerUpPickup;
import io.github.abstractengine.entities.PowerUpType;
import io.github.abstractengine.entities.Square;
import io.github.abstractengine.entities.Triangle;
import io.github.abstractengine.io.KeyCode;
import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.*;
import io.github.abstractengine.movement.KeyboardMovement;
import io.github.abstractengine.movement.RandomMovement;
import io.github.abstractengine.collision.*;
import io.github.abstractengine.effects.PointsFeedbackEffect;
import io.github.abstractengine.effects.ScreenFlash;
import io.github.abstractengine.effects.StreakPowerUpRuntime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StartScene extends Scene {

    private static final float PLAYER_BASE_MOVE_SPEED = 300f;
    private static final float ENEMY_BASE_SPEED = 150f;
    /** Seconds enemy drifts before pausing to telegraph a direction change. */
    private static final float ENEMY_MOVE_PHASE_SECONDS = 2f;
    /** Pre-turn stop at round start (easy) → shorter near time-up (harder). */
    private static final float ENEMY_PRETURN_PAUSE_START = 0.5f;
    private static final float ENEMY_PRETURN_PAUSE_END = 0.05f;
    private static final float POWERUP_PICKUP_SIZE = 40f;

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
    
    // NEW: Screen flash effect
    private ScreenFlash screenFlash;
    private PointsFeedbackEffect pointsFeedback;
    private StreakPowerUpRuntime streakPowerUpRuntime;
    private final List<PowerUpPickup> activePowerUpPickups = new ArrayList<>();
    private int prevStreakForPowerUps;

    private final GlyphLayout layout = new GlyphLayout();

    private String currentQuestionPrompt = "";
    private QuestionBank.Question currentQuestion;
    private final List<Square> currentAnswerSquares;
    private final AlgorithmManager algorithmManager;

    // For pause button
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

        // Get configuration based on category
        this.config = CategoryConfigFactory.get(category);

        this.entityManager = new EntityManager();
        this.movementManager = new MovementManager();
        String trimmedUsername = (username != null ? username.trim() : "");
        this.statsManager = new StatisticsManager(
                category,
                trimmedUsername.isEmpty() ? "Player" : trimmedUsername,
                sceneManager.getIOManager(),
                60f
        );
        this.currentAnswerSquares = new ArrayList<>();
        this.algorithmManager = new AlgorithmManager(config.questions);
    }

    @Override
    public void onEnter() {
        shapeRenderer = new ShapeRenderer();
        
        // NEW: Initialize screen flash effect
        screenFlash = new ScreenFlash();
        pointsFeedback = new PointsFeedbackEffect();
        streakPowerUpRuntime = new StreakPowerUpRuntime();
        streakPowerUpRuntime.reset();
        activePowerUpPickups.clear();
        prevStreakForPowerUps = 0;

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.getData().setScale(1.5f);

        questionFont = new BitmapFont();
        questionFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        questionFont.setUseIntegerPositions(false);
        questionFont.getData().setScale(2.0f);

        // Load category-based background
        bg = new Texture(config.backgroundPath);

        sceneManager.getIOManager().playMusic(AssetManager.MUSIC_START_SCENE, true);

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

        for (int i = 0; i < 3; i++) {
            spawnEnemy();
        }

        // Define safe play area boundaries (keep entities below UI)
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();
        
        float playAreaMinX = 0f;
        float playAreaMaxX = worldW;
        float playAreaMinY = 0f;
        float playAreaMaxY = worldH - 180f;  // Keep entities below question panel and pause button
        
        Boundary boundary = new Boundary(playAreaMinX, playAreaMaxX, playAreaMinY, playAreaMaxY);
        BasicCollisionDetector detector = new BasicCollisionDetector();
        SimulationCollisionHandler handler = new SimulationCollisionHandler(
                sceneManager,
                entityManager,
                viewport,
                circle,
                statsManager,
                this
        );
        collisionManager = new CollisionManager(boundary, entityManager, detector, handler);

        spawnNextQuestion();
        createPauseButton();
    }

    private void createPauseButton() {
        stage = new Stage(viewport);

        // Create button texture (white)
        pauseButtonTex = makeSolidTexture(1, 1, new Color(1f, 1f, 1f, 1f));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.BLACK;  // dark text on white button
        style.up = new TextureRegionDrawable(pauseButtonTex);
        style.down = new TextureRegionDrawable(pauseButtonTex);
        style.over = new TextureRegionDrawable(pauseButtonTex);

        TextButton pauseBtn = new TextButton("PAUSE", style);
        pauseBtn.setSize(120, 50);

        // Position: Top right corner
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

    public void spawnEnemy() {
        float tWidth = 70f;
        float tHeight = 70f;
        
        // Define safe spawn area (avoid UI areas)
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();
        
        float safeMinX = 250f;  // Avoid left score panel
        float safeMaxX = worldW - tWidth - 20f;
        float safeMinY = 20f;
        float safeMaxY = worldH - 200f;  // Avoid top UI (question, timer, pause button)
        
        float randomX = MathUtils.random(safeMinX, safeMaxX);
        float randomY = MathUtils.random(safeMinY, safeMaxY);

        Triangle triangle = new Triangle(randomX, randomY, tWidth, tHeight);
        triangle.setMovementComponent(new RandomMovement(
                ENEMY_BASE_SPEED,
                ENEMY_MOVE_PHASE_SECONDS,
                viewport.getWorldWidth(),
                viewport.getWorldHeight(),
                tWidth
        ));

        entityManager.addEntity(triangle);
        movementManager.register(triangle);
    }

    /**
     * Called when the player collides with an answer square.
     * Records the result in AlgorithmManager, then spawns the next question.
     */
    public void onAnswerSubmitted(boolean wasCorrect) {
        if (currentQuestion != null) {
            algorithmManager.recordAnswer(currentQuestion, wasCorrect);
        }
        spawnNextQuestion();
    }

    public void spawnNextQuestion() {
        // Clear old squares
        for (Square s : currentAnswerSquares) {
            entityManager.removeEntity(s);
        }
        currentAnswerSquares.clear();

        // Use AlgorithmManager to avoid repeating correctly-answered questions
        QuestionBank.Question q = algorithmManager.getNextQuestion();
        if (q == null) {
            q = config.questions.get(MathUtils.random(0, config.questions.size() - 1)); // fallback if empty
        }
        currentQuestion = q;
        currentQuestionPrompt = q.prompt;

        // Spawn answers (1 correct + 2 decoys)
        String[] answers = { q.correct, q.decoy1, q.decoy2 };
        boolean[] correctness = { true, false, false };

        for (int i = 0; i < answers.length; i++) {
            spawnAnswerWithOverlapProtection(answers[i], correctness[i]);
        }
    }
    
    // NEW: Public methods for collision handler to trigger flash effects
    public void flashCorrect() {
        if (screenFlash != null) {
            screenFlash.flashGreen();
        }
    }
    
    public void flashWrong() {
        if (screenFlash != null) {
            screenFlash.flashRed();
        }
    }

    /** Large centered message: points earned (green), fade in/out (~1s). */
    public void showPointsGained(int points) {
        if (pointsFeedback != null && points > 0) {
            pointsFeedback.showGain(points);
        }
    }

    /** Large centered message: points lost (red), fade in/out (~1s). */
    public void showPointsLost(int points) {
        if (pointsFeedback != null && points > 0) {
            pointsFeedback.showLoss(points);
        }
    }

    public void onPowerUpCollected(PowerUpPickup pickup) {
        if (pickup == null || !pickup.isActive() || streakPowerUpRuntime == null) {
            return;
        }
        streakPowerUpRuntime.activate(pickup.getPowerUpType());
        activePowerUpPickups.remove(pickup);
        pickup.disposeTexture();
        pickup.destroy();
        entityManager.removeEntity(pickup);
    }

    private void handleStreakPowerUpSpawns(int streakNow) {
        if (streakNow < prevStreakForPowerUps) {
            clearPowerUpPickups();
        }
        if (streakNow > prevStreakForPowerUps) {
            int c = PowerUpType.CHERRY.streakRequired;
            int b = PowerUpType.BANANA.streakRequired;
            int w = PowerUpType.WATERMELON.streakRequired;
            if (prevStreakForPowerUps < c && streakNow >= c) {
                spawnPowerUpPickup(PowerUpType.CHERRY);
            }
            if (prevStreakForPowerUps < b && streakNow >= b) {
                spawnPowerUpPickup(PowerUpType.BANANA);
            }
            if (prevStreakForPowerUps < w && streakNow >= w) {
                spawnPowerUpPickup(PowerUpType.WATERMELON);
            }
        }
        prevStreakForPowerUps = streakNow;
    }

    private void clearPowerUpPickups() {
        for (PowerUpPickup p : new ArrayList<>(activePowerUpPickups)) {
            p.disposeTexture();
            entityManager.removeEntity(p);
        }
        activePowerUpPickups.clear();
    }

    private void spawnPowerUpPickup(PowerUpType type) {
        float size = POWERUP_PICKUP_SIZE;
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();
        float safeMinX = 250f;
        float safeMaxX = worldW - size - 20f;
        float safeMinY = 50f;
        float safeMaxY = worldH - 200f;

        float spawnX = 0f;
        float spawnY = 0f;
        boolean invalid;
        int attempts = 0;

        do {
            invalid = false;
            spawnX = MathUtils.random(safeMinX, safeMaxX);
            spawnY = MathUtils.random(safeMinY, safeMaxY);

            float pcx = circle.getX() + circle.getWidth() / 2f;
            float pcy = circle.getY() + circle.getHeight() / 2f;
            float cx = spawnX + size / 2f;
            float cy = spawnY + size / 2f;
            if (Vector2.dst(cx, cy, pcx, pcy) < 140f) {
                invalid = true;
            }

            if (!invalid) {
                for (Square s : currentAnswerSquares) {
                    if (rectsOverlap(spawnX, spawnY, size, size,
                            s.getX(), s.getY(), s.getWidth(), s.getHeight(), 35f)) {
                        invalid = true;
                        break;
                    }
                }
            }
            if (!invalid) {
                for (PowerUpPickup p : activePowerUpPickups) {
                    if (rectsOverlap(spawnX, spawnY, size, size,
                            p.getX(), p.getY(), p.getWidth(), p.getHeight(), 25f)) {
                        invalid = true;
                        break;
                    }
                }
            }
            attempts++;
        } while (invalid && attempts < 120);

        PowerUpPickup pickup = new PowerUpPickup(spawnX, spawnY, size, type);
        entityManager.addEntity(pickup);
        activePowerUpPickups.add(pickup);
    }

    private static boolean rectsOverlap(float ax, float ay, float aw, float ah,
                                      float bx, float by, float bw, float bh, float pad) {
        return ax < bx + bw + pad
                && ax + aw + pad > bx
                && ay < by + bh + pad
                && ay + ah + pad > by;
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

    private void spawnAnswerWithOverlapProtection(String text, boolean isCorrect) {
        // Dynamic box sizing based on text length
        GlyphLayout tempLayout = new GlyphLayout();
        tempLayout.setText(font, text);
        
        float padding = 20f; // Padding around text
        float sWidth = tempLayout.width + padding * 2;   // Width = text width + padding
        float sHeight = tempLayout.height + padding * 2; // Height = text height + padding
        
        // Minimum size to keep boxes clickable
        sWidth = Math.max(sWidth, 80f);
        sHeight = Math.max(sHeight, 50f);
        
        float spawnX = 0, spawnY = 0;
        boolean invalidPosition;
        int attempts = 0;

        float minDistanceFromPlayer = 150f;
        
        // Define safe spawn area (avoid UI)
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();
        
        float safeMinX = 250f;  // Avoid left score panel
        float safeMaxX = worldW - sWidth - 20f;
        float safeMinY = 50f;
        float safeMaxY = worldH - 200f;  // Avoid top UI

        do {
            invalidPosition = false;

            spawnX = MathUtils.random(safeMinX, safeMaxX);
            spawnY = MathUtils.random(safeMinY, safeMaxY);

            // Check against player
            float playerCenterX = circle.getX() + circle.getWidth() / 2f;
            float playerCenterY = circle.getY() + circle.getHeight() / 2f;
            float distToPlayer = Vector2.dst(
                    spawnX + sWidth / 2f, spawnY + sHeight / 2f,
                    playerCenterX, playerCenterY
            );

            if (distToPlayer < minDistanceFromPlayer) {
                invalidPosition = true;
            }

            // Check against existing squares
            if (!invalidPosition) {
                for (Square other : currentAnswerSquares) {
                    float buffer = 50f;
                    if (spawnX < other.getX() + other.getWidth() + buffer &&
                            spawnX + sWidth + buffer > other.getX() &&
                            spawnY < other.getY() + other.getHeight() + buffer &&
                            spawnY + sHeight + buffer > other.getY()) {
                        invalidPosition = true;
                        break;
                    }
                }
            }

            attempts++;
        } while (invalidPosition && attempts < 100);

        Square square = new Square(spawnX, spawnY, sWidth, sHeight, text, isCorrect);
        entityManager.addEntity(square);
        currentAnswerSquares.add(square);
    }

    @Override
    public void update(float dt) {
        // Keyboard ESC still works
        if (sceneManager.getIOManager().isKeyJustPressed(KeyCode.ESCAPE)) {
            sceneManager.pushScene(new PauseScene(sceneManager, viewport, this, statsManager));
            return;
        }

        // Update stage (for button interactions)
        if (stage != null) {
            stage.act(dt);
        }
        
        // NEW: Update screen flash
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

        handleStreakPowerUpSpawns(statsManager.getCurrentStreak());
    }
    
    // NEW: Called when returning from pause - re-register input
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

        // Draw Background + Entities
        batch.begin();
        if (bg != null) batch.draw(bg, 0, 0, worldW, worldH);  // Draw background image
        entityManager.render(batch, shapeRenderer);
        batch.end();

        // HUD PANELS (transparent)
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Score/Streak panel (styled, positioned below title to avoid overlap)
        drawScoreStreakPanel(shapeRenderer, worldW, worldH);

        // Time panel (top-center) - dark background for visibility
        shapeRenderer.setColor(0.10f, 0.16f, 0.26f, 0.95f);
        shapeRenderer.rect(worldW / 2f - 100f, worldH - 60f, 200f, 45f);
        shapeRenderer.setColor(0.25f, 0.45f, 0.75f, 0.95f);
        shapeRenderer.rect(worldW / 2f - 100f, worldH - 60f + 42f, 200f, 3f);  // accent line

        // Question panel (center-top) - WHITE background for clarity
        shapeRenderer.setColor(1f, 1f, 1f, 0.95f); // White with slight transparency
        shapeRenderer.rect(worldW / 2f - 300f, worldH - 150f, 600f, 55f);

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // TEXT
        batch.begin();

        int seconds = (int) Math.ceil(statsManager.getTimeRemaining());

        font.setColor(Color.WHITE);
        questionFont.setColor(Color.BLACK); // BLACK text on white background

        // Score and Streak (drawn by panel method for consistent positioning)
        drawScoreStreakText(batch, worldH);

        // Time (centered, larger and brighter for visibility)
        font.getData().setScale(1.8f);
        String timeText = "Time: " + seconds + "s";
        layout.setText(font, timeText);
        float timeX = (worldW - layout.width) / 2f;
        font.setColor(1f, 1f, 1f, 1f);
        drawTextWithShadow(batch, font, timeText, timeX, worldH - 32f);
        font.getData().setScale(1.5f);  // restore default scale

        // Question (centered) - BLACK text on WHITE background
        layout.setText(questionFont, currentQuestionPrompt);
        float qX = (worldW - layout.width) / 2f;
        drawBlackTextWithShadow(batch, questionFont, currentQuestionPrompt, qX, worldH - 110f);

        batch.end();

        // Draw pause button
        if (stage != null) {
            stage.draw();
        }
        
        // NEW: Draw screen flash overlay (green/red feedback)
        if (screenFlash.isFlashing()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(screenFlash.getCurrentColor());
            shapeRenderer.rect(0, 0, worldW, worldH); // Full screen overlay
            shapeRenderer.end();
            
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        if (pointsFeedback != null && pointsFeedback.isActive()) {
            batch.begin();
            batch.setProjectionMatrix(viewport.getCamera().combined);

            float prevX = questionFont.getData().scaleX;
            float prevY = questionFont.getData().scaleY;
            questionFont.getData().setScale(2.8f);

            String t = pointsFeedback.getText();
            Color c = pointsFeedback.getTint();
            layout.setText(questionFont, t);
            float fx = (worldW - layout.width) / 2f;
            float fy = worldH * 0.52f;
            drawPointsFeedbackLabel(batch, questionFont, t, fx, fy, c);

            questionFont.getData().setScale(prevX, prevY);
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
        clearPowerUpPickups();

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

    /**
     * Draws the Score/Streak panel background.
     * Customize position, size, and colors here. Set USE_TOP_LEFT = true
     * for top-left (may overlap title on some screens), false for bottom-left.
     */
    private static final boolean SCORE_PANEL_TOP_LEFT = true;  // true = top-left

    private void drawScoreStreakPanel(ShapeRenderer shape, float worldW, float worldH) {
        float padLeft = 20f;
        float padTop = 55f;  // larger = panel sits lower (avoids title overlap)
        float padBottom = 20f;
        float panelW = 185f;
        int buffLines = (streakPowerUpRuntime != null) ? streakPowerUpRuntime.countActiveBuffLines() : 0;
        float basePanelH = 72f;
        float panelH = basePanelH + (buffLines > 0 ? 8f + buffLines * 17f : 0f);

        float x = padLeft;
        float y = SCORE_PANEL_TOP_LEFT
                ? worldH - padTop - panelH
                : padBottom;

        // Main panel - dark semi-transparent with blue tint
        shape.setColor(0.10f, 0.16f, 0.26f, 0.88f);
        shape.rect(x, y, panelW, panelH);

        // Accent line at top of panel
        shape.setColor(0.25f, 0.45f, 0.75f, 0.9f);
        shape.rect(x, y + panelH - 3f, panelW, 3f);

        // Store for text positioning (we use same values in drawScoreStreakText)
        scorePanelX = x;
        scorePanelY = y;
        scorePanelW = panelW;
        scorePanelH = panelH;
    }

    private float scorePanelX, scorePanelY, scorePanelW, scorePanelH;  // used by drawScoreStreakText

    /**
     * Draws Score and Streak text inside the panel. Centered horizontally.
     */
    private void drawScoreStreakText(SpriteBatch batch, float worldH) {
        float top = scorePanelY + scorePanelH;
        float scoreY = top - 14f;
        float streakY = top - 36f;

        String scoreText = "Score: " + statsManager.getScore();
        String streakText = "Streak: " + statsManager.getCurrentStreak();

        layout.setText(font, scoreText);
        float scoreX = scorePanelX + (scorePanelW - layout.width) / 2f;
        drawScorePanelText(batch, scoreText, scoreX, scoreY);

        layout.setText(font, streakText);
        float streakX = scorePanelX + (scorePanelW - layout.width) / 2f;
        drawScorePanelText(batch, streakText, streakX, streakY);

        drawPowerUpBuffTimers(batch);
    }

    /** One line per active streak buff, bottom of the score panel (color-coded). */
    private void drawPowerUpBuffTimers(SpriteBatch batch) {
        if (streakPowerUpRuntime == null || streakPowerUpRuntime.countActiveBuffLines() == 0) {
            return;
        }

        float savedScale = font.getData().scaleX;
        font.getData().setScale(1.05f);

        float y = scorePanelY + 10f;

        if (streakPowerUpRuntime.isCherryActive()) {
            String t = String.format(Locale.US, "Cherry %.1fs", streakPowerUpRuntime.getCherryTimeLeft());
            font.setColor(1f, 0.45f, 0.52f, 1f);
            layout.setText(font, t);
            float tx = scorePanelX + (scorePanelW - layout.width) / 2f;
            drawScorePanelText(batch, t, tx, y);
            y += 17f;
        }
        if (streakPowerUpRuntime.isBananaActive()) {
            String t = String.format(Locale.US, "Banana %.1fs", streakPowerUpRuntime.getBananaTimeLeft());
            font.setColor(1f, 0.88f, 0.38f, 1f);
            layout.setText(font, t);
            float tx = scorePanelX + (scorePanelW - layout.width) / 2f;
            drawScorePanelText(batch, t, tx, y);
            y += 17f;
        }
        if (streakPowerUpRuntime.isWatermelonActive()) {
            String t = String.format(Locale.US, "Watermelon %.1fs", streakPowerUpRuntime.getWatermelonTimeLeft());
            font.setColor(0.35f, 0.92f, 0.52f, 1f);
            layout.setText(font, t);
            float tx = scorePanelX + (scorePanelW - layout.width) / 2f;
            drawScorePanelText(batch, t, tx, y);
        }

        font.getData().setScale(savedScale);
        font.setColor(Color.WHITE);
    }

    /** Softer shadow for score panel text - less harsh than default drop shadow. */
    private void drawScorePanelText(SpriteBatch batch, String text, float x, float y) {
        Color original = font.getColor().cpy();
        font.setColor(1f, 1f, 1f, 1f);

        // Soft shadow (lighter, smaller offset)
        font.setColor(0f, 0f, 0f, 0.4f);
        font.draw(batch, text, x + 1f, y - 1f);

        // Main text
        font.setColor(original);
        font.draw(batch, text, x, y);
    }

    private void drawTextWithShadow(SpriteBatch batch, BitmapFont f, String text, float x, float y) {
        Color original = f.getColor().cpy();

        // shadow
        f.setColor(0f, 0f, 0f, 0.85f);
        f.draw(batch, text, x + 2f, y - 2f);

        // main text
        f.setColor(original);
        f.draw(batch, text, x, y);
    }
    
    private void drawBlackTextWithShadow(SpriteBatch batch, BitmapFont f, String text, float x, float y) {
        Color original = f.getColor().cpy();

        // Light grey shadow for black text on white
        f.setColor(0.6f, 0.6f, 0.6f, 0.6f);
        f.draw(batch, text, x + 2f, y - 2f);

        // main text
        f.setColor(original);
        f.draw(batch, text, x, y);
    }

    private void drawPointsFeedbackLabel(SpriteBatch batch, BitmapFont f, String text, float x, float y, Color c) {
        Color original = f.getColor().cpy();
        f.setColor(0f, 0f, 0f, c.a * 0.55f);
        f.draw(batch, text, x + 4f, y - 4f);
        f.setColor(c);
        f.draw(batch, text, x, y);
        f.setColor(original);
    }

    // ---------------------------
    // CONFIGURATION
    // ---------------------------

    private static final class CategoryConfig {
        final String backgroundPath;
        final List<QuestionBank.Question> questions;

        CategoryConfig(String backgroundPath, List<QuestionBank.Question> questions) {
            this.backgroundPath = backgroundPath;
            this.questions = questions;
        }
    }

    private static final class CategoryConfigFactory {
        static CategoryConfig get(GameCategory category) {
            if (category == GameCategory.CATEGORIZATION) {
                // Load category game questions from QuestionBank
                return new CategoryConfig(
                    "GameMode_Categorization.png",  // Categorization background
                    QuestionBank.getCategoryQuestions()
                );
            } else {
                // Load all language questions (Grammar + Synonyms + Antonyms)
                return new CategoryConfig(
                    "GameMode_Grammar.png",  // Grammar background
                    QuestionBank.getAllLanguageQuestions()
                );
            }
        }
    }
}