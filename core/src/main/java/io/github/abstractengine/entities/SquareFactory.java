package io.github.abstractengine.entities;

public class SquareFactory implements EntityFactory<Square> {
    @Override
    public Square createEntity(float x, float y) {
        return new Square(x, y, 160f, 70f);
    }
}