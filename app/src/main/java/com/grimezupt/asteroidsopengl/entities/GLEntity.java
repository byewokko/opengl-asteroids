package com.grimezupt.asteroidsopengl.entities;

import android.graphics.PointF;
import android.opengl.Matrix;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.GLManager;
import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.utils.Utils;

import java.util.Objects;

public class GLEntity extends Entity {
    public static final float RADIANS = (float) (Math.PI / 180f);
    public static final float[] modelMatrix = new float[4*4];
    public static final float[] viewportModelMatrix = new float[4*4];
    public static final float[] rotationViewportModelMatrix = new float[4*4];

    Mesh _mesh = null;
    float[] _color = new float[4];
    public float _x = 0f;
    public float _y = 0f;
    public float _width = 0f;
    public float _height = 0f;
    public float _depth = 0f;
    public float _scale = 1f;
    public float _rotation = 0f;
    public float _velX = 0f;
    public float _velY = 0f;
    public float _velW = 0f; //angular velocity

    public float _collisionRadius = 0f;

    public GLEntity() {
        setColors(Config.Colors.FOREGROUND);
    }

    @Override
    public void update(double dt){
        _x += _velX * dt;
        _y += _velY * dt;
        _rotation += _velW * dt;
        wrap();
        //TODO: set _width and _height here
    }

    void wrap() {
        if (left() > World.WIDTH){
            setRight(0);
        } else if (right() < 0){
            setLeft(World.WIDTH);
        }
        if (top() > World.HEIGHT){
            setBottom(0);
        } else if (bottom() < 0){
            setTop(World.HEIGHT);
        }
    }

    @Override
    public void render(float[] viewportMatrix) {
        configureMatrix(viewportMatrix);
        GLManager.draw(_mesh, rotationViewportModelMatrix, _color);
    }

    public void configureMatrix(float[] viewportMatrix) {
        final int OFFSET = 0;
        Matrix.setIdentityM(modelMatrix, OFFSET);
        Matrix.translateM(modelMatrix, OFFSET, _x, _y, _depth);
        Matrix.multiplyMM(viewportModelMatrix, OFFSET,
                viewportMatrix, OFFSET, modelMatrix, OFFSET);
        Matrix.setRotateM(modelMatrix, OFFSET, _rotation, 0f, 0f, 1f);
        Matrix.scaleM(modelMatrix, OFFSET, _scale, _scale, 1f);
        Matrix.multiplyMM(rotationViewportModelMatrix, OFFSET,
                viewportModelMatrix, OFFSET, modelMatrix, OFFSET);
    }

    public boolean isColliding(final GLEntity that){
        if (this == that){
            throw new AssertionError("Can't test entity against itself.");
        }
        return isAABBOverlapping(this, that);
    }

    public float centerX() {
        return _x; //assumes our mesh has been centered on [0,0] (normalized)
    }

    public float centerY() {
        return _y; //assumes our mesh has been centered on [0,0] (normalized)
    }

    //axis-aligned intersection test
    static final PointF overlap = new PointF( 0 , 0 ); //Q&D PointF pool for collision detection. Assumes single threading.
    @SuppressWarnings("UnusedReturnValue")
    static boolean getOverlap(final GLEntity a, final GLEntity b, final PointF overlap) {
        overlap.x = 0.0f;
        overlap.y = 0.0f;
        final float centerDeltaX = a.centerX() - b.centerX();
        final float halfWidths = (a._width + b._width) * 0.5f;
        float dx = Math.abs(centerDeltaX); //cache the abs, we need it twice

        if (dx > halfWidths) return false ; //no overlap on x == no collision

        final float centerDeltaY = a.centerY() - b.centerY();
        final float halfHeights = (a._height + b._height) * 0.5f;
        float dy = Math.abs(centerDeltaY);

        if (dy > halfHeights) return false ; //no overlap on y == no collision

        dx = halfWidths - dx; //overlap on x
        dy = halfHeights - dy; //overlap on y
        if (dy < dx) {
            overlap.y = (centerDeltaY < 0 ) ? -dy : dy;
        } else if (dy > dx) {
            overlap.x = (centerDeltaX < 0 ) ? -dx : dx;
        } else {
            overlap.x = (centerDeltaX < 0 ) ? -dx : dx;
            overlap.y = (centerDeltaY < 0 ) ? -dy : dy;
        }
        return true ;
    }
    //Some good reading on bounding-box intersection tests:
    //https://gamedev.stackexchange.com/questions/586/what-is-the-fastest-way-to-work-out-2d-bounding-box-intersection
    static boolean isAABBOverlapping(final GLEntity a, final GLEntity b) {
//        Log.d(TAG, String.format("%s  %s  %s  %s", a.left(), a.right(), a.top(), a.bottom()));
//        Log.d(TAG, String.format("%s  %s  %s  %s", b.left(), b.right(), b.top(), b.bottom()));
//        Log.d(TAG, String.format("%s  %s  %s  %s",
//                a.right() <= b.left(), b.right() <= a.left(),
//                a.bottom() <= b.top(), b.bottom() <= a.top()));
        return !(a.right() <= b.left()
                || b.right() <= a.left()
//                || a.bottom() >= b.top() // TODO: bottom and top is inverted somewhere. FIX!!
//                || b.bottom() >= a.top());
                || a.bottom() <= b.top()
                || b.bottom() <= a.top());
    }

    public void onCollision(final GLEntity that){}

    public void setColors(final float[] colors){
        Objects.requireNonNull(colors);
        Utils.require(colors.length >= 4);
        setColors(colors[0], colors[1], colors[2], colors[3]);
    }

    void setColors(final float r, final float g, final float b, final float a) {
        _color[0] = r;
        _color[1] = g;
        _color[2] = b;
        _color[3] = a;
    }

    public float left() {
        return _x+_mesh.left()* _scale;
    }

    public float right() {
        return _x+_mesh.right()* _scale;
    }

    public float top() {
        return _y+_mesh.top()*_scale;
    }

    public float bottom() {
        return _y+_mesh.bottom()*_scale;
    }

    public void setLeft(final float xPosition) {
        _x = xPosition - _mesh.left()* _scale;
    }

    public void setRight(final float xPosition) {
        _x = xPosition - _mesh.right()* _scale;
    }

    public void setTop(final float yPosition) {
        _y = yPosition - _mesh.top()*_scale;
    }

    public void setBottom(final float yPosition) {
        _y = yPosition - _mesh.bottom()*_scale;
    }

    public void setCenter(final float x, final float y) {
        _x = x;
        _y = y;
    }

    public void setScale(final float scale){
        _scale = scale;
    }
}
