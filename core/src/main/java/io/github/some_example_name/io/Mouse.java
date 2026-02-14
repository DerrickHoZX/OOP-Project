package io.github.some_example_name.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.some_example_name.interfaces.InputDevice;

public class Mouse implements InputDevice {
	

    @Override
    public void update() {}

    public boolean isJustPressed(MouseButton button) {
        switch (button) {
            case LEFT:   return Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
            case RIGHT:  return Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT);
            case MIDDLE: return Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE);
            default:     return false;
        }
    }
}
