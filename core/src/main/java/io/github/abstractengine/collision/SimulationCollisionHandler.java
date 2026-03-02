package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.CollidableEntity;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.entities.Square;
import io.github.abstractengine.entities.Triangle;
import io.github.abstractengine.managers.EntityManager;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.managers.StatisticsManager;
import io.github.abstractengine.movement.MovementComponent;
import io.github.abstractengine.scene.StartScene;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;

public class SimulationCollisionHandler implements ICollisionHandler {
    
    private SceneManager sceneManager;
    private EntityManager entityManager;
    private Viewport viewport;
    private Circle targetCircle;  
    private StatisticsManager statsManager; 
    private StartScene startScene;

    /**
     * Map from unordered entity type pairs to the rule that should
     * be applied when those two kinds of entities collide.
     */
    private final Map<CollisionPairKey, ICollisionRule> rules = new HashMap<>();
    
    public SimulationCollisionHandler(SceneManager sceneManager, EntityManager entityManager, 
                                      Viewport viewport, Circle targetCircle, 
                                      StatisticsManager statsManager, StartScene startScene) {
        this.sceneManager = sceneManager;
        this.entityManager = entityManager;
        this.viewport = viewport;
        this.targetCircle = targetCircle;
        this.statsManager = statsManager;
        this.startScene = startScene;

        registerRules();
    }
    
    @Override
    public void handleCollision(CollisionInfo info) {
        ICollisionRule rule = findRule(info.getEntity1(), info.getEntity2());
        if (rule != null) {
            rule.apply(info);
        }
    }
    
    @Override
    public void handleEntityCollision(Entity entity1, Entity entity2) {
        handleCollision(new CollisionInfo(entity1, entity2));
    }
    
    @Override
    public void handleBoundaryCollision(Entity entity, Boundary boundary) {
        if (!(entity instanceof CollidableEntity)) return;
        
        CollidableEntity collidable = (CollidableEntity) entity;
        MovementComponent movement = collidable.getMovementComponent();
        if (movement == null) return;

        if (entity instanceof Circle) {
            handleCircleBoundaryCollision(collidable, movement, boundary);
        } else {
            handleRectangularBoundaryCollision(collidable, movement, boundary);
        }
    }

    /**
     * Register all entity‑entity collision rules used in this simulation.
     */
    private void registerRules() {
        rules.put(
                CollisionPairKey.of(Circle.class, Triangle.class),
                new CircleTriangleCollisionRule(sceneManager, entityManager, statsManager, startScene)
        );
        rules.put(
                CollisionPairKey.of(Circle.class, Square.class),
                new CircleSquareCollisionRule(sceneManager, statsManager, startScene)
        );
    }

    /**
     * Find a rule for the given pair of entities, or null if none exists.
     */
    private ICollisionRule findRule(Entity entity1, Entity entity2) {
        if (entity1 == null || entity2 == null) {
            return null;
        }
        CollisionPairKey key = CollisionPairKey.of(entity1.getClass(), entity2.getClass());
        return rules.get(key);
    }

    private void handleCircleBoundaryCollision(CollidableEntity collidable,
                                               MovementComponent movement,
                                               Boundary boundary) {
        float vx = movement.getVelocity().x;
        float vy = movement.getVelocity().y;

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
    }

    private void handleRectangularBoundaryCollision(CollidableEntity collidable,
                                                    MovementComponent movement,
                                                    Boundary boundary) {
        float vx = movement.getVelocity().x;
        float vy = movement.getVelocity().y;

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