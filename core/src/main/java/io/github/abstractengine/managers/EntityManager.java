package io.github.abstractengine.managers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.entities.EntityFactory;
import io.github.abstractengine.game.entities.DisposableEntity;
import io.github.abstractengine.interfaces.Movable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Centralized manager for creating, updating, rendering,
 * and removing entities. Supports the Factory pattern via
 * registered EntityFactory instances for type-safe entity creation.
 */

public class EntityManager {

    private List<Entity> entities;
    private final Map<Class<? extends Entity>, EntityFactory<? extends Entity>> factories;
    private MovementManager movementManager;

    public EntityManager() {
        this.entities = new ArrayList<>();
        this.factories = new HashMap<>();
        this.movementManager = null;
    }

    public void setMovementManager(MovementManager movementManager) {
        this.movementManager = movementManager;
    }

    public <T extends Entity> void registerFactory(Class<T> type, EntityFactory<T> factory) {
        factories.put(type, factory);
    }

    public <T extends Entity> T createEntity(Class<T> type, float x, float y) {
        EntityFactory<?> factory = factories.get(type);
        if (factory != null) {
            T newEntity = type.cast(factory.createEntity(x, y));
            addEntity(newEntity);
            return newEntity;
        }
        throw new IllegalArgumentException("Unknown entity type: " + type.getSimpleName());
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        if (entity == null) return;
        entities.remove(entity);
        cleanupEntity(entity);
    }

    public void update(float dt) {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();
            if (e.isActive()) {
                e.update(dt);
            } else {
                cleanupEntity(e);
                iterator.remove();
            }
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        for (Entity e : entities) {
            if (e.isActive()) {
                e.render(batch, shapeRenderer);
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

    /** Snapshot for read-only iteration (e.g. apply per-entity effects). */
    public List<Entity> getEntitiesSnapshot() {
        return new ArrayList<>(entities);
    }

    private void cleanupEntity(Entity entity) {
        if (movementManager != null && entity instanceof Movable) {
            movementManager.unregister((Movable) entity);
        }
        if (entity instanceof DisposableEntity) {
            ((DisposableEntity) entity).disposeEntity();
        }
    }
}