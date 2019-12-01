package com.grimezupt.asteroidsopengl.entities;

public interface Poolable {
    boolean isActive();
    void setPool(EntityPool pool);
    void suspend();
}
