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
import io.github.abstractengine.entities.PlayerFactory;
import io.github.abstractengine.entities.EnemyFactory;
import io.github.abstractengine.entities.SquareFactory;
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
    
    private ScreenFlash screenFlash;

    private final GlyphLayout layout = new GlyphLayout();

    private String currentQuestionPrompt = "";
    private final List<Square> currentAnswerSquares;

    private Stage stage;
    private Texture pauseButtonTex;

    public StartScene(SceneManager sceneManager, Viewport viewport) {
        this(sceneManager, viewport, GameCategory.GRAMMAR);
    }

    public StartScene(SceneManager sceneManager, Viewport viewport, GameCategory category) {
        super(sceneManager);
        this.viewport = viewport;
        this.category = category;

        this.config = CategoryConfigFactory.get(category);

        this.entityManager = new EntityManager();
        this.movementManager = new MovementManager();
        this.statsManager = new StatisticsManager(60f);
        this.currentAnswerSquares = new ArrayList<>();
    }

    @Override
    public void onEnter() {
        shapeRenderer = new ShapeRenderer();
        
        screenFlash = new ScreenFlash();

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.getData().setScale(1.5f);

        questionFont = new BitmapFont();
        questionFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        questionFont.setUseIntegerPositions(false);
        questionFont.getData().setScale(2.0f);

        bg = new Texture(config.backgroundPath);

        sceneManager.getIOManager().playMusic(AssetManager.MUSIC_START_SCENE, true);

        // Register factories
        entityManager.registerFactory(Circle.class, new PlayerFactory());
        entityManager.registerFactory(Triangle.class, new EnemyFactory());
        entityManager.registerFactory(Square.class, new SquareFactory());

        float circleSize = 60f;
        circle = entityManager.createEntity(Circle.class, viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
        circle.setMovementComponent(new KeyboardMovement(
                sceneManager.getIOManager(),
                300f,
                viewport.getWorldWidth(),
                viewport.getWorldHeight(),
                circleSize
        ));
        movementManager.register(circle);

        for (int i = 0; i < 3; i++) {
            spawnEnemy();
        }

        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();
        
        float playAreaMinX = 0f;
        float playAreaMaxX = worldW;
        float playAreaMinY = 0f;
        float playAreaMaxY = worldH - 180f; 
        
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

        pauseButtonTex = makeSolidTexture(1, 1, new Color(1f, 0.6f, 0f, 1f));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
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

    public void spawnEnemy() {
        float tWidth = 70f;
        float tHeight = 70f;
        
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();
        
        float safeMinX = 250f;  
        float safeMaxX = worldW - tWidth - 20f;
        float safeMinY = 20f;
        float safeMaxY = worldH - 200f; 
        
        float randomX = MathUtils.random(safeMinX, safeMaxX);
        float randomY = MathUtils.random(safeMinY, safeMaxY);

        Triangle triangle = entityManager.createEntity(Triangle.class, randomX, randomY);
        triangle.setMovementComponent(new RandomMovement(
                150f,
                2.0f,
                viewport.getWorldWidth(),
                viewport.getWorldHeight(),
                tWidth
        ));

        movementManager.register(triangle);
    }

    public void spawnNextQuestion() {
        for (Square s : currentAnswerSquares) {
            entityManager.removeEntity(s);
        }
        currentAnswerSquares.clear();

        QuestionBank.Question q = config.questions.get(MathUtils.random(0, config.questions.size() - 1));
        currentQuestionPrompt = q.prompt;

        String[] answers = { q.correct, q.decoy1, q.decoy2 };
        boolean[] correctness = { true, false, false };

        for (int i = 0; i < answers.length; i++) {
            spawnAnswerWithOverlapProtection(answers[i], correctness[i]);
        }
    }
    
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
        float sWidth = 160f;
        float sHeight = 70f;
        float spawnX = 0, spawnY = 0;
        boolean invalidPosition;
        int attempts = 0;

        float minDistanceFromPlayer = 150f;
        
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();
        
        float safeMinX = 250f;  
        float safeMaxX = worldW - sWidth - 20f;
        float safeMinY = 50f;
        float safeMaxY = worldH - 200f; 

        do {
            invalidPosition = false;

            spawnX = MathUtils.random(safeMinX, safeMaxX);
            spawnY = MathUtils.random(safeMinY, safeMaxY);

            float playerCenterX = circle.getX() + circle.getWidth() / 2f;
            float playerCenterY = circle.getY() + circle.getHeight() / 2f;
            float distToPlayer = com.badlogic.gdx.math.Vector2.dst(
                    spawnX + sWidth / 2f, spawnY + sHeight / 2f,
                    playerCenterX, playerCenterY
            );

            if (distToPlayer < minDistanceFromPlayer) {
                invalidPosition = true;
            }

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

        Square square = entityManager.createEntity(Square.class, spawnX, spawnY);
        square.setAnswerDetails(text, isCorrect);
        currentAnswerSquares.add(square);
    }

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

        statsManager.update(dt);
        if (statsManager.isTimeUp()) {
            sceneManager.setScene(new EndScene(sceneManager, viewport, statsManager));
            return;
        }

        movementManager.update(dt);
        entityManager.update(dt);
        collisionManager.update(dt);
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

        batch.begin();
        if (bg != null) batch.draw(bg, 0, 0, worldW, worldH);
        entityManager.render(batch, shapeRenderer);
        batch.end();

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0f, 0f, 0f, 0.20f);
        shapeRenderer.rect(15f, worldH - 95f, 220f, 75f);

        shapeRenderer.rect(worldW / 2f - 90f, worldH - 55f, 180f, 40f);

        shapeRenderer.setColor(1f, 1f, 1f, 0.95f); 
        shapeRenderer.rect(worldW / 2f - 300f, worldH - 150f, 600f, 55f);

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();

        int seconds = (int) Math.ceil(statsManager.getTimeRemaining());

        font.setColor(Color.WHITE);
        questionFont.setColor(Color.BLACK); 

        drawTextWithShadow(batch, font,
                "Score: " + statsManager.getScore(),
                25f, worldH - 25f);

        drawTextWithShadow(batch, font,
                "Streak: " + statsManager.getCurrentStreak(),
                25f, worldH - 55f);

        String timeText = "Time: " + seconds + "s";
        layout.setText(font, timeText);
        float timeX = (worldW - layout.width) / 2f;
        drawTextWithShadow(batch, font, timeText, timeX, worldH - 25f);

        layout.setText(questionFont, currentQuestionPrompt);
        float qX = (worldW - layout.width) / 2f;
        drawBlackTextWithShadow(batch, questionFont, currentQuestionPrompt, qX, worldH - 110f);

        batch.end();

        if (stage != null) {
            stage.draw();
        }
        
        if (screenFlash.isFlashing()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(screenFlash.getCurrentColor());
            shapeRenderer.rect(0, 0, worldW, worldH); 
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

        f.setColor(0f, 0f, 0f, 0.85f);
        f.draw(batch, text, x + 2f, y - 2f);

        f.setColor(original);
        f.draw(batch, text, x, y);
    }
    
    private void drawBlackTextWithShadow(SpriteBatch batch, BitmapFont f, String text, float x, float y) {
        Color original = f.getColor().cpy();

        f.setColor(0.6f, 0.6f, 0.6f, 0.6f);
        f.draw(batch, text, x + 2f, y - 2f);

        f.setColor(original);
        f.draw(batch, text, x, y);
    }
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
                return new CategoryConfig(
                        "GameMode_Categorization.png",
                        QuestionBank.getCategoryQuestions()
                );
            } else if (category == GameCategory.GRAMMAR) {
                return new CategoryConfig(
                        "GameMode_Grammar.png",
                        QuestionBank.getAllLanguageQuestions()
                );
            }

            return new CategoryConfig(
                    "GameMode_Grammar.png",
                    QuestionBank.getAllLanguageQuestions()
            );
        }
    }
}