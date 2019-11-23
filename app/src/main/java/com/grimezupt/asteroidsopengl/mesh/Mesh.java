package com.grimezupt.asteroidsopengl.mesh;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.Point3D;
import com.grimezupt.asteroidsopengl.utils.Utils;

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
    public float _aspectWidth = 1f;
    public float _aspectHeight = 1f;
    public float _aspectDepth = 1f;
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
        normalize();
    }

    private void setVertices(float[] geometry) {
        _vertexBuffer = ByteBuffer.allocateDirect(geometry.length * SIZE_OF_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        _vertexBuffer.put(geometry);
        _vertexBuffer.position(0);
        _vertexCount = geometry.length / COORDS_PER_VERTEX;
        updateBounds();
        saveAspectRatio();
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

    public void normalize(){
        final double inverseW = (_width  == 0.0) ? 0.0 : 1/_width;
        final double inverseH = (_height == 0.0) ? 0.0 : 1/_height;
        final double inverseD = (_depth  == 0.0) ? 0.0 : 1/_depth;
        for (int i = 0; i < _vertexCount * COORDS_PER_VERTEX; i += COORDS_PER_VERTEX) {
            final double dx = _vertexBuffer.get(i + X) - _min._x;
            final double dy = _vertexBuffer.get(i + Y) - _min._y;
            final double dz = _vertexBuffer.get(i + Z) - _min._z;
            final double xNorm = 2.0 * (dx * inverseW) - 1.0;
            final double yNorm = 2.0 * (dy * inverseH) - 1.0;
            final double zNorm = 2.0 * (dz * inverseD) - 1.0;
            _vertexBuffer.put(i+X, (float)xNorm);
            _vertexBuffer.put(i+Y, (float)yNorm);
            _vertexBuffer.put(i+Z, (float)zNorm);
        }
        updateBounds();
        Utils.require(_width <= 2f, "x-axis out of norm-bounds");
        Utils.require(_height <= 2f, "y-axis out of norm-bounds");
        Utils.require(_depth <= 2f, "z-axis out of norm-bounds");
    }

    public void scale(final double xFactor, final double yFactor, final double zFactor){
        for(int i = 0; i < _vertexCount*COORDS_PER_VERTEX; i+=COORDS_PER_VERTEX) {
            _vertexBuffer.put(i+X, (float)(_vertexBuffer.get(i+X) * xFactor));
            _vertexBuffer.put(i+Y, (float)(_vertexBuffer.get(i+Y) * yFactor));
            _vertexBuffer.put(i+Z, (float)(_vertexBuffer.get(i+Z) * zFactor));
        }
        updateBounds();
    }

    public void scale(final double factor){
        scale(factor, factor, factor);
    }

    public void setWidthHeight(final double w, final double h){
        normalize();
        scale(w*0.5, h*0.5, 1.0);
        Utils.require(Math.abs(w-_width) < Float.MIN_NORMAL && Math.abs(h-_height) < Float.MIN_NORMAL,
                "incorrect width / height after scaling!");
    }

    private void rotate(final int axis, final double theta) {
        Utils.require(axis == X || axis == Y || axis == Z);
        final double sinTheta = Math.sin(theta);
        final double cosTheta = Math.cos(theta);
        for (int i = 0; i < _vertexCount * COORDS_PER_VERTEX; i += COORDS_PER_VERTEX) {
            final double x = _vertexBuffer.get(i + X);
            final double y = _vertexBuffer.get(i + Y);
            final double z = _vertexBuffer.get(i + Z);
            if (axis == Z) {
                _vertexBuffer.put(i + X, (float) (x * cosTheta - y * sinTheta));
                _vertexBuffer.put(i + Y, (float) (y * cosTheta + x * sinTheta));
            } else if (axis == Y) {
                _vertexBuffer.put(i + X, (float) (x * cosTheta - z * sinTheta));
                _vertexBuffer.put(i + Z, (float) (z * cosTheta + x * sinTheta));
            } else if (axis == X) {
                _vertexBuffer.put(i + Y, (float) (y * cosTheta - z * sinTheta));
                _vertexBuffer.put(i + Z, (float) (z * cosTheta + y * sinTheta));
            }
        }
        updateBounds();
    }

    public void saveAspectRatio() {
        final float maxDim = Math.max(_width, Math.max(_height, _depth));
        if (maxDim != 0f) {
            _aspectWidth = _width/maxDim;
            _aspectHeight = _height/maxDim;
            _aspectDepth = _depth/maxDim;
        }
    }

    public void applyAspectRatio(){
        scale(_aspectWidth, _aspectHeight, _aspectDepth);
        updateBounds();
    }

    public static float[] regularPolygonGeometry(int points){
        Utils.require(points >= 3, "At least 3 points required.");
        final int numVerts = points*2;
        final float[] geometry = new float[numVerts * Mesh.COORDS_PER_VERTEX];
        double step = 2.0*Math.PI/points;
        int i = 0, point = 0;
        while(point < points) { //generate verts on circle, 2 per point
            double theta = point * step;
            geometry[i++] = (float)(Math.cos(theta)); //X
            geometry[i++] = (float)(Math.sin(theta)); //Y
            geometry[i++] = 0f;                                //Z
            point++;
            theta = point * step;
            geometry[i++] = (float)(Math.cos(theta)); //X
            geometry[i++] = (float)(Math.sin(theta)); //Y
            geometry[i++] = 0f;                                //Z
        }
        return geometry;
    }

    public static float[] squareGeometry(){
        float[] vertices = new float[]{
                1, 1, 0,
                -1, 1, 0,
                -1, 1, 0,
                -1, -1, 0,
                -1, -1, 0,
                1, -1, 0,
                1, -1, 0,
                1, 1, 0
        };
        return vertices;
    }

    public void setOrigin(final float x, final float y, final float z){
        Utils.require(x >= -1f && x <= 1f && y >= -1f && y <= 1f && z >= -1f && z <= 1f,
                "Origin must be between -1 and 1.");
        for (int i = 0; i < _vertexCount * COORDS_PER_VERTEX; i += COORDS_PER_VERTEX) {
            final double nx = _vertexBuffer.get(i + X) - x;
            final double ny = _vertexBuffer.get(i + Y) - y;
            final double nz = _vertexBuffer.get(i + Z) - z;
            _vertexBuffer.put(i+X, (float)nx);
            _vertexBuffer.put(i+Y, (float)ny);
            _vertexBuffer.put(i+Z, (float)nz);
        }
        updateBounds();
    }
}
