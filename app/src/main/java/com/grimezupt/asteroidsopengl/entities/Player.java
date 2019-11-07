package com.grimezupt.asteroidsopengl.entities;

import android.os.SystemClock;
import android.util.Log;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.GLRenderer;
import com.grimezupt.asteroidsopengl.Triangle;

public class Player extends GLEntity {
    private static final String TAG = "Player";

    public Player(float x, float y) {
        super();
        _x = x;
        _y = y;
        _scale = 20f;
        final float [] color = Config.Colors.FG_COLOR;
        setColors(color);
        _mesh = new Triangle();
    }

    @Override
    public void update(double dt) {
        super.update(dt);

    }

    @Override
    public void render(float[] viewportMatrix) {
        final float TO_RADIANS = (float)Math.PI/180.0f;
        final float startPositionX = GLRenderer.WORLD_WIDTH/2;
        final float startPositionY = GLRenderer.WORLD_HEIGHT/2;
        final float range = GLRenderer.METERS_TO_SHOW_Y/2f; //amplitude of our sine wave (how far to travel)
        final double speed = 360d/2000d; //I want to cover a full revolution (360 degrees) in 2 seconds.
        double angle = (SystemClock.uptimeMillis() * speed) % 360d; //turn linear, ever growing, timestamp into 0-359 range
        angle *= TO_RADIANS; //convert degrees to radians, that's what sin wants.

        //sin() returns a numeric value between [-1.0, 1.0], the sine of the angle given in radians.
        //perfect for moving smoothly up-and-down some range
        _x = (float) (startPositionX + Math.sin(angle) * range);
        _y = (float) (startPositionY + Math.sin(2d*angle) * range);
//        Log.d(TAG, String.format("%s ", SystemClock.uptimeMillis()));
        long time = SystemClock.uptimeMillis() % 5000; //turn a timestamp into 0-4999 ms
        _rotation = (360.0f / 5000.0f) * time; // Do a complete rotation every 5 seconds.
        super.render(viewportMatrix);
    }
}
