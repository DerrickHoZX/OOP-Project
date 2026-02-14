package io.github.some_example_name.lwjgl3;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class EntityManager {

    private List<Entity> entities;

    public EntityManager() {
        this.entities = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void update(float dt) {
        // Use Iterator to safely remove inactive entities while looping
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();
            if (e.isActive()) {
                e.update(dt);
            } else {
                iterator.remove(); // Clean up destroyed entities
            }
        }
    }

    public void render() {
        for (Entity e : entities) {
            if (e.isActive()) {
                e.render();
            }
        }
    }

    // Returns a list containing ONLY the collidable entities
    public List<Entity> getCollidableEntities() {
        List<Entity> collidables = new ArrayList<>();
        for (Entity e : entities) {
            // Check the flag we set in the constructors
            if (e.isCollidable() && e.isActive()) {
                collidables.add(e);
            }
        }
        return collidables;
    }
}