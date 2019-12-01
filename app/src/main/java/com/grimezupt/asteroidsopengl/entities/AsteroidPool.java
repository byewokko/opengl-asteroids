package com.grimezupt.asteroidsopengl.entities;

public class AsteroidPool extends EntityPool<Asteroid> {

    public AsteroidPool() {
        super(EntityPool.DYNAMIC_SIZE);
    }

    @Override
    Asteroid createNew() {
        Asteroid a = new Asteroid();
        return a;
    }

    @Override
    public void update(double dt) {
        super.update(dt);
    }
}
