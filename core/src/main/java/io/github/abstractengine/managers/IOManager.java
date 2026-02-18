package io.github.abstractengine.managers;

import io.github.abstractengine.io.*;

public class IOManager {

    private final Input input;
    private final Logging logging;
    private final Persistence persistence;

    // Audio
    private final AssetManager assets;
    private final Output output;

    // Settings persistence file (stored under basePath "saves/")
    private static final String SETTINGS_FILE = "settings.txt";
    private static final float DEFAULT_MUSIC_VOL = 1.0f;
    private static final float DEFAULT_SFX_VOL = 1.0f;

    public IOManager() {

        Logger logger = new Logger(LogDestination.CONSOLE, null);
        this.logging = new Logging(logger);

        // Assets (audio/images). For now we load audio here.
        this.assets = new AssetManager();
        this.assets.loadAll(logging);

        this.output = new Output(assets, logging);

        this.input = new Input(logging);

        this.persistence = new Persistence(new FileIO(), "saves/", SaveFormat.TEXT);

        // Load saved audio settings (must be AFTER output is created)
        loadAudioSettings();
    }

    public void update() {
        input.update();
    }

    public void dispose() {
        output.dispose();
        assets.dispose();
        logging.dispose();
        persistence.dispose();
    }

    public void log(LogCategory category, String message) {
        logging.info(category, message);
    }

    // -------- Input delegation --------
    public boolean isKeyDown(KeyCode key) { return input.isKeyDown(key); }

    public boolean isKeyJustPressed(KeyCode key) { return input.isKeyJustPressed(key); }

    public int getMouseX() { return input.getMouseX(); }

    public int getMouseY() { return input.getMouseY(); }

    public boolean isMouseButtonDown(MouseButton button) { return input.isMouseButtonDown(button); }

    public boolean isMouseJustPressed(MouseButton button) { return input.isMouseJustPressed(button); }

    // -------- Audio delegation --------
    public void playSfx(String id) { output.playSfx(id); }

    public void stopSfx() { output.stopSfx(); }

    public void playMusic(String id, boolean loop) { output.playMusic(id, loop); }

    public void stopMusic() { output.stopMusic(); }

    // Persisted volume setters
    public void setSfxVolume(float v) {
        output.setSfxVolume(clamp01(v));
        saveAudioSettings();
    }

    public float getSfxVolume() { return output.getSfxVolume(); }

    public void setMusicVolume(float v) {
        output.setMusicVolume(clamp01(v));
        saveAudioSettings();
    }

    public float getMusicVolume() { return output.getMusicVolume(); }

    public void toggleMute() { output.toggleMute(); }

    // -------- Persistence delegation --------
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

    public AssetManager getAssets() { return assets; }

    // -------- Settings helpers --------

    private float clamp01(float v) {
        return Math.max(0f, Math.min(1f, v));
    }

    private void loadAudioSettings() {
        try {
            if (!exists(SETTINGS_FILE)) {
                // Defaults if no settings file yet
                output.setMusicVolume(DEFAULT_MUSIC_VOL);
                output.setSfxVolume(DEFAULT_SFX_VOL);
                logging.info(LogCategory.PERSISTENCE, "No settings file found. Using default audio volumes.");
                return;
            }

            String data = load(SETTINGS_FILE);

            float music = parseKey(data, "music", DEFAULT_MUSIC_VOL);
            float sfx   = parseKey(data, "sfx", DEFAULT_SFX_VOL);

            output.setMusicVolume(clamp01(music));
            output.setSfxVolume(clamp01(sfx));

            logging.info(LogCategory.PERSISTENCE,
                    "Loaded audio settings: music=" + output.getMusicVolume() + ", sfx=" + output.getSfxVolume());

        } catch (Exception e) {
            // If file is corrupted / parse error, fall back to defaults
            output.setMusicVolume(DEFAULT_MUSIC_VOL);
            output.setSfxVolume(DEFAULT_SFX_VOL);
            logging.info(LogCategory.PERSISTENCE,
                    "Failed to load settings. Using defaults. Reason: " + e.getMessage());
        }
    }

    private void saveAudioSettings() {
        try {
            String data =
                    "music=" + output.getMusicVolume() + "\n" +
                    "sfx=" + output.getSfxVolume() + "\n";

            save(SETTINGS_FILE, data);

        } catch (Exception e) {
            logging.info(LogCategory.PERSISTENCE,
                    "Failed to save settings. Reason: " + e.getMessage());
        }
    }

    private float parseKey(String data, String key, float defaultVal) {
        if (data == null) return defaultVal;

        String[] lines = data.split("\\R"); // handles \n or \r\n
        for (String line : lines) {
            if (line == null) continue;
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue; // allow comments

            String prefix = key + "=";
            if (line.startsWith(prefix)) {
                String valueStr = line.substring(prefix.length()).trim();
                try {
                    return Float.parseFloat(valueStr);
                } catch (NumberFormatException ignored) {
                    return defaultVal;
                }
            }
        }
        return defaultVal;
    }
}
