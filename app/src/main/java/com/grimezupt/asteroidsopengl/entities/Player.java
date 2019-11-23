package com.grimezupt.asteroidsopengl.entities;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.GLManager;
import com.grimezupt.asteroidsopengl.InputManager;
import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.mesh.Triangle;
import com.grimezupt.asteroidsopengl.utils.Utils;

public class Player extends GLEntity {
    private static final String TAG = "Player";
    private static final float SIZE = 5f;
    private static final float THRUST = 2.5f;
    private static final float DRAG = 0.995f;
    private static final float ROTATION_VELOCITY = 320f;
    private static final float MAX_VELOCITY = 200f;
    private static final float SHOOTING_TIME_LIMIT = 0.25f;
    private static final float RECOIL = 20;
    private final Mesh _bbMesh;
    private EntityPool<Projectile> _projectilePool = null;
    private float _horizontalFactor = 0f;
    private boolean _thrusting = false;
    private boolean _shooting = false;
    private float _shootingTimer = 0f;

    public Player(EntityPool<Projectile> projectilePool, float x, float y) {
        _projectilePool = projectilePool;
        _x = x;
        _y = y;
        setScale(SIZE, SIZE);
        _mesh = new Triangle();
        _mesh.applyAspectRatio();
        _width = _mesh._width * _xScale;
        _height = _mesh._height * _yScale;
        _bbMesh = new Mesh(Mesh.squareGeometry(), GLES20.GL_LINES);
        _bbMesh.scale(_mesh._aspectWidth, _mesh._aspectHeight, 1f);
    }

    @Override
    public void update(double dt) {
        _shootingTimer -= dt;
        final float velocity = (float) Utils.getVectorMagnitude(_velX, _velY);
        final float theta = _rotation * RADIANS;
        thrust(velocity, theta);
        shoot(theta);
        _rotation += _horizontalFactor * ROTATION_VELOCITY * dt;
        _velX *= DRAG;
        _velY *= DRAG;
        super.update(dt);
    }

    private void thrust(final float velocity, final float theta) {
        if (_thrusting && velocity < MAX_VELOCITY){
            _velX -= THRUST * Math.sin(theta);
            _velY += THRUST * Math.cos(theta);
        }
    }

    private void shoot(final float theta){
        if (_shooting && _shootingTimer <= 0f) {
            Projectile p = _projectilePool.pull();
            if (p != null) {
                p.activate(_x, _y, _velX, _velY, theta);
                _shootingTimer = SHOOTING_TIME_LIMIT;
                _velX += RECOIL * Math.sin(theta);
                _velY -= RECOIL * Math.cos(theta);
            }
        }
    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
        GLManager.draw(_bbMesh, rotationViewportModelMatrix, _color);
    }

    public void input(InputManager inputs) {
        _horizontalFactor = inputs._horizontalFactor;
        _thrusting = inputs._pressingB;
        _shooting = inputs._pressingA;
    }
}
