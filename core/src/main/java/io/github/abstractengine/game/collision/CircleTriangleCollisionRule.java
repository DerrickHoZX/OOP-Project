package io.github.abstractengine.game.collision;
import io.github.abstractengine.collision.CollisionInfo;
import io.github.abstractengine.collision.ICollisionRule;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.game.StatisticsManager;
import io.github.abstractengine.game.entities.Circle;
import io.github.abstractengine.game.entities.Triangle;

import io.github.abstractengine.interfaces.GameEventListener;

import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.EntityManager;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.game.GameAssets;

/**
 * Handles collisions between the player circle and an enemy triangle.
 *
 * In the simulation this means deducting points, playing a sound,
 * removing the enemy and spawning a new one.
 */

public class CircleTriangleCollisionRule implements ICollisionRule {

    private final SceneManager sceneManager;
    private final EntityManager entityManager;
    private final StatisticsManager statisticsManager;
    private final GameEventListener listener;

    public CircleTriangleCollisionRule(SceneManager sceneManager,
                                       EntityManager entityManager,
                                       StatisticsManager statisticsManager,
                                       GameEventListener listener) {
        this.sceneManager = sceneManager;
        this.entityManager = entityManager;
        this.statisticsManager = statisticsManager;
        this.listener = listener;
    }

    @Override
    public void apply(CollisionInfo info) {
        // Skip damage if player is in safe zone
        if (listener.isPlayerSafe()) {
            return;
        }

        Entity e1 = info.getEntity1();
        Entity e2 = info.getEntity2();

        Circle circle = null;
        Triangle triangle = null;

        if (e1 instanceof Circle) circle = (Circle) e1;
        else if (e1 instanceof Triangle) triangle = (Triangle) e1;
        if (e2 instanceof Circle) circle = (Circle) e2;
        else if (e2 instanceof Triangle) triangle = (Triangle) e2;

        if (circle == null || triangle == null) return;

        sceneManager.getIOManager().getLogging().info(LogCategory.SESSION, "Hit Enemy! Deducting points.");
        sceneManager.getIOManager().playSfx(GameAssets.SFX_OVER);
        int lost = statisticsManager.registerEnemyCollision();
        listener.onEnemyHit(lost);
        entityManager.removeEntity(triangle);
        listener.onEnemyDestroyed();
    }
}