package io.github.some_example_name.managers;

import io.github.some_example_name.entities.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();
            if (e.isActive()) {
                e.update(dt);
            } else {
                iterator.remove();
            }
        }
    }

    // UPDATED: Pass ShapeRenderer
    public void render(ShapeRenderer shapeRenderer) {
        for (Entity e : entities) {
            if (e.isActive()) {
                e.render(shapeRenderer);
            }
        }
    }

    public List<Entity> getCollidableEntities() {
        List<Entity> collidables = new ArrayList<>();
        for (Entity e : entities) {
            if (e.isCollidable() && e.isActive()) {
                collidables.add(e);
            }
        }
        return collidables;
    }
}