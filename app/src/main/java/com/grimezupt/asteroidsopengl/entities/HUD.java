package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.Game;
import com.grimezupt.asteroidsopengl.Scoring;
import com.grimezupt.asteroidsopengl.utils.TimerListener;

import java.util.Locale;

public class HUD extends GLEntity implements TimerListener {
    public static final String TAG = "HUD";
    public static final int BIGTEXT_EXPIRED = 0;
    private static final int GAMEOVER_TEXT_CHANGE = 1;
    private static final float HUD_TEXT_MARGIN = 2f;
    private static final float DEFAULT_BIGTEXT_DURATION = 3f;
    private static final float SUBTEXT_HEIGHT = 2f;
    private float TEXT_HEIGHT = 3f;
    private float BIGTEXT_HEIGHT = 4f;
    private boolean _showFPS = true;
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
        // init fps text
        _fpsText = new GLText(HUD_TEXT_MARGIN, HUD_TEXT_MARGIN);
        _fpsText.setScale(TEXT_HEIGHT);
        // init lives array
        _lifeArray = new LifeArray(World.WIDTH * 0.5f, 0);
        _lifeArray.setScale(TEXT_HEIGHT);
        _lifeArray.setAlign(LifeArray.ALIGN_CENTER);
        // etc.
        _bigText = new GLText( World.WIDTH * 0.5f, World.HEIGHT * 0.5f - BIGTEXT_HEIGHT * 2f);
        _bigText.setScale(BIGTEXT_HEIGHT);
        _bigText.setAlign(GLText.ALIGN_CENTER);
        _bigText.setColors(Config.Colors.HIGHLIGHT);
        _subText = new GLText(World.WIDTH * 0.5f, World.HEIGHT * 0.5f + HUD_TEXT_MARGIN);
        _subText.setScale(SUBTEXT_HEIGHT);
        _subText.setAlign(GLText.ALIGN_CENTER);
        _subText.setColors(Config.Colors.HIGHLIGHT);
        _subText.setSpacing(0);
    }

    @Override
    public void update(double dt) {
        _fpsText.setString(String.format(Locale.ENGLISH,"%.1ffps", _game.getAvgFPS()));
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
        if (type == BIGTEXT_EXPIRED) {
            _showBigText = false;
        } else if (type == GAMEOVER_TEXT_CHANGE && isGameOver()) {
            setBigText("Game over", "Press @ to start again.", -1);
        }
    }

    public void animateEvent(Game.Event event, Entity entity) {
        if (event == Game.Event.LEVEL_CLEAR){
            if (_scoring._wasFlawless) {
                setBigText("Level cleared;", String.format("Flawless; +%s points;", _scoring._levelBonus), -1);
            } else {
                setBigText("Level cleared;", String.format("%s points bonus", _scoring._levelBonus), -1);
            }
        } else if (event == Game.Event.LEVEL_START){
            setBigText(String.format("Wave %s incoming", _scoring._level), "Go;", DEFAULT_BIGTEXT_DURATION);
        } else if (event == Game.Event.GAME_START){
            setBigText("Destroy all asteroids;", "Press @ to start.", -1);
        } else if (event == Game.Event.GAME_OVER){
            setBigText("Game over", String.format("Final score: %s", _scoring._score), -1);
            getTimer().setEvent(this, GAMEOVER_TEXT_CHANGE, DEFAULT_BIGTEXT_DURATION);
        }
    }
}
