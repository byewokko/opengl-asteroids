package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.Scoring;
import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.mesh.Triangle;
import com.grimezupt.asteroidsopengl.utils.TimerListener;

public class HUD extends GLEntity implements TimerListener {
    public static final String TAG = "HUD";
    private static final float DEFAULT_BIGTEXT_DURATION = 3f;
    private static final float HUD_TEXT_MARGIN = 1f;
    private boolean _showFPS = true;
    private boolean _showBigText = true;
    private float TEXT_SIZE = 10f;
    private float BIG_TEXT_SIZE = 15f;

    private Scoring _scoring = null;

    private GLText _scoreText = null;
    private GLText _bigText = null;
    private GLText _fpsText = null;
    private GLEntity[] _lifeArray = null;
    private Mesh _lifeMesh = null;

    public HUD() {
        init();
    }

    private void init() {
        // init score text
        _scoreText = new GLText("00000", HUD_TEXT_MARGIN, HUD_TEXT_MARGIN);
        _scoreText.setScale(3f);
        _scoreText.setAlign(GLText.ALIGN_LEFT);
        // init lives array
        _lifeMesh = new Triangle();
        _lifeMesh.rotate(Mesh.Z, -Math.PI*0.3);
        // etc.
    }

    @Override
    public void update(double dt) {
        _scoreText.setString(String.valueOf(_scoring._score));
        // TODO: update FPS
    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
        _scoreText.render(viewportMatrix);
        for (int i = 0; i < _scoring._lives; i++) {
            _lifeArray[i].render(viewportMatrix);
        }
        if (_showFPS){
            _fpsText.render(viewportMatrix);
        }
        if (_showBigText){
            _bigText.render(viewportMatrix);
        }
    }

    public void setBigText(String string, float duration){
        if (duration == 0) duration = DEFAULT_BIGTEXT_DURATION;
        _bigText.setString(string);
        getTimer().setEvent(this, 0, duration);
        _showBigText = true;
    }

    @Override
    public void onTimerEvent(int type) {
        _showBigText = false;
    }
}
