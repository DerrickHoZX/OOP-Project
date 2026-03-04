package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.Square;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.managers.StatisticsManager;
import io.github.abstractengine.scene.StartScene;

public class CircleSquareCollisionRule implements ICollisionRule {

    private SceneManager sceneManager;
    private StatisticsManager statsManager;
    private StartScene startScene;

    public CircleSquareCollisionRule(SceneManager sceneManager, StatisticsManager statsManager, StartScene startScene) {
        this.sceneManager = sceneManager;
        this.statsManager = statsManager;
        this.startScene = startScene;
    }

    @Override
    public void apply(CollisionInfo info) {
        // Determine which is the circle and which is the square
        Circle circle = null;
        Square square = null;

        if (info.getEntity1() instanceof Circle && info.getEntity2() instanceof Square) {
            circle = (Circle) info.getEntity1();
            square = (Square) info.getEntity2();
        } else if (info.getEntity2() instanceof Circle && info.getEntity1() instanceof Square) {
            circle = (Circle) info.getEntity2();
            square = (Square) info.getEntity1();
        }

        if (circle == null || square == null) {
            return; // Should not happen, but safety check
        }

        // Check if the answer is correct
        if (square.isCorrect()) {
            // ✅ CORRECT ANSWER!
            statsManager.registerCorrectAnswer();  // FIXED: Uses your actual method name

            // Trigger GREEN screen flash
            startScene.flashCorrect();

            // Play success sound (if you have one)
            // sceneManager.getIOManager().playSfx(AssetManager.SFX_CORRECT);

        } else {
            // ❌ WRONG ANSWER!
            statsManager.registerIncorrectAnswer();  // FIXED: Uses your actual method name

            // Trigger RED screen flash
            startScene.flashWrong();

            // Play error sound (if you have one)
            // sceneManager.getIOManager().playSfx(AssetManager.SFX_WRONG);
        }

        // Remove the collected square
        square.destroy();

        // Spawn next question
        startScene.spawnNextQuestion();
    }
}