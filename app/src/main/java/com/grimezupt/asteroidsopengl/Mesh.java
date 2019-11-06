package com.grimezupt.asteroidsopengl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Mesh {
    private static final String TAG = "Mesh";
    public static final int SIZE_OF_FLOAT = Float.SIZE / Byte.SIZE;
    public static final int COORDS_PER_VERTEX = 3;
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * SIZE_OF_FLOAT;

    public FloatBuffer _vertexBuffer = null;
    public int _vertexCount = 0;
    public int _drawMode = GLES20.GL_TRIANGLES;

    public Mesh(final float[] geometry) {
        init(geometry, GLES20.GL_TRIANGLES);
    }

    public Mesh(final float[] geometry, final int drawMode) {
        init(geometry, drawMode);
    }

    private void init(final float[] geometry, final int drawMode) {
        setVertices(geometry);
        setDrawMode(drawMode);
    }

    private void setVertices(float[] geometry) {
        _vertexBuffer = ByteBuffer.allocateDirect(geometry.length * SIZE_OF_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        _vertexBuffer.put(geometry);
        _vertexBuffer.position(0);
        _vertexCount = geometry.length;
    }

    private void setDrawMode(int drawMode) {
        assert(drawMode == GLES20.GL_TRIANGLES ||
                drawMode == GLES20.GL_LINES ||
                drawMode == GLES20.GL_POINTS);
        _drawMode = drawMode;
    }
}
