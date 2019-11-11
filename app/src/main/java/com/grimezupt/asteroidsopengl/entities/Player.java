package com.grimezupt.asteroidsopengl.entities;

import android.os.SystemClock;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.mesh.Triangle;
import com.grimezupt.asteroidsopengl.World;

public class Player extends GLEntity {
    private static final String TAG = "Player";

    public Player(float x, float y) {
        super();
        _x = x;
        _y = y;
        _scale = 10f;
        _mesh = new Triangle();
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        final float TO_RADIANS = (float)Math.PI/180.0f;
        final float startPositionX = World.WIDTH /2;
        final float startPositionY = World.HEIGHT /2;
        final float rangeX = World.WIDTH /2f; //amplitude of our sine wave (how far to travel)
        final float rangeY = World.HEIGHT /2f; //amplitude of our sine wave (how far to travel)
        final double speed = 360d/2000d; //I want to cover a full revolution (360 degrees) in 2 seconds.
        double angle = (SystemClock.uptimeMillis() * speed) % 360d; //turn linear, ever growing, timestamp into 0-359 range
        double angle_rad = angle * TO_RADIANS; //convert degrees to radians, that's what sin wants.

        //sin() returns a numeric value between [-1.0, 1.0], the sine of the angle given in radians.
        //perfect for moving smoothly up-and-down some range
        _x = (float) (startPositionX + Math.cos(angle_rad) * rangeX);
        _y = (float) (startPositionY + Math.sin(2f*angle_rad) * rangeY);
//        Log.d(TAG, String.format("%s ", SystemClock.uptimeMillis()));
        _rotation = (float) angle; // Do a complete rotation every 5 seconds.

    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
    }
}
