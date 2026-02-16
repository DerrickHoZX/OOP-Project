package io.github.some_example_name.managers;

import io.github.some_example_name.io.Input;
import io.github.some_example_name.io.KeyCode;
import io.github.some_example_name.io.MouseButton;

public class IOManager {

    private final Input input;

    public IOManager() {
        this.input = new Input();
    }

    public void update() {
        input.update();
    }

    // Access to Input subsystem (useful for components that need more than wrappers)
    public Input getInput() {
        return input;
    }

    // --- Delegated convenience methods ---
    public boolean isKeyDown(KeyCode key) {
        return input.isKeyDown(key);
    }

    public boolean isKeyJustPressed(KeyCode key) {
        return input.isKeyJustPressed(key);
    }

    public int getMouseX() {
        return input.getMouseX();
    }

    public int getMouseY() {
        return input.getMouseY();
    }

    public boolean isMouseButtonDown(MouseButton button) {
        return input.isMouseButtonDown(button);
    }

    public boolean isMouseJustPressed(MouseButton button) {
        return input.isMouseJustPressed(button);
    }
}
