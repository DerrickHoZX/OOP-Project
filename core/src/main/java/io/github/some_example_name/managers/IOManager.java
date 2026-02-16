package io.github.some_example_name.managers;

import io.github.some_example_name.io.*;

public class IOManager {

    private final Input input;
    private final Logging logging;
    private final Persistence persistence;

    public IOManager() {

        Logger logger = new Logger(LogDestination.BOTH, "logs/game.log");
        this.logging = new Logging(logger);

        this.input = new Input(logging);

        this.persistence = new Persistence(new FileIO(), "saves/", SaveFormat.TEXT);
    }

    public void update() {
        input.update();
    }

    public void dispose() {
        logging.dispose();
        persistence.dispose();
    }

    public void log(LogCategory category, String message) {
        logging.info(category, message);
    }

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

    public void save(String path, String data) {
        persistence.save(path, data);
        logging.info(LogCategory.PERSISTENCE, "Saved file: " + path);
    }

    public String load(String path) {
        String data = persistence.load(path);
        logging.info(LogCategory.PERSISTENCE, "Loaded file: " + path);
        return data;
    }

    public boolean exists(String path) {
        return persistence.exists(path);
    }
}
