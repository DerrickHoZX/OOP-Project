package io.github.abstractengine.game.entities;

import com.badlogic.gdx.math.MathUtils;
import io.github.abstractengine.entities.EntityFactory;

public class PowerUpFactory implements EntityFactory<PowerUpPickup> {

    @Override
    public PowerUpPickup createEntity(float x, float y) {
        // Set a default size for all power-up pickups
        float defaultSize = 40f; 
        
        // Randomly select one of the available PowerUpTypes
        PowerUpType[] types = PowerUpType.values();
        PowerUpType randomType = types[MathUtils.random(types.length - 1)];
        
        return new PowerUpPickup(x, y, defaultSize, randomType);
    }
}