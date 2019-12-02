package com.grimezupt.asteroidsopengl.utils;

public class TimerEvent {
    private static int counter = 0;
    public TimerListener listener;
    public int id;
    public int type;
    public double time;

    public TimerEvent() {
        id = counter++;
    }

    public TimerEvent(TimerListener listener, int type, double time) {
        id = counter++;
        set(listener, type, time);
    }

    public void set(TimerListener listener, int type, double time) {
        this.listener = listener;
        this.type = type;
        this.time = time;
    }

    public boolean isDue(double time){
        return time > this.time;
    }

    public double remaining(double time){
        return this.time - time;
    }

    public void trigger(){
        listener.onTimerEvent(type);
    }
}
