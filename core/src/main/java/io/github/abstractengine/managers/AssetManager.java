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
 * Engine-level asset manager for loading and caching audio assets.
 * Game-specific assets are registered externally via registerSound
 * and registerMusic, keeping the engine domain-agnostic.
 */

public class AssetManager {

    private final com.badlogic.gdx.assets.AssetManager gdxAssets = new com.badlogic.gdx.assets.AssetManager();
    private final Map<String, AssetDescriptor<?>> descriptors = new HashMap<>();

    public AssetManager() {
    }

    public void registerSound(String id, String internalPath) {
        descriptors.put(id, new AssetDescriptor<>(internalPath, Sound.class));
    }

    public void registerMusic(String id, String internalPath) {
        descriptors.put(id, new AssetDescriptor<>(internalPath, Music.class));
    }

    public void loadAll(Logging logging) {
        for (Map.Entry<String, AssetDescriptor<?>> e : descriptors.entrySet()) {
            String id = e.getKey();
            AssetDescriptor<?> desc = e.getValue();

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