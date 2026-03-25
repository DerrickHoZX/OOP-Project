package io.github.abstractengine.game.collision;

import io.github.abstractengine.collision.ICollisionRule;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.game.entities.Circle;
import io.github.abstractengine.game.entities.PowerUpPickup;

import io.github.abstractengine.interfaces.GameEventListener;

import io.github.abstractengine.managers.SceneManager;
import io.github.abstractengine.collision.CollisionInfo;
import io.github.abstractengine.game.GameAssets;

public class CirclePowerUpCollisionRule implements ICollisionRule {

    private final SceneManager sceneManager;
    private final GameEventListener listener;

    public CirclePowerUpCollisionRule(SceneManager sceneManager, GameEventListener listener) {
        this.sceneManager = sceneManager;
        this.listener = listener;
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
        if (pickup == null || !pickup.isActive()) return;
        sceneManager.getIOManager().playSfx(GameAssets.SFX_SPEED_BOOST);
        listener.onItemCollected(pickup);
    }
}