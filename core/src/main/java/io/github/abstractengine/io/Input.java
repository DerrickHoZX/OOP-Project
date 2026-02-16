package io.github.abstractengine.io;

import java.util.EnumMap;
import java.util.Map;

public class Input {

    private final Keyboard keyboard;
    private final Mouse mouse;
    private final Logging logging;

    // Tracks previous down-state so isKeyDown() can log once per press
    private final Map<KeyCode, Boolean> lastDown = new EnumMap<>(KeyCode.class);

    public Input(Logging logging) {
        this.logging = logging;
        this.keyboard = new Keyboard();
        this.mouse = new Mouse();

        // init all keys to false (not down)
        for (KeyCode k : KeyCode.values()) {
            lastDown.put(k, false);
        }
    }

    public void update() {
        keyboard.update();
        mouse.update();
        // (No logging here)
    }

    // -----------------------------
    // Keyboard
    // -----------------------------

    public boolean isKeyDown(KeyCode key) {
        boolean downNow = keyboard.isPressed(key);
        boolean wasDown = lastDown.getOrDefault(key, false);

        // edge: just went down
        if (downNow && !wasDown && logging != null) {
            logging.info(LogCategory.INPUT, "Key down: " + key);
        }

        lastDown.put(key, downNow);
        return downNow;
    }

    public boolean isKeyJustPressed(KeyCode key) {
        boolean pressed = keyboard.isJustPressed(key);

        if (pressed && logging != null) {
            logging.info(LogCategory.INPUT, "Key pressed: " + key);
        }

        // keep state consistent
        if (pressed) lastDown.put(key, true);

        return pressed;
    }

    // -----------------------------
    // Mouse (NO raw logging here)
    // -----------------------------

    public int getMouseX() { return mouse.getX(); }
    public int getMouseY() { return mouse.getY(); }

    public boolean isMouseButtonDown(MouseButton button) { return mouse.isPressed(button); }
    public boolean isMouseJustPressed(MouseButton button) { return mouse.isJustPressed(button); }
}
