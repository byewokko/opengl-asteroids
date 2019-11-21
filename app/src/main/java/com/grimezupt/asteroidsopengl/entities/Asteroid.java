package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.mesh.GLEquiPolygon;
import com.grimezupt.asteroidsopengl.utils.Random;

public class Asteroid extends GLEntity {
    private static final float DEFAULT_RADIUS = 10f;
    private float _radius = DEFAULT_RADIUS;

    public Asteroid(final float x, final float y, final int points, final float radius) {
        _x = x;
        _y = y;
        final float velocity = Random.between(10f, 15f);
        final float angle = Random.between(0f, (float) (Math.PI * 2f));
        _velX = (float) (Math.cos(angle) * velocity);
        _velY = (float) (Math.sin(angle) * velocity);
        _velW = Random.between(-30f, 30f);
        _radius = radius;
        _mesh = GLEquiPolygon.build(points, radius);
    }

    public Asteroid(final float x, final float y, final int points){
        this(x, y, points, DEFAULT_RADIUS);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
//        wrap();
    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
    }
}
