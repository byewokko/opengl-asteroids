package com.grimezupt.asteroidsopengl.utils;

import android.util.Log;
import android.util.SparseArray;

import com.grimezupt.asteroidsopengl.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

public class Timer {
    private static final String TAG = "Timer";
    private static final int POOL_SIZE = 10;
    private double _clock = 0d;
    private final ArrayList<TimerEvent> _activeEvents = new ArrayList<>();
    private final ArrayList<Integer> _activeE = new ArrayList<>();
    private final ArrayList<TimerEvent> _inactiveEvents = new ArrayList<>();
    private final HashSet<TimerEvent> _eventsToRemove = new HashSet<>();

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
//                Log.d(TAG, "active: " + _activeEvents.size() + ", suspended: "
//                        + _inactiveEvents.size() + ", to add: " + _eventsToRemove.size());
            }
        }
        _activeEvents.removeAll(_eventsToRemove);
        _inactiveEvents.addAll(_eventsToRemove);
        _eventsToRemove.clear();
    }

    public int setEvent(TimerListener listener, int event, double time){
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

    public void cancelEventsOfListener(TimerListener listener){
        for (TimerEvent e :
                _activeEvents) {
            if (e.listener == listener){
                _eventsToRemove.add(e);
            }
        }
    }
}
