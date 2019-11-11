package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.World;
import com.grimezupt.asteroidsopengl.mesh.GLEquiPolygon;
import com.grimezupt.asteroidsopengl.mesh.Triangle;
import com.grimezupt.asteroidsopengl.utils.Random;

public class Asteroid extends GLEntity {
    private static final float DEFAULT_RADIUS = 10f;
    private float _radius = DEFAULT_RADIUS;

    public Asteroid(final float x, final float y, final int points, final float radius) {
        _x = x;
        _y = y;
        _velX = Random.between(0, 10f);
        _velY = Random.between(0, 10f);
        _radius = radius;
        _mesh = GLEquiPolygon.build(points, radius);
    }

    public Asteroid(final float x, final float y, final int points){
        this(x, y, points, DEFAULT_RADIUS);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        wrap();
    }

    private void wrap() {
        _x %= World.WIDTH;
        _y %= World.HEIGHT;
    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
    }
}
