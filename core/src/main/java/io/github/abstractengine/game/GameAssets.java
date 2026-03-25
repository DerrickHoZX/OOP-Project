package io.github.abstractengine.game;

import io.github.abstractengine.managers.AssetManager;

/**
 * Registers all game-specific audio assets with the engine's AssetManager.
 */
public class GameAssets {

    // Audio IDs
    public static final String MUSIC_MAIN_MENU = "music.mainmenu";
    public static final String MUSIC_START_SCENE = "music.startscene";
    public static final String MUSIC_END_SCENE = "music.endscene";

    public static final String SFX_OVER = "sfx.over";
    public static final String SFX_SPEED_BOOST = "sfx.speedboost";

    public static void registerAll(AssetManager assets) {
        assets.registerMusic(MUSIC_MAIN_MENU, "audio/mainmenu.wav");
        assets.registerMusic(MUSIC_START_SCENE, "audio/startscene.wav");
        assets.registerMusic(MUSIC_END_SCENE, "audio/endscene.wav");

        assets.registerSound(SFX_OVER, "audio/end.wav");
        assets.registerSound(SFX_SPEED_BOOST, "audio/speedboost.wav");
    }
}