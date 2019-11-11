package com.grimezupt.asteroidsopengl.entities;

import android.opengl.Matrix;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.Game;
import com.grimezupt.asteroidsopengl.GLManager;
import com.grimezupt.asteroidsopengl.mesh.Mesh;

import java.util.Objects;

public class GLEntity {
    public static Game _game = null;
    public static final float[] modelMatrix = new float[4*4];
    public static final float[] viewportModelMatrix = new float[4*4];
    public static final float[] rotationViewportModelMatrix = new float[4*4];

    Mesh _mesh = null;
    private float[] _color = new float[4];
    public float _x = 0f;
    public float _y = 0f;
    public float _depth = 0f;
    public float _scale = 1f;
    public float _rotation = 0f;
    public float _velX = 0f;
    public float _velY = 0f;
    public float _velW = 0f; //angular velocity

    public GLEntity() {
        setColors(Config.Colors.FOREGROUND);
    }

    public void update(double dt){
        _x += _velX * dt;
        _y += _velY * dt;
        _rotation += _velW * dt;
    }

    public void render(float[] viewportMatrix) {
        final int OFFSET = 0;
        Matrix.setIdentityM(modelMatrix, OFFSET);
        Matrix.translateM(modelMatrix, OFFSET, _x, _y, _depth);
        Matrix.multiplyMM(viewportModelMatrix, OFFSET,
                viewportMatrix, OFFSET, modelMatrix, OFFSET);
        Matrix.setRotateM(modelMatrix, OFFSET, _rotation, 0f, 0f, 1f);
        Matrix.scaleM(modelMatrix, OFFSET, _scale, _scale, 1f);
        Matrix.multiplyMM(rotationViewportModelMatrix, OFFSET,
                viewportModelMatrix, OFFSET, modelMatrix, OFFSET);
        GLManager.draw(_mesh, rotationViewportModelMatrix, _color);
    }

    public void onCollision(final GLEntity that){}

    public void setColors(final float[] colors){
        Objects.requireNonNull(colors);
        assert(colors.length >= 4);
        setColors(colors[0], colors[1], colors[2], colors[3]);
    }

    void setColors(final float r, final float g, final float b, final float a) {
        _color[0] = r;
        _color[1] = g;
        _color[2] = b;
        _color[3] = a;
    }
}
