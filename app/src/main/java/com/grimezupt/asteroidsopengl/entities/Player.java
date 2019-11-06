package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.Triangle;

public class Player extends GLEntity {
    private static final String TAG = "Player";

    public Player(float x, float y) {
        super();
        _x = x;
        _y = y;
        final float [] color = {0.9f, 0.9f, 0.1f, 1.0f};
        setColors(color);
        _mesh = new Triangle();
    }

    @Override
    public void update(double dt) {
//        super.update(dt);
    }
}
