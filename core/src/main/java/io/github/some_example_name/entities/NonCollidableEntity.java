package io.github.some_example_name.entities;

import com.badlogic.gdx.math.Vector2;                               // added for movement
import io.github.some_example_name.interfaces.Movable;              // added for movement
import io.github.some_example_name.movement.MovementComponent;      // added for movement

public class NonCollidableEntity extends Entity implements Movable {

	protected MovementComponent movementComponent; //for movement
	
    public NonCollidableEntity(float x, float y) {
        super(x, y);
        this.isCollidable = false;
        this.movementComponent = null; //for movement
    }

    @Override
    public void update(float dt) {
        // Movement is handled by MovementManager now
    }

    @Override
    public void render() {
        // Implement draw logic
    }
    
    // Implement Movable interface
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
    
    // Allow setting movement component
    public void setMovementComponent(MovementComponent component) {
        this.movementComponent = component;
    }
}