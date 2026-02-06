package io.github.some_example_name.lwjgl3;

import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityManager {

    private ArrayList<Entity> entities;

    public EntityManager() {
        this.entities = new ArrayList<Entity>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void update(float deltaTime) {
        // Use Iterator to safely remove entities while iterating
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            
            // 1. Update the entity logic
            entity.update(deltaTime);
            
            // 2. Check if the entity is dead
            // Accessing 'isActive' directly as it is protected and we are in the same package
            if (!entity.isActive) {
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