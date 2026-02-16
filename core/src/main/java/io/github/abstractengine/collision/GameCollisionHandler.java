package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.CollidableEntity;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.entities.Square;
import io.github.abstractengine.entities.Triangle;
import io.github.abstractengine.managers.EntityManager;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.movement.MovementComponent;
import io.github.abstractengine.scene.EndScene;

import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * GameCollisionHandler implements specific collision rules:
 * - Circle + Triangle = Transition to EndScene
 * - Circle + Square = Speed Boost + Remove Square
 * - Any Entity + Boundary = Bounce Back
 */
public class GameCollisionHandler implements ICollisionHandler {
    
    private SceneManager sceneManager;
    private EntityManager entityManager;
    private Viewport viewport;
    private Circle targetCircle;  // The circle entity to apply effects to
    private SpeedBoostEffect speedBoost;
    
    /**
     * Creates a game collision handler
     * @param sceneManager For scene transitions
     * @param entityManager For entity management
     * @param viewport For creating new scenes
     * @param targetCircle The circle entity that receives effects (speed boost)
     */
    public GameCollisionHandler(SceneManager sceneManager, EntityManager entityManager, 
                               Viewport viewport, Circle targetCircle) {
        this.sceneManager = sceneManager;
        this.entityManager = entityManager;
        this.viewport = viewport;
        this.targetCircle = targetCircle;
        
        // Create speed boost effect: 40% increase for 5 seconds
        this.speedBoost = new SpeedBoostEffect(targetCircle, 1.4f, 5.0f);
    }
    
    /**
     * Update the speed boost timer (call every frame)
     */
    public void update(float dt) {
        speedBoost.update(dt);
    }
    
    @Override
    public void handleCollision(CollisionInfo info) {
        handleEntityCollision(info.getEntity1(), info.getEntity2());
    }
    
    @Override
    public void handleEntityCollision(Entity entity1, Entity entity2) {
        // Identify entity types
        Circle circle = null;
        Triangle triangle = null;
        Square square = null;
        
        // Check entity1
        if (entity1 instanceof Circle) circle = (Circle) entity1;
        else if (entity1 instanceof Triangle) triangle = (Triangle) entity1;
        else if (entity1 instanceof Square) square = (Square) entity1;
        
        // Check entity2
        if (entity2 instanceof Circle) circle = (Circle) entity2;
        else if (entity2 instanceof Triangle) triangle = (Triangle) entity2;
        else if (entity2 instanceof Square) square = (Square) entity2;
        
        // RULE 1: Circle + Triangle = Scene Transition
        if (circle != null && triangle != null) {
            System.out.println("Circle-Triangle collision detected. Transitioning to EndScene.");
            sceneManager.setScene(new EndScene(sceneManager, viewport));
            return;
        }
        
        // RULE 2: Circle + Square = Speed Boost + Remove Square
        if (circle != null && square != null) {
            System.out.println("Circle-Square collision detected. Applying speed boost effect.");
            speedBoost.activate();
            entityManager.removeEntity(square);
            return;
        }
        
        // Additional collision rules can be added here
    }
    
    @Override
    public void handleBoundaryCollision(Entity entity, Boundary boundary) {
        if (!(entity instanceof CollidableEntity)) return;
        
        CollidableEntity collidable = (CollidableEntity) entity;
        MovementComponent movement = collidable.getMovementComponent();
        
        if (movement == null) return;
        
        // Get current velocity
        float vx = movement.getVelocity().x;
        float vy = movement.getVelocity().y;
        
        // For circles, check from center
        if (entity instanceof Circle) {
            float centerX = collidable.getX() + collidable.getWidth() / 2f;
            float centerY = collidable.getY() + collidable.getHeight() / 2f;
            float radius = collidable.getWidth() / 2f;
            
            // Left or Right boundary
            if (centerX - radius <= boundary.getMinX()) {
                collidable.setPosition(boundary.getMinX() + radius - collidable.getWidth() / 2f, collidable.getY());
                movement.getVelocity().x = Math.abs(vx); // Bounce right
            } 
            else if (centerX + radius >= boundary.getMaxX()) {
                collidable.setPosition(boundary.getMaxX() - radius - collidable.getWidth() / 2f, collidable.getY());
                movement.getVelocity().x = -Math.abs(vx); // Bounce left
            }
            
            // Bottom or Top boundary
            if (centerY - radius <= boundary.getMinY()) {
                collidable.setPosition(collidable.getX(), boundary.getMinY() + radius - collidable.getHeight() / 2f);
                movement.getVelocity().y = Math.abs(vy); // Bounce up
            } 
            else if (centerY + radius >= boundary.getMaxY()) {
                collidable.setPosition(collidable.getX(), boundary.getMaxY() - radius - collidable.getHeight() / 2f);
                movement.getVelocity().y = -Math.abs(vy); // Bounce down
            }
        }
        // For squares/triangles, check bounding box
        else {
            // Left or Right boundary
            if (collidable.getX() <= boundary.getMinX()) {
                collidable.setPosition(boundary.getMinX(), collidable.getY());
                movement.getVelocity().x = Math.abs(vx); // Bounce right
            } 
            else if (collidable.getX() + collidable.getWidth() >= boundary.getMaxX()) {
                collidable.setPosition(boundary.getMaxX() - collidable.getWidth(), collidable.getY());
                movement.getVelocity().x = -Math.abs(vx); // Bounce left
            }
            
            // Bottom or Top boundary
            if (collidable.getY() <= boundary.getMinY()) {
                collidable.setPosition(collidable.getX(), boundary.getMinY());
                movement.getVelocity().y = Math.abs(vy); // Bounce up
            } 
            else if (collidable.getY() + collidable.getHeight() >= boundary.getMaxY()) {
                collidable.setPosition(collidable.getX(), boundary.getMaxY() - collidable.getHeight());
                movement.getVelocity().y = -Math.abs(vy); // Bounce down
            }
        }
    }
    
    public SpeedBoostEffect getSpeedBoost() {
        return speedBoost;
    }
}