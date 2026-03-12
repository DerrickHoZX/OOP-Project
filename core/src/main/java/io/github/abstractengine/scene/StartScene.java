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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.Square;
import io.github.abstractengine.entities.Triangle;
import io.github.abstractengine.io.KeyCode;
import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.*;
import io.github.abstractengine.movement.KeyboardMovement;
import io.github.abstractengine.movement.RandomMovement;
import io.github.abstractengine.collision.*;
import io.github.abstractengine.effects.ScreenFlash;

import java.util.ArrayList;
import java.util.List;

public class StartScene extends Scene {

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

    private final GlyphLayout layout = new GlyphLayout();

    private String currentQuestionPrompt = "";
    private final List<Square> currentAnswerSquares;

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
    }

    @Override
    public void onEnter() {
        shapeRenderer = new ShapeRenderer();
        
        // NEW: Initialize screen flash effect
        screenFlash = new ScreenFlash();

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
                300f,
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

        // Create button texture (orange color for visibility)
        pauseButtonTex = makeSolidTexture(1, 1, new Color(1f, 0.6f, 0f, 1f));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
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
                150f,
                2.0f,
                viewport.getWorldWidth(),
                viewport.getWorldHeight(),
                tWidth
        ));

        entityManager.addEntity(triangle);
        movementManager.register(triangle);
    }

    public void spawnNextQuestion() {
        // Clear old squares
        for (Square s : currentAnswerSquares) {
            entityManager.removeEntity(s);
        }
        currentAnswerSquares.clear();

        // Pick random question from config
        QuestionBank.Question q = config.questions.get(MathUtils.random(0, config.questions.size() - 1));
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
            float distToPlayer = com.badlogic.gdx.math.Vector2.dst(
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

        statsManager.update(dt);
        if (statsManager.isTimeUp()) {
            sceneManager.setScene(new EndScene(sceneManager, viewport, statsManager));
            return;
        }

        movementManager.update(dt);
        entityManager.update(dt);
        collisionManager.update(dt);
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

        // Score/Streak panel (top-left)
        shapeRenderer.setColor(0f, 0f, 0f, 0.20f);
        shapeRenderer.rect(15f, worldH - 95f, 220f, 75f);

        // Time panel (top-center)
        shapeRenderer.rect(worldW / 2f - 90f, worldH - 55f, 180f, 40f);

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

        // Score
        drawTextWithShadow(batch, font,
                "Score: " + statsManager.getScore(),
                25f, worldH - 25f);

        // Streak
        drawTextWithShadow(batch, font,
                "Streak: " + statsManager.getCurrentStreak(),
                25f, worldH - 55f);

        // Time (centered)
        String timeText = "Time: " + seconds + "s";
        layout.setText(font, timeText);
        float timeX = (worldW - layout.width) / 2f;
        drawTextWithShadow(batch, font, timeText, timeX, worldH - 25f);

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
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void onExit() {
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