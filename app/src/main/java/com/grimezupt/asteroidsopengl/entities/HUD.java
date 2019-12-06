package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.Game;
import com.grimezupt.asteroidsopengl.Scoring;
import com.grimezupt.asteroidsopengl.utils.TimerListener;

public class HUD extends GLEntity implements TimerListener {
    public static final String TAG = "HUD";
    private static final float DEFAULT_BIGTEXT_DURATION = 3f;
    private static final float HUD_TEXT_MARGIN = 2f;
    public static final int BIGTEXT_EXPIRED = 0;
    private static final float SUBTEXT_HEIGHT = 2f;
    private float TEXT_HEIGHT = 3f;
    private float BIGTEXT_HEIGHT = 4f;
    private boolean _showFPS = false;
    private boolean _showBigText = false;

    private Scoring _scoring = null;

    private GLText _scoreText = null;
    private GLText _bigText = null;
    private GLText _fpsText = null;
    private LifeArray _lifeArray = null;
    private GLText _subText = null;

    public HUD(Scoring scoring) {
        _scoring = scoring;
        init();
    }

    private void init() {
        // init score text
        _scoreText = new GLText(World.WIDTH - HUD_TEXT_MARGIN, HUD_TEXT_MARGIN);
        _scoreText.setScale(TEXT_HEIGHT);
        _scoreText.setAlign(GLText.ALIGN_RIGHT);
        // init lives array
        _lifeArray = new LifeArray(World.WIDTH * 0.5f, HUD_TEXT_MARGIN);
        _lifeArray.setScale(TEXT_HEIGHT);
        _lifeArray.setAlign(LifeArray.ALIGN_CENTER);
        // etc.
        _bigText = new GLText( World.WIDTH * 0.5f, World.HEIGHT * 0.5f - BIGTEXT_HEIGHT * 2f);
        _bigText.setScale(BIGTEXT_HEIGHT);
        _bigText.setAlign(GLText.ALIGN_CENTER);
        _subText = new GLText(World.WIDTH * 0.5f, World.HEIGHT * 0.5f + HUD_TEXT_MARGIN);
        _subText.setScale(SUBTEXT_HEIGHT);
        _subText.setAlign(GLText.ALIGN_CENTER);
    }

    @Override
    public void update(double dt) {
        // TODO: update FPS
        _scoreText.setString(String.valueOf(_scoring._score));
        _lifeArray.setLives(_scoring._lives);
    }

    @Override
    public void render(final float[] viewportMatrix) {
        _scoreText.render(viewportMatrix);
        _lifeArray.render(viewportMatrix);
        if (_showFPS){
            _fpsText.render(viewportMatrix);
        }
        if (_showBigText){
            _bigText.render(viewportMatrix);
            _subText.render(viewportMatrix);
        }
    }

    public void setBigText(String bigString, String subString, float duration){
        if (duration == 0f) {
            getTimer().setEvent(this, BIGTEXT_EXPIRED, DEFAULT_BIGTEXT_DURATION);
        } else if (duration > 0f) {
            getTimer().setEvent(this, BIGTEXT_EXPIRED, duration);
        }
        _bigText.setString(bigString);
        _subText.setString(subString);
        _showBigText = true;
    }

    @Override
    public void onTimerEvent(int type) {
        _showBigText = false;
    }

    public void animateEvent(Game.Event event, Entity entity) {
        if (event == Game.Event.LEVEL_CLEAR){
            setBigText(String.format("Level %s cleared@", _scoring._level), "Next wave incoming", -1);
        }
        if (event == Game.Event.GAME_START){
            setBigText("Destroy all the asteroids@", "Press up to start.", -1);
        }
        if (event == Game.Event.GAME_OVER){
            setBigText("Game over", "Press up to start again.", -1);
        }
    }
}
