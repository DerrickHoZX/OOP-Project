package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.entities.Square;
import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.AssetManager;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.managers.StatisticsManager;
import io.github.abstractengine.scene.StartScene;

/**
 * Handles collisions between the player circle and an answer hub square.
 *
 * This rule is responsible for logging correct / wrong answers,
 * updating statistics and spawning the next question.
 */
public class CircleSquareCollisionRule implements ICollisionRule {

    private final SceneManager sceneManager;
    private final StatisticsManager statisticsManager;
    private final StartScene startScene;

    public CircleSquareCollisionRule(SceneManager sceneManager,
                                     StatisticsManager statisticsManager,
                                     StartScene startScene) {
        this.sceneManager = sceneManager;
        this.statisticsManager = statisticsManager;
        this.startScene = startScene;
    }

    @Override
    public void apply(CollisionInfo info) {
        Entity e1 = info.getEntity1();
        Entity e2 = info.getEntity2();

        Circle circle = null;
        Square square = null;

        if (e1 instanceof Circle) {
            circle = (Circle) e1;
        } else if (e1 instanceof Square) {
            square = (Square) e1;
        }

        if (e2 instanceof Circle) {
            circle = (Circle) e2;
        } else if (e2 instanceof Square) {
            square = (Square) e2;
        }

        // Safety check – rule should only run for Circle–Square pairs
        if (circle == null || square == null) {
            return;
        }

        if (square.isCorrect()) {
        	sceneManager.getIOManager().getLogging().info(LogCategory.SESSION, "Correct Answer!");
        	sceneManager.getIOManager().playSfx(AssetManager.SFX_SPEED_BOOST);
            statisticsManager.registerCorrectAnswer();
        } else {
        	sceneManager.getIOManager().getLogging().info(LogCategory.SESSION, "Wrong Answer!");
        	sceneManager.getIOManager().playSfx(AssetManager.SFX_OVER);
            statisticsManager.registerIncorrectAnswer();
        }

        startScene.spawnNextQuestion();
    }
}

