package io.github.some_example_name.lwjgl3;

public abstract class NonCollidableEntity extends Entity {

    public NonCollidableEntity(float x, float y) {
        super(x, y);
        this.isCollidable = false;
    }

    // Subclasses must still implement abstract update() and render()
}