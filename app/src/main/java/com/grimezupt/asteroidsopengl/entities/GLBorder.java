package com.grimezupt.asteroidsopengl.entities;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.mesh.Mesh;

public class GLBorder extends GLEntity {

    private static final float[] BORDER_COLOR = {1f, 1f, 1f, 1f};

    public GLBorder(final float x, final float y, final float width, final float height) {
        super();
        setColors(BORDER_COLOR[0], BORDER_COLOR[1], BORDER_COLOR[2], BORDER_COLOR[3]);
        _mesh = new Mesh(Mesh.squareGeometry(), GLES20.GL_LINES);
        setAttrs(x, y, width, height);
    }

    public void setAttrs(float x, float y, float width, float height) {
        _x = x;
        _y = y;
        _mesh.setWidthHeight(width, height);
    }

    @Override
    public void update(double dt) {}

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
    }
}
