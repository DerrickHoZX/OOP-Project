package io.github.abstractengine.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // Added SpriteBatch import
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.abstractengine.interfaces.Movable;
import io.github.abstractengine.movement.MovementComponent;

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

    // UPDATED: Now accepts SpriteBatch to match the Entity class
    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
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