package com.grimezupt.asteroidsopengl.entities;

import android.graphics.PointF;

import com.grimezupt.asteroidsopengl.InputManager;
import com.grimezupt.asteroidsopengl.mesh.Triangle;
import com.grimezupt.asteroidsopengl.utils.CollisionDetection;
import com.grimezupt.asteroidsopengl.utils.Utils;

public class Player extends GLEntity {
    private static final String TAG = "Player";
    private static final float SIZE = 5f;
    private static final float THRUST = 2.5f;
    private static final float DRAG = 0.995f;
    private static final float ROTATION_VELOCITY = 320f;
    private static final float MAX_VELOCITY = 200f;
    private static final float SHOOTING_COOLDOWN = 0.25f;
    private static final float RECOIL = 10f;
    private static final float KNOCKBACK = 20f;
    private static final float RECOVERY_TIME = 1.6f;
    private static final float NUMB_TIME = 0.6f;
    private EntityPool<Projectile> _projectilePool = null;
    private float _horizontalFactor = 0f;
    private boolean _thrusting = false;
    private boolean _shooting = false;
    private float _shootingCooldown = 0f;
    private double _timeToRecover = 0f;


    public Player(EntityPool<Projectile> projectilePool, float x, float y) {
        _projectilePool = projectilePool;
        _x = x;
        _y = y;
        _rotation = 0f;
        setScale(SIZE);
        _mesh = new Triangle();
        _mesh.applyAspectRatio();
    }

    @Override
    public void update(double dt) {
        afterCollisionUpdate();
        _timeToRecover -= dt;
        _shootingCooldown -= dt;
        final float velocity = (float) Utils.getVectorMagnitude(_velX, _velY);
        final float theta = _rotation * RADIANS;
        thrust(velocity, theta);
        shoot(theta);
        _rotation += _horizontalFactor * ROTATION_VELOCITY * dt;
        _velX *= DRAG;
        _velY *= DRAG;
        super.update(dt);
    }

    private void afterCollisionUpdate() {
        // TODO: update speeds accumulated from last update's collisions
    }

    private void thrust(final float velocity, final float theta) {
        if (_thrusting && velocity < MAX_VELOCITY){
            _velX += THRUST * Math.sin(theta);
            _velY -= THRUST * Math.cos(theta);
        }
    }

    private void shoot(final float theta){
        if (_shooting && _shootingCooldown <= 0f && _timeToRecover <= 0f) {
            Projectile p = _projectilePool.pull();
            if (p != null) {
                p.activate(_x, _y, _velX, _velY, theta);
                _shootingCooldown = SHOOTING_COOLDOWN;
                _velX -= RECOIL * Math.sin(theta);
                _velY += RECOIL * Math.cos(theta);
            }
        }
    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
    }

    public void input(InputManager inputs) {
        _horizontalFactor = inputs._horizontalFactor;
        if (isNumb()) {
            _thrusting = false;
            _shooting = false;
        } else {
            _thrusting = inputs._pressingA;
            _shooting = inputs._pressingB;
        }
    }

    @Override
    public boolean isColliding(GLEntity that) {
        final float distance = (float) Utils.getVectorMagnitude(_x - that._x, _y - that._y);
        if (distance > radius() + that.radius()){
            return false;
        }
//        if (distance < that.radius() - width()){ //TODO: stupid
//            return true;
//        }
        final PointF[] thisVerts = CollisionDetection.pointListA;
        getPointList(thisVerts);
        final PointF[] thatVerts = CollisionDetection.pointListB;
        that.getPointList(thatVerts);

        return CollisionDetection.polygonVsPolygon(thisVerts, thatVerts);
//        return CollisionDetection.triangleVsPolygon(thisVerts, thatVerts);
    }

    @Override
    public void onCollision(GLEntity that) {
        super.onCollision(that);
    }

    public void onCollision(Asteroid that) {
//        super.onCollision(that);
        final PointF relativePos = Utils.normalize(this._x - that._x, this._y - that._y);
        _velX = relativePos.x * KNOCKBACK;
        _velY = relativePos.y * KNOCKBACK;
        if (_timeToRecover <= 0f) {
            takeDamage();
            recover();
        }
    }

    private void takeDamage() {

    }

    private void recover() {
        _timeToRecover = RECOVERY_TIME;
    }

    private boolean isNumb() {
        return (_timeToRecover > RECOVERY_TIME - NUMB_TIME);
    }
}
