package io.github.abstractengine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import io.github.abstractengine.io.LogCategory;
import io.github.abstractengine.io.Logging;

import java.util.HashMap;
import java.util.Map;

/**
 * Project AssetManager (NOT LibGDX's class).
 * Holds IDs -> file paths and loads audio assets safely (skips missing files).
 *
 * You can extend this later to load textures/images.
 */
public class AssetManager {

    // ---- Audio IDs (used by scenes / collision rules) ----
    public static final String MUSIC_MAIN_MENU = "music.mainmenu";
    public static final String MUSIC_START_SCENE = "music.startscene";
    public static final String MUSIC_END_SCENE = "music.endscene";

    public static final String SFX_OVER = "sfx.over";
    public static final String SFX_SPEED_BOOST = "sfx.speedboost";

    // ---- Default file paths (put these files under your project's assets/ folder) ----
    // Example structure: assets/audio/...
    public static final String PATH_MUSIC_MAIN_MENU = "audio/mainmenu.wav";
    public static final String PATH_MUSIC_START_SCENE = "audio/startscene.wav";
    public static final String PATH_MUSIC_END_SCENE = "audio/endscene.wav";

    public static final String PATH_SFX_END = "audio/end.wav";
    public static final String PATH_SFX_SPEED_BOOST = "audio/speedboost.wav";

    private final com.badlogic.gdx.assets.AssetManager gdxAssets = new com.badlogic.gdx.assets.AssetManager();
    private final Map<String, AssetDescriptor<?>> descriptors = new HashMap<>();

    public AssetManager() {
        // Register default audio descriptors
        registerMusic(MUSIC_MAIN_MENU, PATH_MUSIC_MAIN_MENU);
        registerMusic(MUSIC_START_SCENE, PATH_MUSIC_START_SCENE);
        registerMusic(MUSIC_END_SCENE, PATH_MUSIC_END_SCENE);

        registerSound(SFX_OVER, PATH_SFX_END);
        registerSound(SFX_SPEED_BOOST, PATH_SFX_SPEED_BOOST);
    }

    public void registerSound(String id, String internalPath) {
        descriptors.put(id, new AssetDescriptor<>(internalPath, Sound.class));
    }

    public void registerMusic(String id, String internalPath) {
        descriptors.put(id, new AssetDescriptor<>(internalPath, Music.class));
    }

    /** Loads all registered audio assets. Missing files are skipped (and logged). */
    public void loadAll(Logging logging) {
        for (Map.Entry<String, AssetDescriptor<?>> e : descriptors.entrySet()) {
            String id = e.getKey();
            AssetDescriptor<?> desc = e.getValue();

            // Skip if file missing to avoid crashing during finishLoading()
            if (!Gdx.files.internal(desc.fileName).exists()) {
                if (logging != null) {
                    logging.warning(LogCategory.AUDIO, "Missing asset file (skipped): " + desc.fileName + " for id=" + id);
                }
                continue;
            }

            if (!gdxAssets.isLoaded(desc.fileName)) {
                gdxAssets.load(desc);
            }
        }

        // This blocks until loaded; OK for small projects. Replace with async later if needed.
        gdxAssets.finishLoading();

        if (logging != null) {
            logging.info(LogCategory.AUDIO, "Audio assets loaded. Count=" + gdxAssets.getLoadedAssets());
        }
    }

    public boolean has(String id) {
        AssetDescriptor<?> desc = descriptors.get(id);
        if (desc == null) return false;
        return gdxAssets.isLoaded(desc.fileName);
    }

    public Sound getSound(String id) {
        AssetDescriptor<?> desc = descriptors.get(id);
        if (desc == null) return null;
        if (!gdxAssets.isLoaded(desc.fileName)) return null;
        return (Sound) gdxAssets.get(desc.fileName, Sound.class);
    }

    public Music getMusic(String id) {
        AssetDescriptor<?> desc = descriptors.get(id);
        if (desc == null) return null;
        if (!gdxAssets.isLoaded(desc.fileName)) return null;
        return (Music) gdxAssets.get(desc.fileName, Music.class);
    }

    public void dispose() {
        gdxAssets.dispose();
        descriptors.clear();
    }
}
