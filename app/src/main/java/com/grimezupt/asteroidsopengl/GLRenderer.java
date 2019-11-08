package com.grimezupt.asteroidsopengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.grimezupt.asteroidsopengl.entities.GLEntity;

import java.util.ArrayList;
import java.util.Collection;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "GLRenderer";
    static Game _game = null;

    public static float WORLD_WIDTH = 80f; //all dimensions are in meters
    public static float WORLD_HEIGHT = 80f;
    public static float METERS_TO_SHOW_X = 160f; //160m x 90m, the entire game world in view
    public static float METERS_TO_SHOW_Y = 90f; //TODO: calculate to match screen aspect ratio

    private float[] _viewportMatrix = new float[4*4];

    private float[] _bgColor = Config.Colors.BG_COLOR;

    private final ArrayList<GLEntity> _entities = new ArrayList<>();
    public GLRenderer() {
        GLManager.buildProgram();
    }

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
        GLManager.buildProgram();
        GLES20.glClearColor(_bgColor[0], _bgColor[1], _bgColor[2], _bgColor[3]);
        _game.buildEntities();
        // build program (shaders)
        // compile program
        // tell opengl to use program
    }

    @Override
    public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(final GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        final int offset = 0;
        final float left = 0;
        final float right = METERS_TO_SHOW_X;
        final float bottom = 0;
        final float top = METERS_TO_SHOW_Y;
        final float near = 0f;
        final float far = 1f;
        Matrix.orthoM(_viewportMatrix, offset, left, right, bottom, top, near, far);
        for (GLEntity e : _entities){
            e.render(_viewportMatrix);
        }
    }

    public void setBgColor(float[] bgColor) {
        _bgColor = bgColor;
    }

    public void addEntities(final Collection<GLEntity> entityCollection){
        _entities.addAll(entityCollection);
    }

    public void addEntities(final GLEntity entity){
        _entities.add(entity);
    }

    public void clearEntities(){
        _entities.clear();
    }
}
