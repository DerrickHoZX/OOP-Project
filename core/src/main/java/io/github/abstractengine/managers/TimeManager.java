package io.github.abstractengine.managers;

import io.github.abstractengine.time.Timer;

/**
 * Manages game timing via a Timer instance.
 * Provides duration-based countdown with start, pause,
 * reset, and elapsed/remaining time queries.
 */

public class TimeManager {
    
    private Timer timer;
    
    public TimeManager() {
        this.timer = new Timer();
    }
    
    // Delegate to Timer
    public void setDuration(float seconds) {
        timer.setDuration(seconds);
    }
    
    public void start() {
        timer.start();
    }
    
    public void pause() {
        timer.pause();
    }
    
    public void reset() {
        timer.reset();
    }
    
    public void update(float dt) {
        timer.update(dt);
    }
    
    public boolean isTimeUp() {
        return timer.isTimeUp();
    }
    
    public float getElapsedTime() {
        return timer.getElapsedTime();
    }
    
    public float getRemainingTime() {
        return timer.getRemainingTime();
    }
}