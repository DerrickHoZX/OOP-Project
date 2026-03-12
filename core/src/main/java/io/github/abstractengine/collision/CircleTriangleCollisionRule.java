package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.entities.Triangle;
import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.AssetManager;
import io.github.abstractengine.managers.EntityManager;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.managers.StatisticsManager;
import io.github.abstractengine.scene.StartScene;

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
    private final StartScene startScene;

    public CircleTriangleCollisionRule(SceneManager sceneManager,
                                       EntityManager entityManager,
                                       StatisticsManager statisticsManager,
                                       StartScene startScene) {
        this.sceneManager = sceneManager;
        this.entityManager = entityManager;
        this.statisticsManager = statisticsManager;
        this.startScene = startScene;
    }

    @Override
    public void apply(CollisionInfo info) {
        Entity e1 = info.getEntity1();
        Entity e2 = info.getEntity2();

        Circle circle = null;
        Triangle triangle = null;

        if (e1 instanceof Circle) {
            circle = (Circle) e1;
        } else if (e1 instanceof Triangle) {
            triangle = (Triangle) e1;
        }

        if (e2 instanceof Circle) {
            circle = (Circle) e2;
        } else if (e2 instanceof Triangle) {
            triangle = (Triangle) e2;
        }

        // Safety check – rule should only run for Circle–Triangle pairs
        if (circle == null || triangle == null) {
            return;
        }

        sceneManager.getIOManager().getLogging().info(LogCategory.SESSION, "Hit Enemy! Deducting points.");
        sceneManager.getIOManager().playSfx(AssetManager.SFX_OVER);
        statisticsManager.registerEnemyCollision();
        startScene.flashWrong();
        entityManager.removeEntity(triangle);
        startScene.spawnEnemy();
    }
}

