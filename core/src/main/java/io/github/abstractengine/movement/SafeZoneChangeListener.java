package io.github.abstractengine.movement;

/**
 * Optional listener for movement components that need to react
 * when an "avoid zone" teleports/relocates.
 */
public interface SafeZoneChangeListener {
    void onSafeZoneChanged();
}

