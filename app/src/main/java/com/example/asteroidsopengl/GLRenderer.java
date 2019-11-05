package com.example.asteroidsopengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.asteroidsopengl.entities.GLEntity;

import java.util.ArrayList;
import java.util.Collection;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "GLRenderer";
    private final ArrayList<GLEntity> _entities = new ArrayList<>();

    public GLRenderer() {
        GLManager.buildProgram();
    }

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
        GLES20.glClearColor(0.7f, 0.1f, 0.4f, 1f);
        GLManager.buildProgram();
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
        for (GLEntity e : _entities){
            e.render();
        }
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
