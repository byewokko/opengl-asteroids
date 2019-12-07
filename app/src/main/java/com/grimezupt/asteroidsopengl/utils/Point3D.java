package com.grimezupt.asteroidsopengl.utils;

public class Point3D {
    public float _x = 0f;
    public float _y = 0f;
    public float _z = 0f;

    public Point3D() {
    }

    public Point3D(float x, float y, float z) {
        set(x, y, z);
    }

    public Point3D(final float[] point) {
        set(point);
    }

    public void set(float x, float y, float z) {
        _x = x;
        _y = y;
        _z = z;
    }

    public void set(final float[] point){
        set(point[0], point[1], point[2]);
    }

    public final float distanceL1(Point3D that){
        final float dx = this._x - that._x;
        final float dy = this._y - that._y;
        final float dz = this._z - that._z;
        return Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
    }

    public final float distanceSquared(Point3D that){
        final float dx = this._x - that._x;
        final float dy = this._y - that._y;
        final float dz = this._z - that._z;
        return dx*dx + dy*dy + dz*dz;
    }

    public final float distance(Point3D that){
        return (float) Math.sqrt(distanceSquared(that));
    }
}
