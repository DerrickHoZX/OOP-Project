package io.github.abstractengine.collision;

import io.github.abstractengine.entities.Circle;
import io.github.abstractengine.entities.CollidableEntity;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.entities.Square;
import io.github.abstractengine.entities.Triangle;

import java.util.ArrayList;
import java.util.List;

/**
 * BasicCollisionDetector implements true circle collision detection.
 * Uses mathematical formulas to check overlapping shapes.
 */
public class BasicCollisionDetector implements ICollisionDetector {
    
    @Override
    public List<CollisionInfo> checkCollisions(List<Entity> entities) {
        List<CollisionInfo> collisions = new ArrayList<>();
        
        // Check every pair of entities (avoid duplicate checks)
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);
                
                // Determine collision type based on entity types
                boolean collision = false;
                
                if (e1 instanceof Circle && e2 instanceof Circle) {
                    collision = checkCircle(e1, e2);
                } 
                else if (e1 instanceof Square && e2 instanceof Square) {
                    collision = checkSquare(e1, e2);
                }
                else if (e1 instanceof Triangle && e2 instanceof Triangle) {
                    collision = checkSquare(e1, e2); // Treat triangles as squares
                }
                else if ((e1 instanceof Circle && e2 instanceof Square) ||
                         (e1 instanceof Square && e2 instanceof Circle)) {
                    collision = checkCircleSquare(e1, e2);
                }
                else if ((e1 instanceof Circle && e2 instanceof Triangle) ||
                         (e1 instanceof Triangle && e2 instanceof Circle)) {
                    collision = checkCircleSquare(e1, e2);
                }
                else if ((e1 instanceof Square && e2 instanceof Triangle) ||
                         (e1 instanceof Triangle && e2 instanceof Square)) {
                    collision = checkSquare(e1, e2);
                }
                
                if (collision) {
                    collisions.add(new CollisionInfo(e1, e2));
                }
            }
        }
        
        return collisions;
    }
    
    @Override
    public boolean checkCircle(Entity circle1, Entity circle2) {
        if (!(circle1 instanceof Circle && circle2 instanceof Circle)) {
            return false;
        }
        
        Circle c1 = (Circle) circle1;
        Circle c2 = (Circle) circle2;
        
        // Calculate circle centers
        float c1CenterX = c1.getX() + c1.getWidth() / 2f;
        float c1CenterY = c1.getY() + c1.getHeight() / 2f;
        float c1Radius = c1.getWidth() / 2f;
        
        float c2CenterX = c2.getX() + c2.getWidth() / 2f;
        float c2CenterY = c2.getY() + c2.getHeight() / 2f;
        float c2Radius = c2.getWidth() / 2f;
        
        // Calculate distance between centers
        float dx = c2CenterX - c1CenterX;
        float dy = c2CenterY - c1CenterY;
        float distanceSquared = dx * dx + dy * dy;
        
        // Check if circles overlap
        float radiusSum = c1Radius + c2Radius;
        return distanceSquared < (radiusSum * radiusSum);
    }
    
    @Override
    public boolean checkSquare(Entity square1, Entity square2) {
        if (!(square1 instanceof CollidableEntity && square2 instanceof CollidableEntity)) {
            return false;
        }
        
        CollidableEntity s1 = (CollidableEntity) square1;
        CollidableEntity s2 = (CollidableEntity) square2;
        
        // AABB (Axis-Aligned Bounding Box) collision
        return s1.getX() < s2.getX() + s2.getWidth() &&
               s1.getX() + s1.getWidth() > s2.getX() &&
               s1.getY() < s2.getY() + s2.getHeight() &&
               s1.getY() + s1.getHeight() > s2.getY();
    }
    
    @Override
    public boolean checkCircleSquare(Entity e1, Entity e2) {
        // Determine which is circle and which is square
        Circle circle;
        CollidableEntity square;
        
        if (e1 instanceof Circle) {
            circle = (Circle) e1;
            square = (CollidableEntity) e2;
        } else {
            circle = (Circle) e2;
            square = (CollidableEntity) e1;
        }
        
        // Circle center and radius
        float circleCenterX = circle.getX() + circle.getWidth() / 2f;
        float circleCenterY = circle.getY() + circle.getHeight() / 2f;
        float circleRadius = circle.getWidth() / 2f;
        
        // Find the closest point on the rectangle to the circle center
        float closestX = Math.max(square.getX(), Math.min(circleCenterX, square.getX() + square.getWidth()));
        float closestY = Math.max(square.getY(), Math.min(circleCenterY, square.getY() + square.getHeight()));
        
        // Calculate distance from circle center to this closest point
        float dx = circleCenterX - closestX;
        float dy = circleCenterY - closestY;
        float distanceSquared = dx * dx + dy * dy;
        
        // Check if distance is less than radius
        return distanceSquared < (circleRadius * circleRadius);
    }
    
    @Override
    public boolean checkBoundary(Entity entity, Boundary boundary) {
        if (!(entity instanceof CollidableEntity)) {
            return false;
        }
        
        CollidableEntity collidable = (CollidableEntity) entity;
        
        // For circles, check from center with radius
        if (entity instanceof Circle) {
            float centerX = collidable.getX() + collidable.getWidth() / 2f;
            float centerY = collidable.getY() + collidable.getHeight() / 2f;
            float radius = collidable.getWidth() / 2f;
            
            return centerX - radius <= boundary.getMinX() ||
                   centerX + radius >= boundary.getMaxX() ||
                   centerY - radius <= boundary.getMinY() ||
                   centerY + radius >= boundary.getMaxY();
        }
        
        // For squares/triangles, check bounding box
        return collidable.getX() <= boundary.getMinX() ||
               collidable.getX() + collidable.getWidth() >= boundary.getMaxX() ||
               collidable.getY() <= boundary.getMinY() ||
               collidable.getY() + collidable.getHeight() >= boundary.getMaxY();
    }
}