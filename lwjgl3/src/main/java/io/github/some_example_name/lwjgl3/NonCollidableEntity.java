package io.github.some_example_name.lwjgl3;

public abstract class NonCollidableEntity extends Entity {

    // Constructor
    public NonCollidableEntity(float x, float y) {
        super(x, y);
        this.isCollidable = false;
    }

    // Subclasses (e.g., Clouds, Backgrounds) must still implement update/render
}