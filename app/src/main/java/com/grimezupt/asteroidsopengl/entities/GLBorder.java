package com.grimezupt.asteroidsopengl.entities;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.Mesh;

public class GLBorder extends GLEntity {

    private static final float[] BORDER_COLOR = {1f, 1f, 1f, 1f};
    private final float _width;
    private final float _height;
    private final float[] _borderVertices;

    public GLBorder(final float x, final float y, final float width, final float height) {
        super();
        _x = x;
        _y = y;
        _width = width;
        _height = height;
        setColors(BORDER_COLOR[0], BORDER_COLOR[1], BORDER_COLOR[2], BORDER_COLOR[3]);
        _borderVertices = new float[]{
                // Line from point 1 to point 2
                0, 0, 0,
                _width, 0, 0,
                // Point 2 to point 3
                _width, 0, 0,
                _width, _height, 0,
                // Point 3 to point 4
                _width, _height, 0,
                0, _height, 0,
                // Point 4 to point 1
                0, _height, 0,
                0, 0, 0
        };
        _mesh = new Mesh(_borderVertices, GLES20.GL_LINES);
    }
}
