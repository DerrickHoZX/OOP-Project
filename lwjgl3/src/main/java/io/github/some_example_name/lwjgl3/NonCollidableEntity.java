package io.github.some_example_name.lwjgl3;

public class NonCollidableEntity extends Entity {

    public NonCollidableEntity(float x, float y) {
        super(x, y);
        this.isCollidable = false;
    }

    @Override
    public void update(float dt) {
        // Implement logic (e.g., cloud drifting)
    }

    @Override
    public void render() {
        // Implement drawing logic
    }
}