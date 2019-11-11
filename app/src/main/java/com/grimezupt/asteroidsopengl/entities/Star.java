package com.grimezupt.asteroidsopengl.entities;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.utils.Random;

public class Star extends GLEntity {
    private static Mesh mesh = null; // pool
    public Star(final float x, final float y) {
        super();
        _x = x;
        _y = y;
        if (mesh == null){
            final float[] vertices = new float[Mesh.COORDS_PER_VERTEX];
            mesh = new Mesh(vertices, GLES20.GL_POINTS);
        }
        _mesh = mesh; // all stars use the same mesh
    }

    public static Star random(float xRange, float yRange) {
        return new Star(Random.between(0, xRange), Random.between(0, yRange));
    }
}
