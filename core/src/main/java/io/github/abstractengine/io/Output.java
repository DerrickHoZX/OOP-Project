package io.github.abstractengine.io;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import io.github.abstractengine.managers.AssetManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Output = Audio output (SFX + Music).
 * Uses AssetManager to retrieve loaded Sound/Music.
 */
public class Output {

    private float volume = 1.0f;        // master volume 0..1
    private boolean mute = false;

    private float sfxVolume = 1.0f;     // 0..1
    private float musicVolume = 1.0f;   // 0..1

    private String currentMusicId = null;

    // Cache handles for currently playing SFX so we can change volume / stop
    private final Map<String, Long> playingSfxHandles = new HashMap<>();

    private final AssetManager assets;
    private final Logging logging;

    public Output(AssetManager assets, Logging logging) {
        this.assets = assets;
        this.logging = logging;
    }

    // ---------- SFX ----------
    public void playSfx(String id) {
        if (mute) return;

        Sound s = assets.getSound(id);
        if (s == null) {
            if (logging != null) logging.warning(LogCategory.AUDIO, "SFX not loaded or unknown id: " + id);
            return;
        }

        float v = clamp01(volume * sfxVolume);
        long handle = s.play(v);
        playingSfxHandles.put(id, handle);

        if (logging != null) logging.info(LogCategory.AUDIO, "Play SFX: " + id);
    }

    /** Stops all SFX currently known by this Output. */
    public void stopSfx() {
        for (Map.Entry<String, Long> e : playingSfxHandles.entrySet()) {
            Sound s = assets.getSound(e.getKey());
            if (s != null) s.stop(e.getValue());
        }
        playingSfxHandles.clear();
        if (logging != null) logging.info(LogCategory.AUDIO, "Stop all SFX");
    }

    public void setSfxVolume(float v) {
        this.sfxVolume = clamp01(v);
        // update current playing handles
        float actual = clamp01(volume * sfxVolume);
        for (Map.Entry<String, Long> e : playingSfxHandles.entrySet()) {
            Sound s = assets.getSound(e.getKey());
            if (s != null) s.setVolume(e.getValue(), mute ? 0f : actual);
        }
        if (logging != null) logging.info(LogCategory.AUDIO, "Set SFX volume=" + this.sfxVolume);
    }

    public float getSfxVolume() { return sfxVolume; }

    // ---------- Music ----------
    public void setMusicVolume(float v) {
        this.musicVolume = clamp01(v);
        applyMusicVolume();
        if (logging != null) logging.info(LogCategory.AUDIO, "Set music volume=" + this.musicVolume);
    }

    public float getMusicVolume() { return musicVolume; }

    public void playMusic(String id, boolean loop) {
        Music m = assets.getMusic(id);
        if (m == null) {
            if (logging != null) logging.warning(LogCategory.AUDIO, "Music not loaded or unknown id: " + id);
            return;
        }

        // stop old
        stopMusic();

        currentMusicId = id;
        m.setLooping(loop);
        applyMusicVolume();
        m.play();

        if (logging != null) logging.info(LogCategory.AUDIO, "Play music: " + id + " loop=" + loop);
    }

    public void stopMusic() {
        if (currentMusicId == null) return;
        Music m = assets.getMusic(currentMusicId);
        if (m != null) {
            m.stop();
        }
        if (logging != null) logging.info(LogCategory.AUDIO, "Stop music: " + currentMusicId);
        currentMusicId = null;
    }

    public void toggleMute() {
        mute = !mute;
        // stop SFX handles immediately when muting
        if (mute) {
            stopSfx();
        }
        applyMusicVolume();
        if (logging != null) logging.info(LogCategory.AUDIO, "Mute toggled: " + mute);
    }

    public void dispose() {
        stopSfx();
        stopMusic();
    }

    private void applyMusicVolume() {
        if (currentMusicId == null) return;
        Music m = assets.getMusic(currentMusicId);
        if (m == null) return;
        float v = mute ? 0f : clamp01(volume * musicVolume);
        m.setVolume(v);
    }

    private float clamp01(float v) {
        if (v < 0f) return 0f;
        if (v > 1f) return 1f;
        return v;
    }
}
