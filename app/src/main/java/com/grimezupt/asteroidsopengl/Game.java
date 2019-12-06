package com.grimezupt.asteroidsopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.grimezupt.asteroidsopengl.entities.Entity;
import com.grimezupt.asteroidsopengl.entities.GLBorder;
import com.grimezupt.asteroidsopengl.entities.HUD;
import com.grimezupt.asteroidsopengl.entities.Player;
import com.grimezupt.asteroidsopengl.entities.World;
import com.grimezupt.asteroidsopengl.input.InputManager;
import com.grimezupt.asteroidsopengl.utils.Timer;

public class Game extends GLSurfaceView {
    private static final String TAG = "Game";
    public Timer _timer = new Timer();
    public HUD _hud = null;

    public enum Event {
        BULLET_IMPACT,
        ASTEROID_DESTROYED,
        PLAYER_HURT,
        SHOOT,
        GAME_OVER,
        LEVEL_START, GAME_START, LEVEL_CLEAR
    }
    private GLRenderer _renderer = null;
    private World _world = null;
    private Player _player = null;
    public Scoring _scoring = null;
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
        _scoring = new Scoring();
        setEGLContextClientVersion(2);
        _renderer = new GLRenderer(_world);
        setRenderer(_renderer);
        _world.build();
        _hud = new HUD(_scoring);
    }

    public void setControls(final InputManager controls) {
        _controls = controls;
    }

    public InputManager getInputs() {
        return _controls;
    }

    public void onGameEvent(Event event, Entity entity){
        // TODO: play sound
        _hud.animateEvent(event, entity);
        _scoring.onGameEvent(event, entity);
    }
}
