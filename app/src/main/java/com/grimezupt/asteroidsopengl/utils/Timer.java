package com.grimezupt.asteroidsopengl.utils;

import android.util.Log;

import java.util.ArrayList;

public class Timer {
    private static final String TAG = "Timer";
    private static final int POOL_SIZE = 10;
    private double _clock = 0d;
    private final ArrayList<TimerEvent> _activeEvents = new ArrayList<>();
    private final ArrayList<TimerEvent> _inactiveEvents = new ArrayList<>();
    private final ArrayList<TimerEvent> _eventsToRemove = new ArrayList<>();

    public Timer() {
        for (int i = 0; i < POOL_SIZE; i++) {
            _inactiveEvents.add(new TimerEvent());
        }
    }

    public void update(double dt){
        _clock += dt;
        for (TimerEvent e :
                _activeEvents) {
            if (e.isDue(_clock)){
                e.trigger();
                _eventsToRemove.add(e);
            }
        }
        _activeEvents.removeAll(_eventsToRemove);
        _inactiveEvents.addAll(_eventsToRemove);
        _eventsToRemove.clear();
    }

    public int setEvent(TimerListener listener,int event, double time){
        final TimerEvent e;
        if (_inactiveEvents.isEmpty()){
            e = new TimerEvent();
            Log.d(TAG, "Event pool empty, creating new!");
        } else {
            e = _inactiveEvents.remove(0);
        }
        e.set(listener, event, _clock + time);
        _activeEvents.add(e);
        return e.id;
    }

    public double getClock() {
        return _clock;
    }
}
