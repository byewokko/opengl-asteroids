package com.grimezupt.asteroidsopengl.entities;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.utils.Random;

public class Asteroid extends GLEntity {
    private static final float DEFAULT_SCALE = 8;

    public Asteroid(final float x, final float y, final int points) {
        setScale(DEFAULT_SCALE);
        _x = x;
        _y = y;
        final float velocity = Random.between(10f, 15f);
        final float angle = Random.between(0f, (float) (Math.PI * 2f));
        _velX = (float) (Math.cos(angle) * velocity);
        _velY = (float) (Math.sin(angle) * velocity);
        _velW = Random.between(-30f, 30f);
        final float[] vertices = Mesh.regularPolygonGeometry(points);
        _mesh = new Mesh(vertices, GLES20.GL_LINES);
        _mesh.applyAspectRatio();
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
