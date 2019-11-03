package com.example.asteroidsopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class Game extends GLSurfaceView {
    private static final String TAG = "Game";
    private GlRenderer _renderer = null;

    public Game(final Context context) {
        super(context);
        init();
    }

    public Game(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        _renderer = new GlRenderer();
        setRenderer(_renderer);
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }








}
