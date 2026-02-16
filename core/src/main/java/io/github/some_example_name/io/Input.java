package io.github.some_example_name.io;

import java.util.ArrayList;
import java.util.List;

import io.github.some_example_name.interfaces.InputDevice;

public class Input {

    private final List<InputDevice> devices = new ArrayList<>();

    
    private final Keyboard keyboard;
    private final Mouse mouse;

    public Input() {
        this.keyboard = new Keyboard();
        this.mouse = new Mouse();

        // Register default devices
        addDevice(keyboard);
        addDevice(mouse);
    }

    public void update() {
        for (InputDevice d : devices) {
            d.update();
        }
    }

    public void addDevice(InputDevice device) {
        if (device == null) return;
        if (!devices.contains(device)) devices.add(device);
    }

    public void removeDevice(InputDevice device) {
        devices.remove(device);
    }

    // --- Keyboard queries ---
    public boolean isKeyDown(KeyCode key) {
        return keyboard.isPressed(key);
    }

    public boolean isKeyJustPressed(KeyCode key) {
        return keyboard.isJustPressed(key);
    }

    // --- Mouse queries ---
    public int getMouseX() {
        return mouse.getX();
    }

    public int getMouseY() {
        return mouse.getY();
    }

    public boolean isMouseButtonDown(MouseButton button) {
        return mouse.isPressed(button);
    }

    public boolean isMouseJustPressed(MouseButton button) {
        return mouse.isJustPressed(button);
    }
}
