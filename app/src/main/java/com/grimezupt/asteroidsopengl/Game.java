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
        setEGLContextClientVersion(2);
        _renderer = new GLRenderer();
        setRenderer(_renderer);
    }


    public void buildEntities() {
//        _player = new Player(0f, 0f);
        _player = new Player(GLRenderer.WORLD_WIDTH/2f, GLRenderer.WORLD_HEIGHT/2f);
        _border = new GLBorder(0f, 0f, GLRenderer.WORLD_WIDTH, GLRenderer.WORLD_HEIGHT);
        _renderer.addEntities(_player);
        _renderer.addEntities(_border);
    }
}
