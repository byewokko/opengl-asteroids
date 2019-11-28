package com.grimezupt.asteroidsopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.grimezupt.asteroidsopengl.entities.Entity;
import com.grimezupt.asteroidsopengl.entities.GLBorder;
import com.grimezupt.asteroidsopengl.entities.Player;
import com.grimezupt.asteroidsopengl.entities.World;

public class Game extends GLSurfaceView {
    private static final String TAG = "Game";
    public enum Event {
        BULLET_IMPACT,
        ASTEROID_DESTROYED,
        PLAYER_HURT,
        SHOOT,
        GAME_OVER,
        LEVEL_CLEAR
    }
    private GLRenderer _renderer = null;
    private World _world = null;
    private Player _player = null;
    private GLBorder _border = null;
    private InputManager _controls = new InputManager();

    public Game(final Context context) {
        super(context);
        init();
    }

    public Game(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        GLRenderer._game = this;
        Entity._game = this;
        _world = new World();
//        Entity._world = _world;
        setEGLContextClientVersion(2);
        _renderer = new GLRenderer(_world);
        setRenderer(_renderer);
        _world.build();
    }

    public void setControls(final InputManager controls) {
        _controls = controls;
    }

    public InputManager getInputs() {
        return _controls;
    }

    public void onGameEvent(Event event, Entity entity){
        // TODO: play sound
    }
}
