package io.github.some_example_name.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.some_example_name.interfaces.InputDevice;

public class Keyboard implements InputDevice {

    @Override
    public void update() {
        // If you later want to cache state per-frame, do it here.
    }

    // True while the key is held down.
    public boolean isPressed(KeyCode key) {
        switch (key) {
            case ESCAPE: return Gdx.input.isKeyPressed(Input.Keys.ESCAPE);
            case ENTER:  return Gdx.input.isKeyPressed(Input.Keys.ENTER);
            case SPACE:  return Gdx.input.isKeyPressed(Input.Keys.SPACE);

            case W:      return Gdx.input.isKeyPressed(Input.Keys.W);
            case A:      return Gdx.input.isKeyPressed(Input.Keys.A);
            case S:      return Gdx.input.isKeyPressed(Input.Keys.S);
            case D:      return Gdx.input.isKeyPressed(Input.Keys.D);

            case UP:     return Gdx.input.isKeyPressed(Input.Keys.UP);
            case DOWN:   return Gdx.input.isKeyPressed(Input.Keys.DOWN);
            case LEFT:   return Gdx.input.isKeyPressed(Input.Keys.LEFT);
            case RIGHT:  return Gdx.input.isKeyPressed(Input.Keys.RIGHT);

            default:     return false;
        }
    }

    // True only on the frame the key was pressed.
    public boolean isJustPressed(KeyCode key) {
        switch (key) {
            case ESCAPE: return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
            case ENTER:  return Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
            case SPACE:  return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

            case W:      return Gdx.input.isKeyJustPressed(Input.Keys.W);
            case A:      return Gdx.input.isKeyJustPressed(Input.Keys.A);
            case S:      return Gdx.input.isKeyJustPressed(Input.Keys.S);
            case D:      return Gdx.input.isKeyJustPressed(Input.Keys.D);

            case UP:     return Gdx.input.isKeyJustPressed(Input.Keys.UP);
            case DOWN:   return Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
            case LEFT:   return Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
            case RIGHT:  return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);

            default:     return false;
        }
    }
}
