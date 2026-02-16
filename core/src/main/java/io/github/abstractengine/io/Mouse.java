package io.github.abstractengine.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import io.github.abstractengine.interfaces.InputDevice;

public class Mouse implements InputDevice {

    @Override
    public void update() {
        
    }

    public int getX() {
        return Gdx.input.getX();
    }

    public int getY() {
        return Gdx.input.getY();
    }

    // True while button is held down. 
    public boolean isPressed(MouseButton button) {
        switch (button) {
            case LEFT:   return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
            case RIGHT:  return Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
            case MIDDLE: return Gdx.input.isButtonPressed(Input.Buttons.MIDDLE);
            default:     return false;
        }
    }

    // True only on the frame the button was pressed.
    public boolean isJustPressed(MouseButton button) {
        switch (button) {
            case LEFT:   return Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
            case RIGHT:  return Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT);
            case MIDDLE: return Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE);
            default:     return false;
        }
    }
}
