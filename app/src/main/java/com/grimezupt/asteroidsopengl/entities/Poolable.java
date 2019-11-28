package com.grimezupt.asteroidsopengl.entities;

public interface Poolable {
    boolean isSuspended(); // TODO: refactor to isActive
    void onSuspend();
}
