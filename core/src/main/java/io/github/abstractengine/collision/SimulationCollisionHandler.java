package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.CollidableEntity;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.entities.Square;
import io.github.abstractengine.entities.Triangle;
import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.EntityManager;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.managers.StatisticsManager;
import io.github.abstractengine.movement.MovementComponent;
import io.github.abstractengine.scene.StartScene; 
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.abstractengine.managers.AssetManager;

public class SimulationCollisionHandler implements ICollisionHandler {
    
    private SceneManager sceneManager;
    private EntityManager entityManager;
    private Viewport viewport;
    private Circle targetCircle;  
    private StatisticsManager statsManager; 
    private StartScene startScene; 
    
    public SimulationCollisionHandler(SceneManager sceneManager, EntityManager entityManager, 
                                      Viewport viewport, Circle targetCircle, 
                                      StatisticsManager statsManager, StartScene startScene) {
        this.sceneManager = sceneManager;
        this.entityManager = entityManager;
        this.viewport = viewport;
        this.targetCircle = targetCircle;
        this.statsManager = statsManager;
        this.startScene = startScene; 
    }
    
    @Override
    public void handleCollision(CollisionInfo info) {
        handleEntityCollision(info.getEntity1(), info.getEntity2());
    }
    
    @Override
    public void handleEntityCollision(Entity entity1, Entity entity2) {
        Circle circle = null;
        Triangle triangle = null;
        Square square = null;
        
        if (entity1 instanceof Circle) circle = (Circle) entity1;
        else if (entity1 instanceof Triangle) triangle = (Triangle) entity1;
        else if (entity1 instanceof Square) square = (Square) entity1;
        
        if (entity2 instanceof Circle) circle = (Circle) entity2;
        else if (entity2 instanceof Triangle) triangle = (Triangle) entity2;
        else if (entity2 instanceof Square) square = (Square) entity2;
        
        // RULE 1: Enemy Hit
        if (circle != null && triangle != null) {
            sceneManager.getIOManager().log(LogCategory.SESSION, "Hit Enemy! Deducting points.");
            sceneManager.getIOManager().playSfx(AssetManager.SFX_OVER); 
            statsManager.registerEnemyCollision(); 
            entityManager.removeEntity(triangle);
            startScene.spawnEnemy(); 
            return;
        }        
        
        // RULE 2: Answer Hub Hit
        if (circle != null && square != null) {
            if (square.isCorrect()) {
                sceneManager.getIOManager().log(LogCategory.SESSION, "Correct Answer!");
                sceneManager.getIOManager().playSfx(AssetManager.SFX_SPEED_BOOST); 
                statsManager.registerCorrectAnswer(); 
            } else {
                sceneManager.getIOManager().log(LogCategory.SESSION, "Wrong Answer!");
                sceneManager.getIOManager().playSfx(AssetManager.SFX_OVER); 
                statsManager.registerIncorrectAnswer();
            }
            startScene.spawnNextQuestion();
            return;
        }
    }
    
    @Override
    public void handleBoundaryCollision(Entity entity, Boundary boundary) {
        if (!(entity instanceof CollidableEntity)) return;
        
        CollidableEntity collidable = (CollidableEntity) entity;
        MovementComponent movement = collidable.getMovementComponent();
        
        if (movement == null) return;
        
        float vx = movement.getVelocity().x;
        float vy = movement.getVelocity().y;
        
        if (entity instanceof Circle) {
            float centerX = collidable.getX() + collidable.getWidth() / 2f;
            float centerY = collidable.getY() + collidable.getHeight() / 2f;
            float radius = collidable.getWidth() / 2f;
            
            if (centerX - radius <= boundary.getMinX()) {
                collidable.setPosition(boundary.getMinX() + radius - collidable.getWidth() / 2f, collidable.getY());
                movement.getVelocity().x = Math.abs(vx); 
            } else if (centerX + radius >= boundary.getMaxX()) {
                collidable.setPosition(boundary.getMaxX() - radius - collidable.getWidth() / 2f, collidable.getY());
                movement.getVelocity().x = -Math.abs(vx); 
            }
            
            if (centerY - radius <= boundary.getMinY()) {
                collidable.setPosition(collidable.getX(), boundary.getMinY() + radius - collidable.getHeight() / 2f);
                movement.getVelocity().y = Math.abs(vy); 
            } else if (centerY + radius >= boundary.getMaxY()) {
                collidable.setPosition(collidable.getX(), boundary.getMaxY() - radius - collidable.getHeight() / 2f);
                movement.getVelocity().y = -Math.abs(vy); 
            }
        } else {
            if (collidable.getX() <= boundary.getMinX()) {
                collidable.setPosition(boundary.getMinX(), collidable.getY());
                movement.getVelocity().x = Math.abs(vx); 
            } else if (collidable.getX() + collidable.getWidth() >= boundary.getMaxX()) {
                collidable.setPosition(boundary.getMaxX() - collidable.getWidth(), collidable.getY());
                movement.getVelocity().x = -Math.abs(vx); 
            }
            
            if (collidable.getY() <= boundary.getMinY()) {
                collidable.setPosition(collidable.getX(), boundary.getMinY());
                movement.getVelocity().y = Math.abs(vy); 
            } else if (collidable.getY() + collidable.getHeight() >= boundary.getMaxY()) {
                collidable.setPosition(collidable.getX(), boundary.getMaxY() - collidable.getHeight());
                movement.getVelocity().y = -Math.abs(vy); 
            }
        }
    }
}