package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.utils.Random;
import com.grimezupt.asteroidsopengl.utils.Utils;

public class ExplosionPool extends EntityPool<ExplosionParticle> {

    public static final int BIG_EXPLOSION = 4;
    public static final int MEDIUM_EXPLOSION = 2;
    public static final int SMALL_EXPLOSION = 1;
    public static final float SCATTER = 5f;
    public static final int PARTICLES_PER_EXPLOSION = 15;
    public static final int DEFAULT_POOL_SIZE = 60;

    public ExplosionPool() {
        super(EntityPool.DYNAMIC_SIZE);
    }

    @Override
    ExplosionParticle createNew() {
        return new ExplosionParticle();
    }

    public void makeExplosion(float x, float y, int size) {
        final float scatter = (size - 1) * SCATTER;
        ExplosionParticle particle;
        for (int i = 0; i < size; i++) {
            final float xi = Random.clampnorm(x, scatter*0.5f, scatter);
            final float yi = Random.clampnorm(y, scatter*0.5f, scatter);
            for (int j = 0; j < PARTICLES_PER_EXPLOSION; j++) {
                particle = pull();
                if (particle != null) {
                    particle.activate(xi, yi, Random.between(0f, (float) (Math.PI * 2f)));
                }
            }
        }
    }

    public void makeExplosion(GLEntity a, GLEntity b, int size) {
        makeExplosion(Utils.weightedAvg2(a._x, a.radius(), b._x, b.radius()),
                Utils.weightedAvg2(a._y, a.radius(), b._y, b.radius()),
                size);
    }

    public void init() {
        super.init(DEFAULT_POOL_SIZE);
    }

    @Override
    public void update(double dt) {
        for (ExplosionParticle p :
                _activeEntities) {
            p.update(dt);
        }
    }
}
