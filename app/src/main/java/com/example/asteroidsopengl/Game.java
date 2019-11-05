package com.example.asteroidsopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.asteroidsopengl.entities.GLEntity;
import com.example.asteroidsopengl.entities.Player;

public class Game extends GLSurfaceView {
    private static final String TAG = "Game";
    private GLRenderer _renderer = null;
    private Player _player = null;

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
        _renderer = new GLRenderer();
        GLManager.buildProgram();
        setRenderer(_renderer);
        GLEntity._game = this;
        _player = new Player(0f, 0.5f);
        _renderer.addEntities(_player);
    }






}
