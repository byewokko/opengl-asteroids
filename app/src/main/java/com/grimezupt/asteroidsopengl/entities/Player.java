package com.grimezupt.asteroidsopengl.entities;

import android.os.SystemClock;

import com.grimezupt.asteroidsopengl.InputManager;
import com.grimezupt.asteroidsopengl.mesh.Triangle;
import com.grimezupt.asteroidsopengl.World;
import com.grimezupt.asteroidsopengl.utils.Utils;

public class Player extends GLEntity {
    private static final String TAG = "Player";
    private static final float THRUST = 5f;
    private static final float DRAG = 0.995f;
    private static final float ROTATION_VELOCITY = 320f;
    private static final float HALT_THRESHOLD = 0.1f;
    private float _horizontalFactor = 0f;
    private boolean _thrusting = false;
    private boolean _shooting = false;

    public Player(float x, float y) {
        super();
        _x = x;
        _y = y;
        _scale = 10f;
        _mesh = new Triangle();
    }

    @Override
    public void update(double dt) {
        _rotation += _horizontalFactor * ROTATION_VELOCITY * dt;
        if (_thrusting){
            final float theta = _rotation * RADIANS;
            _velX -= THRUST * Math.sin(theta);
            _velY += THRUST * Math.cos(theta);
        }
        _velX *= DRAG;
        _velY *= DRAG;
        if (Utils.getVectorMagnitude(_velX, _velY) < HALT_THRESHOLD){
            _velX = 0f;
            _velY = 0f;
        }
        super.update(dt);
    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
    }

    public void input(InputManager inputs) {
        _horizontalFactor = inputs._horizontalFactor;
        _thrusting = inputs._pressingB;
        _shooting = inputs._pressingA;
    }
}
