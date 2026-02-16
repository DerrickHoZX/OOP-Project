package io.github.abstractengine.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.Square;
import io.github.abstractengine.entities.Triangle;
import io.github.abstractengine.io.KeyCode;
import io.github.abstractengine.managers.EntityManager;
import io.github.abstractengine.managers.MovementManager;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.movement.KeyboardMovement;
import io.github.abstractengine.movement.RandomMovement;
import io.github.abstractengine.collision.BasicCollisionDetector;
import io.github.abstractengine.collision.Boundary;
import io.github.abstractengine.collision.GameCollisionHandler;
import io.github.abstractengine.managers.CollisionManager;

import com.badlogic.gdx.math.MathUtils;

public class StartScene extends Scene {

    private final Viewport viewport;
    private Texture bg;
    
    private CollisionManager collisionManager;  // For Collision 
    private Circle circle;  // For Collision handler
    
    private EntityManager entityManager;
    private MovementManager movementManager;
    private ShapeRenderer shapeRenderer;

    public StartScene(SceneManager sceneManager, Viewport viewport) {
        super(sceneManager);
        this.viewport = viewport;
        this.entityManager = new EntityManager();
        this.movementManager = new MovementManager();
    }

    @Override
    public void onEnter() {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        bg = new Texture("startscene.png"); 
        
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // --- 1. PLAYER (Circle) ---
        float circleSize = 60f;
        circle = new Circle(
        		(worldWidth - circleSize) / 2f, 
                (worldHeight - circleSize) / 2f, 
                circleSize, circleSize
            );
        
        // CHANGED: Pass screen dimensions to KeyboardMovement
        circle.setMovementComponent(new KeyboardMovement(sceneManager.getIOManager(), 300f, worldWidth, worldHeight, circleSize));
        
        entityManager.addEntity(circle);
        movementManager.register(circle);

        // --- 2. ENEMIES (Triangles) ---
        int triangleCount = 3; 
        float tWidth = 70f;
        float tHeight = 70f;

        for (int i = 0; i < triangleCount; i++) {
            float randomX = MathUtils.random(0, worldWidth - tWidth);
            float randomY = MathUtils.random(0, worldHeight - tHeight);

            Triangle triangle = new Triangle(randomX, randomY, tWidth, tHeight);
            
            // Random Movement (Bounces off walls)
            triangle.setMovementComponent(new RandomMovement(150f, 2.0f, worldWidth, worldHeight, tWidth));

            entityManager.addEntity(triangle);
            movementManager.register(triangle);
        }

        // --- 3. ITEMS (Squares) ---
        int squareCount = 2;
        float sWidth = 50f;
        float sHeight = 50f;

        for (int i = 0; i < squareCount; i++) {
            float randomX = MathUtils.random(0, worldWidth - sWidth);
            float randomY = MathUtils.random(0, worldHeight - sHeight);

            Square square = new Square(randomX, randomY, sWidth, sHeight);
            entityManager.addEntity(square);
        }
        
        // --- 4. INITIALIZE COLLISION SYSTEM ---
        Boundary boundary = new Boundary(0, worldWidth, 0, worldHeight);
        BasicCollisionDetector detector = new BasicCollisionDetector();
        GameCollisionHandler handler = new GameCollisionHandler(sceneManager, entityManager, viewport, circle);

        collisionManager = new CollisionManager(boundary, entityManager, detector, handler);
    }

    @Override
    public void update(float dt) {
        if (sceneManager.getIOManager().isKeyJustPressed(KeyCode.ESCAPE)) {
            sceneManager.pushScene(new PauseScene(sceneManager, viewport, this));
            return;
        }

        movementManager.update(dt);
        entityManager.update(dt);
        collisionManager.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if (bg != null) batch.draw(bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();
        
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        entityManager.render(shapeRenderer); 
        shapeRenderer.end();
    }

    @Override
    public void onExit() {
        if (bg != null) bg.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        movementManager.clear();
        if (collisionManager != null) collisionManager.clear();
    }
}