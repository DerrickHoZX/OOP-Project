package io.github.some_example_name.entities;

import com.badlogic.gdx.math.Rectangle;								// for movement
import com.badlogic.gdx.math.Vector2;                              // for movement
import io.github.some_example_name.interfaces.Movable;              //  for movement
import io.github.some_example_name.movement.MovementComponent;      // for movement
import com.badlogic.gdx.math.Rectangle; // Or java.awt.Rectangle depending on your library

public class CollidableEntity extends Entity implements Movable { 

    protected float width;
    protected float height;
    protected MovementComponent movementComponent; // added for movement

    public CollidableEntity(float x, float y, float w, float h) {
        super(x, y);       
        this.width = w;
        this.height = h;
        this.isCollidable = true; // IMPORTANT: Flag this as collidable
        this.movementComponent = null;
    }

    @Override
    public void update(float dt) {
        // Movement is handled by MovementManager now
    }

    @Override
    public void render() {
        // Implement draw logic
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public Rectangle getCollisionBox() {
        return new Rectangle(x, y, width, height);
    }

    // Concrete implementation (required because the class is not abstract)
    public void onCollision(Entity other) {
        // Default: do nothing. Override in subclasses like Player/Enemy.
        System.out.println("Collision detected with " + other);
    }
    
    
    //added for movement 
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