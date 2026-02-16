package io.github.abstractengine.entities;

import com.badlogic.gdx.math.Vector2;

import io.github.abstractengine.interfaces.Movable;
import io.github.abstractengine.movement.MovementComponent;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class NonCollidableEntity extends Entity implements Movable {

    protected MovementComponent movementComponent;
    
    public NonCollidableEntity(float x, float y) {
        super(x, y);
        this.isCollidable = false;
        this.movementComponent = null;
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
    }
    
    @Override
    public Vector2 getPosition() {
        return new Vector2(x, y);
    }
    
    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public MovementComponent getMovementComponent() {
        return movementComponent;
    }
    
    public void setMovementComponent(MovementComponent component) {
        this.movementComponent = component;
    }
}