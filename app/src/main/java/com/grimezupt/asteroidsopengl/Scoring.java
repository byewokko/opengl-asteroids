package com.grimezupt.asteroidsopengl;

import com.grimezupt.asteroidsopengl.entities.Entity;

public class Scoring {
    public static final int MAX_LIVES = 4;
    public static final int LEVEL_CLEAR_BONUS = 50;
    public static final int FLAWLESS_LEVEL_BONUS = 50;
    public int _level = 1;
    public int _lives = 0;
    public long _score = 0;
    public int _levelBonus = 0;
    public boolean _wasFlawless = false;

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
        if (_lives == MAX_LIVES){
            _wasFlawless = true;
            _levelBonus = scorePoints(LEVEL_CLEAR_BONUS + FLAWLESS_LEVEL_BONUS);
        } else {
            _wasFlawless = false;
            _levelBonus = scorePoints(LEVEL_CLEAR_BONUS);
        }
        _level++;
    }

    public int scorePoints(int points){
        final int pointsTotal = points * _level;
        _score += pointsTotal;
        return pointsTotal;
    }

    public void onGameEvent(Game.Event event, Entity entity) {
        if (event == Game.Event.LEVEL_CLEAR){
            levelUp();
        } else if (event == Game.Event.LEVEL_START){
            resetLives();
        } else if (event == Game.Event.GAME_START){
            init();
        }
    }

    private void resetLives() {
        _lives = MAX_LIVES;
    }
}
