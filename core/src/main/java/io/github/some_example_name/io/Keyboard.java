package io.github.some_example_name.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.some_example_name.interfaces.InputDevice;

public class Keyboard implements InputDevice {

    @Override
    public void update() {}

    public boolean isJustPressed(KeyCode key) {
        switch (key) {
            case ESCAPE:
                return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
            case ENTER:
                return Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
            case SPACE:
                return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
            default:
                return false;
        }
    }
}