package io.github.abstractengine.game.entities;

import io.github.abstractengine.entities.EntityFactory;

public class SquareFactory implements EntityFactory<Square> {
    @Override
    public Square createEntity(float x, float y) {
        return new Square(x, y, 160f, 70f);
    }
}