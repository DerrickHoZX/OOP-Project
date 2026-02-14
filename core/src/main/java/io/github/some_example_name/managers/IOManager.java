package io.github.some_example_name.managers;

import io.github.some_example_name.io.Keyboard;
import io.github.some_example_name.io.KeyCode;
import io.github.some_example_name.io.Mouse;
import io.github.some_example_name.io.MouseButton;

public class IOManager {

    private final Mouse mouse;
    private final Keyboard keyboard;

    public IOManager() {
        mouse = new Mouse();
        keyboard = new Keyboard();
    }

    public void update() {
        mouse.update();
        keyboard.update();
    }

    public boolean isMouseJustPressed(MouseButton button) {
        return mouse.isJustPressed(button);
    }

    public boolean isKeyJustPressed(KeyCode key) {
        return keyboard.isJustPressed(key);
    }
    
}
