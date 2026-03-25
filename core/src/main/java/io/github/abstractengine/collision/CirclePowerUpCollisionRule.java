package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.entities.PowerUpPickup;
import io.github.abstractengine.managers.AssetManager;
import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.scene.StartScene;

public class CirclePowerUpCollisionRule implements ICollisionRule {

    private final SceneManager sceneManager;
    private final StartScene startScene;

    public CirclePowerUpCollisionRule(SceneManager sceneManager, StartScene startScene) {
        this.sceneManager = sceneManager;
        this.startScene = startScene;
    }

    @Override
    public void apply(CollisionInfo info) {
        Entity e1 = info.getEntity1();
        Entity e2 = info.getEntity2();

        if (e1 instanceof Circle && e2 instanceof PowerUpPickup) {
            collect((PowerUpPickup) e2);
        } else if (e2 instanceof Circle && e1 instanceof PowerUpPickup) {
            collect((PowerUpPickup) e1);
        }
    }

    private void collect(PowerUpPickup pickup) {
        if (pickup == null || !pickup.isActive()) {
            return;
        }
        sceneManager.getIOManager().playSfx(AssetManager.SFX_SPEED_BOOST);
        startScene.onPowerUpCollected(pickup);
    }
}
