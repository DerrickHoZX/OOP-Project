package io.github.abstractengine.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.Square;
import io.github.abstractengine.entities.Triangle;
import io.github.abstractengine.io.KeyCode;
import io.github.abstractengine.managers.*;
import io.github.abstractengine.movement.KeyboardMovement;
import io.github.abstractengine.movement.RandomMovement;
import io.github.abstractengine.collision.*;

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

    private final GlyphLayout layout = new GlyphLayout();

    private String currentQuestionPrompt = "";
    private final List<Square> currentAnswerSquares;

    public StartScene(SceneManager sceneManager, Viewport viewport) {
        this(sceneManager, viewport, GameCategory.GRAMMAR);
    }

    public StartScene(SceneManager sceneManager, Viewport viewport, GameCategory category) {
        super(sceneManager);
        this.viewport = viewport;
        this.category = category;

        // central place to decide background + questions
        this.config = CategoryConfigFactory.get(category);

        this.entityManager = new EntityManager();
        this.movementManager = new MovementManager();
        this.statsManager = new StatisticsManager(60f);
        this.currentAnswerSquares = new ArrayList<>();
    }

    @Override
    public void onEnter() {
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.getData().setScale(1.5f);

        questionFont = new BitmapFont();
        questionFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        questionFont.setUseIntegerPositions(false);
        questionFont.getData().setScale(2.0f);

        // Category-based background
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

        Boundary boundary = new Boundary(0, viewport.getWorldWidth(), 0, viewport.getWorldHeight());
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
    }

    public void spawnEnemy() {
        float tWidth = 70f;
        float tHeight = 70f;
        float randomX = MathUtils.random(0, viewport.getWorldWidth() - tWidth);
        float randomY = MathUtils.random(0, viewport.getWorldHeight() - tHeight);

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
        Question q = config.questions.get(MathUtils.random(0, config.questions.size() - 1));
        currentQuestionPrompt = q.prompt;

        // Spawn answers (1 correct + 2 decoys)
        String[] answers = { q.correct, q.decoy1, q.decoy2 };
        boolean[] correctness = { true, false, false };

        for (int i = 0; i < answers.length; i++) {
            spawnAnswerWithOverlapProtection(answers[i], correctness[i]);
        }
    }

    private void spawnAnswerWithOverlapProtection(String text, boolean isCorrect) {
        float sWidth = 160f;
        float sHeight = 70f;
        float spawnX = 0, spawnY = 0;
        boolean invalidPosition;
        int attempts = 0;

        float minDistanceFromPlayer = 150f;

        do {
            invalidPosition = false;

            spawnX = MathUtils.random(50f, viewport.getWorldWidth() - sWidth - 50f);
            spawnY = MathUtils.random(50f, viewport.getWorldHeight() - sHeight - 250f);

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
        if (sceneManager.getIOManager().isKeyJustPressed(KeyCode.ESCAPE)) {
            sceneManager.pushScene(new PauseScene(sceneManager, viewport, this, statsManager));
            return;
        }

        statsManager.update(dt);
        if (statsManager.isTimeUp()) {
            sceneManager.setScene(new EndScene(sceneManager, viewport, statsManager));
            return;
        }

        movementManager.update(dt);
        entityManager.update(dt);
        collisionManager.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);

        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        // ----- Draw Background + Entities -----
        batch.begin();
        if (bg != null) batch.draw(bg, 0, 0, worldW, worldH);
        entityManager.render(batch, shapeRenderer);
        batch.end();

        // ----- HUD PANELS (transparent; requires blending) -----
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Score/Streak panel (top-left) - light
        shapeRenderer.setColor(0f, 0f, 0f, 0.20f);
        shapeRenderer.rect(15f, worldH - 95f, 220f, 75f);

        // Time panel (top-center) - light
        shapeRenderer.rect(worldW / 2f - 90f, worldH - 55f, 180f, 40f);

        // Question panel (center-top) - slightly stronger
        shapeRenderer.setColor(0f, 0f, 0f, 0.28f);
        shapeRenderer.rect(worldW / 2f - 300f, worldH - 150f, 600f, 55f);

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // ----- TEXT -----
        batch.begin();

        int seconds = (int) Math.ceil(statsManager.getTimeRemaining());

        font.setColor(Color.WHITE);
        questionFont.setColor(Color.WHITE);

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

        // Question (centered)
        layout.setText(questionFont, currentQuestionPrompt);
        float qX = (worldW - layout.width) / 2f;
        drawTextWithShadow(batch, questionFont, currentQuestionPrompt, qX, worldH - 110f);

        batch.end();
    }

    @Override
    public void onExit() {
        if (bg != null) bg.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (font != null) font.dispose();
        if (questionFont != null) questionFont.dispose();

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

    // ---------------------------
    // Helpers (same file)
    // ---------------------------

    private static final class Question {
        final String prompt;
        final String correct;
        final String decoy1;
        final String decoy2;

        Question(String prompt, String correct, String decoy1, String decoy2) {
            this.prompt = prompt;
            this.correct = correct;
            this.decoy1 = decoy1;
            this.decoy2 = decoy2;
        }
    }

    private static final class CategoryConfig {
        final String backgroundPath;
        final List<Question> questions;

        CategoryConfig(String backgroundPath, List<Question> questions) {
            this.backgroundPath = backgroundPath;
            this.questions = questions;
        }
    }

    private static final class CategoryConfigFactory {
        static CategoryConfig get(GameCategory category) {
            if (category == GameCategory.CATEGORIZATION) {
                List<Question> qs = new ArrayList<>();
                qs.add(new Question("Find an Action Word (Verb)", "Run", "Apple", "Blue"));
                qs.add(new Question("Find an Adjective", "Happy", "Jump", "Chair"));
                qs.add(new Question("Find a Noun", "Car", "Quickly", "Run"));
                qs.add(new Question("Pick a Verb", "Write", "Green", "Dog"));
                return new CategoryConfig("startscene_category.png", qs);
            } else {
                List<Question> qs = new ArrayList<>();
                qs.add(new Question("Find the word that rhymes with Cat", "Hat", "Dog", "Sun"));
                qs.add(new Question("What is the plural of Mouse?", "Mice", "Mouses", "Meese"));
                qs.add(new Question("Find the opposite of Happy", "Sad", "Excited", "Fast"));
                qs.add(new Question("Choose the correct article: ___ apple", "An", "A", "The"));
                qs.add(new Question("Pick the past tense of 'go'", "Went", "Goed", "Going"));
                qs.add(new Question("Pick the synonym of 'big'", "Large", "Tiny", "Slow"));
                return new CategoryConfig("startscene_grammar.png", qs);
            }
        }
    }
}