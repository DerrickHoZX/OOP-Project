package io.github.some_example_name.entities;

public class NonCollidableEntity extends Entity {

    public NonCollidableEntity(float x, float y) {
        super(x, y);
        this.isCollidable = false;
    }

    @Override
    public void update(float dt) {
        // Implement logic (e.g., simple animation)
    }

    @Override
    public void render() {
        // Implement draw logic
    }
}