package io.github.abstractengine.managers;

import io.github.abstractengine.collision.Boundary;
import io.github.abstractengine.collision.CollisionInfo;
import io.github.abstractengine.collision.ICollisionDetector;
import io.github.abstractengine.collision.ICollisionHandler;
import io.github.abstractengine.entities.Entity;
import java.util.List;

public class CollisionManager {
    
    private ICollisionDetector detector;
    private ICollisionHandler handler;
    private EntityManager entityManager;
    private Boundary boundaries;
    
    public CollisionManager(Boundary boundaries, EntityManager entityManager,
                           ICollisionDetector detector, ICollisionHandler handler) {
        this.boundaries = boundaries;
        this.entityManager = entityManager;
        this.detector = detector;
        this.handler = handler;
    }
    
    public void update(float deltaTime) {
        List<Entity> collidables = entityManager.getCollidableEntities();
        List<CollisionInfo> collisions = detector.checkCollisions(collidables);
        
        for (CollisionInfo collision : collisions) {
            handler.handleCollision(collision);
        }
        
        for (Entity entity : collidables) {
            if (detector.checkBoundary(entity, boundaries)) {
                handler.handleBoundaryCollision(entity, boundaries);
            }
        }
    }
    
    public ICollisionDetector getDetector() { return detector; }
    public ICollisionHandler getHandler() { return handler; }
    public Boundary getBoundaries() { return boundaries; }
    
    public void setDetector(ICollisionDetector detector) { this.detector = detector; }
    public void setHandler(ICollisionHandler handler) { this.handler = handler; }
    public void setBoundaries(Boundary boundaries) { this.boundaries = boundaries; }
    
    public void clear() {}
}