package com.grimezupt.asteroidsopengl.mesh;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.Point3D;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Mesh {
    private static final String TAG = "Mesh";
    public static final int SIZE_OF_FLOAT = Float.SIZE / Byte.SIZE;
    public static final int COORDS_PER_VERTEX = 3;
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * SIZE_OF_FLOAT;

    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;

    public FloatBuffer _vertexBuffer = null;
    public int _vertexCount = 0;
    public int _drawMode = GLES20.GL_TRIANGLES;

    public Point3D _min = new Point3D();
    public Point3D _max = new Point3D();
    public float _width = 0f;
    public float _height = 0f;
    public float _depth = 0f;
    public double _radius = 0f;

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
        _vertexCount = geometry.length / COORDS_PER_VERTEX;
    }

    private void setDrawMode(int drawMode) {
        assert(drawMode == GLES20.GL_TRIANGLES ||
                drawMode == GLES20.GL_LINES ||
                drawMode == GLES20.GL_POINTS);
        _drawMode = drawMode;
    }

    public void updateBounds(){
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;
        for (int i = 0; i < _vertexCount*COORDS_PER_VERTEX; i+=COORDS_PER_VERTEX){
            final float x = _vertexBuffer.get(i+X);
            final float y = _vertexBuffer.get(i+Y);
            final float z = _vertexBuffer.get(i+Z);
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            maxZ = Math.max(maxZ, z);
        }
        _min.set(minX, minY, minZ);
        _max.set(maxX, maxY, maxZ);
        _width = maxX - minX;
        _height = maxY - minY;
        _depth = maxZ - minZ;
        _radius = Math.max(_depth, Math.max(_width, _height)) * 0.5;
    }

    public float left(){
        return _min._x;
    }
    public float right(){
        return _max._x;
    }
    public float bottom(){
        return _min._y;
    }
    public float top(){
        return _max._y;
    }
    public float centerX(){
        return _min._x + _width * 0.5f;
    }
    public float centerY(){
        return _min._y + _height * 0.5f;
    }
}
