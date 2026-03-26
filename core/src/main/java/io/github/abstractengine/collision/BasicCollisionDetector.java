package io.github.abstractengine.collision;

import io.github.abstractengine.entities.CollidableEntity;
import io.github.abstractengine.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic collision detector using AABB (Axis-Aligned Bounding Box).
 * Works with any CollidableEntity without knowing concrete game types.
 */
public class BasicCollisionDetector implements ICollisionDetector {

    @Override
    public List<CollisionInfo> checkCollisions(List<Entity> entities) {
        List<CollisionInfo> collisions = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);

                if (e1 instanceof CollidableEntity && e2 instanceof CollidableEntity) {
                    if (checkAABB((CollidableEntity) e1, (CollidableEntity) e2)) {
                        collisions.add(new CollisionInfo(e1, e2));
                    }
                }
            }
        }

        return collisions;
    }

    @Override
    public boolean checkCircle(Entity circle1, Entity circle2) {
        if (!(circle1 instanceof CollidableEntity && circle2 instanceof CollidableEntity)) {
            return false;
        }

        CollidableEntity c1 = (CollidableEntity) circle1;
        CollidableEntity c2 = (CollidableEntity) circle2;

        float c1CenterX = c1.getX() + c1.getWidth() / 2f;
        float c1CenterY = c1.getY() + c1.getHeight() / 2f;
        float c1Radius = c1.getWidth() / 2f;

        float c2CenterX = c2.getX() + c2.getWidth() / 2f;
        float c2CenterY = c2.getY() + c2.getHeight() / 2f;
        float c2Radius = c2.getWidth() / 2f;

        float dx = c2CenterX - c1CenterX;
        float dy = c2CenterY - c1CenterY;
        float distanceSquared = dx * dx + dy * dy;

        float radiusSum = c1Radius + c2Radius;
        return distanceSquared < (radiusSum * radiusSum);
    }

    @Override
    public boolean checkSquare(Entity square1, Entity square2) {
        if (!(square1 instanceof CollidableEntity && square2 instanceof CollidableEntity)) {
            return false;
        }
        return checkAABB((CollidableEntity) square1, (CollidableEntity) square2);
    }

    @Override
    public boolean checkCircleSquare(Entity e1, Entity e2) {
        if (!(e1 instanceof CollidableEntity && e2 instanceof CollidableEntity)) {
            return false;
        }

        CollidableEntity c1 = (CollidableEntity) e1;
        CollidableEntity c2 = (CollidableEntity) e2;

        // Treat the smaller entity as the "circle" for circle-rect math
        CollidableEntity circle, rect;
        if (c1.getWidth() <= c2.getWidth()) {
            circle = c1;
            rect = c2;
        } else {
            circle = c2;
            rect = c1;
        }

        float circleCenterX = circle.getX() + circle.getWidth() / 2f;
        float circleCenterY = circle.getY() + circle.getHeight() / 2f;
        float circleRadius = circle.getWidth() / 2f;

        float closestX = Math.max(rect.getX(), Math.min(circleCenterX, rect.getX() + rect.getWidth()));
        float closestY = Math.max(rect.getY(), Math.min(circleCenterY, rect.getY() + rect.getHeight()));

        float dx = circleCenterX - closestX;
        float dy = circleCenterY - closestY;
        float distanceSquared = dx * dx + dy * dy;

        return distanceSquared < (circleRadius * circleRadius);
    }

    @Override
    public boolean checkBoundary(Entity entity, Boundary boundary) {
        if (!(entity instanceof CollidableEntity)) {
            return false;
        }

        CollidableEntity collidable = (CollidableEntity) entity;

        return collidable.getX() <= boundary.getMinX() ||
               collidable.getX() + collidable.getWidth() >= boundary.getMaxX() ||
               collidable.getY() <= boundary.getMinY() ||
               collidable.getY() + collidable.getHeight() >= boundary.getMaxY();
    }

    /**
     * Generic AABB collision check between any two collidable entities.
     */
    private boolean checkAABB(CollidableEntity a, CollidableEntity b) {
        return a.getX() < b.getX() + b.getWidth() &&
               a.getX() + a.getWidth() > b.getX() &&
               a.getY() < b.getY() + b.getHeight() &&
               a.getY() + a.getHeight() > b.getY();
    }
}