package io.github.some_example_name.lwjgl3;

import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityManager {

    // Private attribute
    private ArrayList<Entity> entities;

    // Constructor
    public EntityManager() {
        entities = new ArrayList<Entity>();
    }

    // Methods
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void update(float deltaTime) {
        // Safe removal using Iterator to prevent ConcurrentModificationException
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            
            // 1. Update the entity
            entity.update(deltaTime);
            
            // 2. Check if it should be destroyed (using destroy() from Entity class)
            if (!entity.isActive) { // accessing protected field via package-private
                iterator.remove();
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Entity entity : entities) {
            entity.render(batch);
        }
    }

    public void dispose() {
        entities.clear();
    }
}