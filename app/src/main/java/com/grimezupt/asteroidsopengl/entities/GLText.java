package com.grimezupt.asteroidsopengl.entities;

import android.opengl.Matrix;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.GLManager;
import com.grimezupt.asteroidsopengl.mesh.GLPixelFont;
import com.grimezupt.asteroidsopengl.mesh.Mesh;

public class GLText extends GLEntity {
    public static final GLPixelFont FONT = new GLPixelFont();
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;
    public static float GLYPH_WIDTH = GLPixelFont.WIDTH;
    public static float GLYPH_HEIGHT = GLPixelFont.HEIGHT;
    public static float GLYPH_SPACING = 0.3f;
    public static float POINT_SIZE = 6f;

    Mesh[] _meshes = null;
    private float _spacing = GLYPH_SPACING; //spacing between characters
    private float _glyphWidth = GLYPH_WIDTH;
    private float _glyphHeight = GLYPH_HEIGHT;
    private float[] _color = Config.Colors.FOREGROUND;
    private float _pointSize = POINT_SIZE;
    private int _align = ALIGN_LEFT;

    public GLText(final String s, final float x, final float y) {
        setString(s);
        _x = x;
        _y = y;
        setScale(1f);
    }

    public GLText(final float x, final float y) {
        _x = x;
        _y = y;
        setScale(1f);
    }

    @Override
    public void render(final float[] viewportMatrix){
        final int OFFSET = 0;
        final float alignOffset;
        if (_align == ALIGN_RIGHT){
            alignOffset = _meshes.length * (_glyphWidth*(1+_spacing)) - _glyphWidth*_spacing;
        } else if (_align == ALIGN_CENTER){
            alignOffset = (_meshes.length * (_glyphWidth*(1+_spacing)) - _glyphWidth*_spacing) * 0.5f;
        } else {
            alignOffset = 0;
        }
        for(int i = 0; i < _meshes.length; i++){
            if(_meshes[i] == null){ continue; }
            Matrix.setIdentityM(modelMatrix, OFFSET); //reset model matrix
            Matrix.translateM(modelMatrix, OFFSET, _x + (_glyphWidth*(1+_spacing))*i - alignOffset, _y, _depth);
            Matrix.scaleM(modelMatrix, OFFSET, _scale, _scale, 1f);
            Matrix.multiplyMM(viewportModelMatrix, OFFSET, viewportMatrix, OFFSET, modelMatrix, OFFSET);
            GLManager.draw(_meshes[i], viewportModelMatrix, _color, _pointSize);
        }
    }

    @Override
    public void update(double dt) {
        // super.update(dt);
    }

    public void setScale(float factor){
        super.setScale(factor);
//        _spacing = GLYPH_SPACING * _scale; //TODO: delet dis. scaling is handled by matrices
//        _glyphWidth = GLYPH_WIDTH*_scale;
//        _glyphHeight = GLYPH_HEIGHT*_yScale;
//        _pointSize = POINT_SIZE;
    }

    @Override
    public float height() {
        return _glyphHeight;
    }

    @Override
    public float width() {
        return (_glyphWidth+_spacing)*_meshes.length;
    }

    public void setString(final String s){
        _meshes = FONT.getString(s);
    }

    public void setAlign(int align) {
        _align = align;
    }
}
