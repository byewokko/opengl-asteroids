package com.grimezupt.asteroidsopengl;

import com.grimezupt.asteroidsopengl.entities.Entity;

public class Scoring {
    private static final int MAX_LIVES = 5;
    public int _level = 1;
    public int _lives = 0;
    public int _score = 0;

    public Scoring() {
        init();
    }

    public void init(){
        _level = 1;
        _score = 0;
        resetLives();
    }

    public boolean loseLife(){
        if (_lives > 0){
            _lives--;
            return true;
        } else {
            return false;
        }
    }

    public void levelUp(){
        _level++;
    }

    public void scorePoints(int points){
        _score += points;
    }

    public void onGameEvent(Game.Event event, Entity entity) {
        if (event == Game.Event.LEVEL_CLEAR){
            levelUp();
            resetLives();
        } else if (event == Game.Event.GAME_START){
            init();
        }
    }

    private void resetLives() {
        _lives = MAX_LIVES;
    }
}
