package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.utils.Utils;

public class ExplosionPool extends EntityPool<Explosion> {

    public static final int BIG_EXPLOSION = 1;
    public static final int SMALL_EXPLOSION = 0;

    public ExplosionPool() {
        super(EntityPool.DYNAMIC_SIZE);
    }

    @Override
    Explosion createNew() {
        return new Explosion();
    }

    public void makeExplosion(float x, float y, int size) {
        // TODO!
    }

    public void makeExplosion(GLEntity p, GLEntity a, int size) {
        makeExplosion(Utils.weightedAvg2(p._x, p.radius(), a._x, a.radius()),
                Utils.weightedAvg2(p._y, p.radius(), a._y, a.radius()),
                size);
    }
}
