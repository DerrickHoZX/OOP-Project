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
    private Texture bg;
    private CollisionManager collisionManager;
    private Circle circle;
    private EntityManager entityManager;
    private MovementManager movementManager;
    private StatisticsManager statsManager; 
    private ShapeRenderer shapeRenderer;
    private BitmapFont font; 
    private BitmapFont questionFont;
    
    private String currentQuestionPrompt = "";
    private List<Square> currentAnswerSquares;

    private class Question {
        String prompt, correct, decoy1, decoy2;
        public Question(String p, String c, String d1, String d2) {
            prompt = p; correct = c; decoy1 = d1; decoy2 = d2;
        }
    }
    private List<Question> questionBank;

    public StartScene(SceneManager sceneManager, Viewport viewport) {
        super(sceneManager);
        this.viewport = viewport;
        this.entityManager = new EntityManager();
        this.movementManager = new MovementManager();
        this.statsManager = new StatisticsManager(60f); 
        this.currentAnswerSquares = new ArrayList<>();
        
        questionBank = new ArrayList<>();
        /// Simple Question
        questionBank.add(new Question("Find the word that rhymes with Cat", "Hat", "Dog", "Sun"));
        questionBank.add(new Question("What is the plural of Mouse?", "Mice", "Mouses", "Meese"));
        questionBank.add(new Question("Find the opposite of Happy", "Sad", "Excited", "Fast"));
        // Math & Logic
        questionBank.add(new Question("What is 12 x 12?", "144", "124", "142"));
        questionBank.add(new Question("What is 50 divided by 2?", "25", "20", "30"));
        questionBank.add(new Question("What is the square root of 64?", "8", "6", "12"));
    }

    @Override
    public void onEnter() {
        shapeRenderer = new ShapeRenderer(); 
        
        font = new BitmapFont(); 
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.getData().setScale(1.5f); 
        font.setColor(Color.BLACK); 
        
        questionFont = new BitmapFont();
        questionFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        questionFont.setUseIntegerPositions(false);
        questionFont.getData().setScale(2.0f);
        questionFont.setColor(Color.BLACK);
        
        bg = new Texture("startscene.png"); 
        sceneManager.getIOManager().playMusic(AssetManager.MUSIC_START_SCENE, true);

        float circleSize = 60f;
        circle = new Circle(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, circleSize, circleSize);
        circle.setMovementComponent(new KeyboardMovement(sceneManager.getIOManager(), 300f, viewport.getWorldWidth(), viewport.getWorldHeight(), circleSize));
        entityManager.addEntity(circle);
        movementManager.register(circle);

        for (int i = 0; i < 3; i++) {
            spawnEnemy();
        }

        Boundary boundary = new Boundary(0, viewport.getWorldWidth(), 0, viewport.getWorldHeight());
        BasicCollisionDetector detector = new BasicCollisionDetector();
        SimulationCollisionHandler handler = new SimulationCollisionHandler(sceneManager, entityManager, viewport, circle, statsManager, this);
        collisionManager = new CollisionManager(boundary, entityManager, detector, handler);
        
        spawnNextQuestion();
    }

    public void spawnEnemy() {
        float tWidth = 70f;
        float tHeight = 70f;
        float randomX = MathUtils.random(0, viewport.getWorldWidth() - tWidth);
        float randomY = MathUtils.random(0, viewport.getWorldHeight() - tHeight);

        Triangle triangle = new Triangle(randomX, randomY, tWidth, tHeight);
        triangle.setMovementComponent(new RandomMovement(150f, 2.0f, viewport.getWorldWidth(), viewport.getWorldHeight(), tWidth));

        entityManager.addEntity(triangle);
        movementManager.register(triangle);
    }

    public void spawnNextQuestion() {
        // 1. Clear old squares safely
        for (Square s : currentAnswerSquares) {
            entityManager.removeEntity(s);
        }
        currentAnswerSquares.clear();

        // 2. Pick a random question
        Question q = questionBank.get(MathUtils.random(0, questionBank.size() - 1));
        currentQuestionPrompt = q.prompt;

        // 3. Spawn 3 answers with overlap protection
        String[] answers = {q.correct, q.decoy1, q.decoy2};
        boolean[] correctness = {true, false, false};

        for (int i = 0; i < answers.length; i++) {
            spawnAnswerWithOverlapProtection(answers[i], correctness[i]);
        }
    }

    private void spawnAnswerWithOverlapProtection(String text, boolean isCorrect) {
        float sWidth = 160f; 
        float sHeight = 70f;
        float spawnX = 0, spawnY = 0;
        boolean overlapping;
        int attempts = 0;

        // Keep trying until we find a non-overlapping spot or hit an attempt limit
        do {
            overlapping = false;
            spawnX = MathUtils.random(50f, viewport.getWorldWidth() - sWidth - 50f);
            spawnY = MathUtils.random(50f, viewport.getWorldHeight() - sHeight - 250f);

            // Check against every square already in the current list
            for (Square other : currentAnswerSquares) {
                float buffer = 40f; // Extra space between boxes
                if (spawnX < other.getX() + other.getWidth() + buffer &&
                    spawnX + sWidth + buffer > other.getX() &&
                    spawnY < other.getY() + other.getHeight() + buffer &&
                    spawnY + sHeight + buffer > other.getY()) {
                    overlapping = true;
                    break;
                }
            }
            attempts++;
        } while (overlapping && attempts < 100);

        Square square = new Square(spawnX, spawnY, sWidth, sHeight, text, isCorrect);
        entityManager.addEntity(square);
        currentAnswerSquares.add(square);
    }

    @Override
    public void update(float dt) {
        if (sceneManager.getIOManager().isKeyJustPressed(KeyCode.ESCAPE)) {
            // UPDATED: Now passes statsManager so PauseScene can hand it to EndScene if needed!
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
        // Clear the screen entirely before drawing to prevent font smearing
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        
        // Single batch loop for everything
        batch.begin();
        
        if (bg != null) batch.draw(bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        
        entityManager.render(batch, shapeRenderer); 
        
        int seconds = (int) Math.ceil(statsManager.getTimeRemaining()); 
        font.draw(batch, "Time: " + seconds + "s", viewport.getWorldWidth() / 2f - 50f, viewport.getWorldHeight() - 30f);
        font.draw(batch, "Score: " + statsManager.getScore(), 20f, viewport.getWorldHeight() - 30f); 
        font.draw(batch, "Streak: " + statsManager.getCurrentStreak(), 20f, viewport.getWorldHeight() - 60f); 
        
        questionFont.draw(batch, currentQuestionPrompt, viewport.getWorldWidth() / 2f - 250f, viewport.getWorldHeight() - 120f);
        
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
}