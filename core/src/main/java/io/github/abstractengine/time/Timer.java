package io.github.abstractengine.time;

public class Timer {
    
    private float elapsedTime;
    private float duration;
    private boolean running;
    
    public Timer() {
        this.elapsedTime = 0f;
        this.duration = 0f;
        this.running = false;
    }
    
    public void setDuration(float seconds) {
        this.duration = seconds;
    }
    
    public void start() {
        this.running = true;
        this.elapsedTime = 0f;
    }
    
    public void pause() {
        this.running = false;
    }
    
    public void reset() {
        this.elapsedTime = 0f;
        this.running = false;
    }
    
    public void update(float dt) {
        if (running) {
            elapsedTime += dt;
        }
    }
    
    public boolean isTimeUp() {
        return elapsedTime >= duration;
    }
    
    public float getElapsedTime() {
        return elapsedTime;
    }
    
    public float getRemainingTime() {
        float remaining = duration - elapsedTime;
        return Math.max(0, remaining); // Never return negative
    }
}