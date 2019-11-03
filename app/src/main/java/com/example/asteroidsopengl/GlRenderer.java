package com.example.asteroidsopengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GlRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "GlRenderer";
    public GlRenderer() {
    }

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
        float[] color = {0.9f, 0.2f, 0.5f, 1f};
        GLES20.glClearColor(0.7f, 0.1f, 0.4f, 1f);
        // build program (shaders)
        // compile program
        // tell opengl to use program
    }

    @Override
    public void onSurfaceChanged(final GL10 unused, final int width, final int height) {

    }

    @Override
    public void onDrawFrame(final GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
