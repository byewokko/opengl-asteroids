package com.grimezupt.asteroidsopengl.entities;

public class Explosion extends GLEntity implements Poolable {
    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
    public void onSuspend() {

    }
}
