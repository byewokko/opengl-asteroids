package com.grimezupt.asteroidsopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.grimezupt.asteroidsopengl.entities.Entity;
import com.grimezupt.asteroidsopengl.entities.HUD;
import com.grimezupt.asteroidsopengl.entities.Player;
import com.grimezupt.asteroidsopengl.entities.World;
import com.grimezupt.asteroidsopengl.input.InputManager;
import com.grimezupt.asteroidsopengl.utils.Timer;
import com.grimezupt.asteroidsopengl.utils.TimerListener;

public class Game extends GLSurfaceView implements TimerListener {
    private static final String TAG = "Game";
    public static final float LEVEL_BREAK_TIME = 3f;
    public static final int TIMER_LEVEL_START = 0;
    public Timer _timer = new Timer();
    public HUD _hud = null;
    public boolean _levelBreak = true;
    public boolean _fireToContinue = true;


    public enum Event {
        EXPLOSION,
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
        _levelBreak = true;
        _fireToContinue = true;
        GLRenderer._game = this;
        Entity._game = this;
        _world = new World();
        _scoring = new Scoring();
        setEGLContextClientVersion(2);
        _renderer = new GLRenderer(_world);
        setRenderer(_renderer);
        _hud = new HUD(_scoring);
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
        _scoring.onGameEvent(event, entity);
        _world.onGameEvent(event, entity);
        _hud.animateEvent(event, entity);
        if (event == Event.LEVEL_CLEAR){
            _timer.setEvent(this, TIMER_LEVEL_START, LEVEL_BREAK_TIME);
            _levelBreak = true;
        } else if (event == Event.LEVEL_START){
            _fireToContinue = false;
            _levelBreak = false;
        }
    }

    @Override
    public void onTimerEvent(int type) {
        if (type == TIMER_LEVEL_START){
            onGameEvent(Event.LEVEL_START, null);
        }
    }
}
