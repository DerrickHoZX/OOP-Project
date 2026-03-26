package io.github.abstractengine.game.movement;

/**
 * Minimal abstraction for a circular/area "zone" that entities should avoid.
 * The engine doesn't need to know the concrete zone type.
 */
public interface AvoidanceZone {

    /**
     * @return true if the rectangle overlaps the zone (including partial overlap).
     */
    boolean overlapsRectangle(float rectX, float rectY, float rectW, float rectH, float padding);
}

