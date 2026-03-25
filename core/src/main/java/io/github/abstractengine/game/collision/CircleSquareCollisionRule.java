package io.github.abstractengine.game.collision;
import io.github.abstractengine.collision.CollisionInfo;
import io.github.abstractengine.collision.ICollisionRule;

import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.game.StatisticsManager;
import io.github.abstractengine.game.entities.Circle;
import io.github.abstractengine.game.entities.Square;

import io.github.abstractengine.interfaces.GameEventListener;

import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.game.GameAssets;

/**
 * Handles collisions between the player circle and an answer hub square.
 *
 * This rule is responsible for logging correct / wrong answers,
 * updating statistics and spawning the next question.
 */
public class CircleSquareCollisionRule implements ICollisionRule {

    private final SceneManager sceneManager;
    private final StatisticsManager statisticsManager;
    private final GameEventListener listener;

    public CircleSquareCollisionRule(SceneManager sceneManager,
                                     StatisticsManager statisticsManager,
                                     GameEventListener listener) {
        this.sceneManager = sceneManager;
        this.statisticsManager = statisticsManager;
        this.listener = listener;
    }

    @Override
    public void apply(CollisionInfo info) {
        Entity e1 = info.getEntity1();
        Entity e2 = info.getEntity2();

        Circle circle = null;
        Square square = null;

        if (e1 instanceof Circle) circle = (Circle) e1;
        else if (e1 instanceof Square) square = (Square) e1;
        if (e2 instanceof Circle) circle = (Circle) e2;
        else if (e2 instanceof Square) square = (Square) e2;

        if (circle == null || square == null) return;

        boolean wasCorrect = square.isCorrect();
        if (wasCorrect) {
            sceneManager.getIOManager().getLogging().info(LogCategory.SESSION, "Correct Answer!");
            sceneManager.getIOManager().playSfx(GameAssets.SFX_SPEED_BOOST);
            int gained = statisticsManager.registerCorrectAnswer();
            listener.onCorrectAnswer(gained);
        } else {
            sceneManager.getIOManager().getLogging().info(LogCategory.SESSION, "Wrong Answer!");
            sceneManager.getIOManager().playSfx(GameAssets.SFX_OVER);
            int lost = statisticsManager.registerIncorrectAnswer();
            listener.onWrongAnswer(lost);
        }

        square.destroy();
        listener.onAnswerSubmitted(wasCorrect);
    }
}