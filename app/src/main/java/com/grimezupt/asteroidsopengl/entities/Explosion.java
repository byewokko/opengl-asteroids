package com.grimezupt.asteroidsopengl.entities;

public class Explosion extends DynamicEntity implements Poolable {
    private boolean _active = false;
    private EntityPool _pool = null;

    @Override
    public boolean isActive() {
        return _active;
    }

    @Override
    public void setPool(EntityPool pool) {
        _pool = pool;
    }

    @Override
    public void suspend() {

    }

    @Override
    public boolean isDangerous(GLEntity that) {
        return true;
    }
}
