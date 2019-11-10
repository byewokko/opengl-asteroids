package com.grimezupt.asteroidsopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.grimezupt.asteroidsopengl.entities.GLBorder;
import com.grimezupt.asteroidsopengl.entities.GLEntity;
import com.grimezupt.asteroidsopengl.entities.Player;

public class Game extends GLSurfaceView {
    private static final String TAG = "Game";
    private GLRenderer _renderer = null;
    private World _world = null;
    private Player _player = null;
    private GLBorder _border = null;

    public Game(final Context context) {
        super(context);
        init();
    }

    public Game(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        GLEntity._game = this;
        GLRenderer._game = this;
        World._game = this;
        _world = new World();
        setEGLContextClientVersion(2);
        _renderer = new GLRenderer(_world);
        setRenderer(_renderer);
    }
}
