package com.grimezupt.asteroidsopengl.entities;

import android.opengl.Matrix;

import com.grimezupt.asteroidsopengl.GLManager;
import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.mesh.Triangle;

public class LifeArray extends GLEntity {
    private static final float SPACING = 3f;
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;
    private int _align = ALIGN_LEFT;
    private int _lives = 0;

    public LifeArray(final float x, final float y) {
        _x = x;
        _y = y;
        _mesh = new Triangle();
        _mesh.applyAspectRatio();
        _mesh.rotate(Mesh.Z, Math.PI*0.3);
        _mesh.saveAspectRatio();
        _mesh.normalize();
        _mesh.applyAspectRatio();
        _mesh.setOrigin(-1f, -1f, 0f);
    }

    @Override
    public void update(double dt) {}

    public void setLives(int lives){
        _lives = lives;
    }

    @Override
    public void render(float[] viewportMatrix) {
        final int OFFSET = 0;
        final float alignOffset;
        if (_align == ALIGN_RIGHT){
            alignOffset = _lives * (_mesh._width+SPACING) - SPACING;
        } else if (_align == ALIGN_CENTER){
            alignOffset = (_lives * (_mesh._width+SPACING) - SPACING) * 0.5f;
        } else {
            alignOffset = 0;
        }
        for(int i = 0; i < _lives; i++) {
            Matrix.setIdentityM(modelMatrix, OFFSET); //reset model matrix
            Matrix.translateM(modelMatrix, OFFSET, _x + (_mesh._width + SPACING) * i - alignOffset, _y, _depth);
            Matrix.scaleM(modelMatrix, OFFSET, _scale, _scale, 1f);
            Matrix.multiplyMM(viewportModelMatrix, OFFSET, viewportMatrix, OFFSET, modelMatrix, OFFSET);
            GLManager.draw(_mesh, viewportModelMatrix, _color);
        }
    }

    public void setAlign(int align) {
        _align = align;
    }
}
