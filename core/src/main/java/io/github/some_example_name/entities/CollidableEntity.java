package io.github.some_example_name.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; 
import io.github.some_example_name.interfaces.Movable;
import io.github.some_example_name.movement.MovementComponent;

public class CollidableEntity extends Entity implements Movable { 

    protected float width;
    protected float height;
    protected MovementComponent movementComponent;

    public CollidableEntity(float x, float y, float w, float h) {
        super(x, y);       
        this.width = w;
        this.height = h;
        this.isCollidable = true;
        this.movementComponent = null;
    }

    @Override
    public void update(float dt) {
        // Movement logic handled by manager or component
    }

    // UPDATED: Implementation of the new abstract method
    @Override
    public void render(ShapeRenderer shapeRenderer) {
        // Default: Draw nothing. Child classes (Circle/Square) will override this.
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public Rectangle getCollisionBox() {
        return new Rectangle(x, y, width, height);
    }

    public void onCollision(Entity other) {
        System.out.println("Collision detected with " + other);
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