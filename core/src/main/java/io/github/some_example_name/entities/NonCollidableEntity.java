package io.github.some_example_name.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.some_example_name.interfaces.Movable;
import io.github.some_example_name.movement.MovementComponent;

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