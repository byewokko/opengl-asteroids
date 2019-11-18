package com.grimezupt.asteroidsopengl.entities;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.GLManager;
import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.utils.Random;

public class Star extends GLEntity {
    private static final float STAR_SIZE = 5f;
    private static Mesh mesh = null; // pool
    private float _size = STAR_SIZE;

    public Star(final float x, final float y, final float size) {
        super();
        _x = x;
        _y = y;
        _size = size;
        if (mesh == null){
            final float[] vertices = new float[Mesh.COORDS_PER_VERTEX];
            mesh = new Mesh(vertices, GLES20.GL_POINTS);
        }
        _mesh = mesh; // all stars use the same mesh
    }

    @Override
    public void render(float[] viewportMatrix) {
        configureMatrix(viewportMatrix);
        GLManager.draw(_mesh, rotationViewportModelMatrix, _color, _size);
    }

    public static Star random(float xRange, float yRange) {
        return new Star(Random.between(0, xRange), Random.between(0, yRange), Random.between(0, STAR_SIZE));
    }
}
