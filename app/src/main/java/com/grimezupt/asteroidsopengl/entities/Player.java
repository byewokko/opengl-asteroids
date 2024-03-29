package com.grimezupt.asteroidsopengl.entities;

import android.graphics.PointF;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.input.InputManager;
import com.grimezupt.asteroidsopengl.mesh.Triangle;
import com.grimezupt.asteroidsopengl.utils.CollisionDetection;
import com.grimezupt.asteroidsopengl.utils.TimerListener;
import com.grimezupt.asteroidsopengl.utils.Utils;

public class Player extends DynamicEntity implements TimerListener {
    private static final String TAG = "Player";

    private static final int RECOVERED = 0;
    private static final int AWAKE = 1;
    private static final int MISSILE_LOADED = 2;
    private static final float RECOVERY_TIME = 1.6f;
    private static final float NUMB_TIME = 0.5f;
    private static final float SHOOTING_COOLDOWN = 0.15f;

    private static final float SIZE = 5f;
    private static final float THRUST = 2.5f;
    private static final float ROTATION_VELOCITY = 320f;
    private static final float MAX_VELOCITY = 200f;
    private static final float DRAG = 1.4f;
    private static final float RECOIL = 10f;
    private static final float KNOCKBACK = 25f;

    private JetFlame _jet = null;
    private ProjectilePool _projectilePool = null;
    private float _horizontalFactor = 0f;
    private boolean _thrusting = false;
    private boolean _shooting = false;
    private boolean _isNumb = false;
    private boolean _isRecovering = false;
    private boolean _hasLoaded = true;
    private boolean _isRecovering0 = false;


    public Player(ProjectilePool projectilePool, float x, float y) {
        _projectilePool = projectilePool;
        setScale(SIZE);
        _mass = SIZE;
        _mesh = new Triangle();
        _mesh.applyAspectRatio();
        _jet = new JetFlame();
        reset(x, y);
    }

    @Override
    public void update(double dt) {
        if (isGameOver()) return;
        _x0 = _x;
        _y0 = _y;
        _isRecovering0 = _isRecovering;
        _velX0 = _velX;
        _velY0 = _velY;
        final float velocity = (float) Utils.getVectorMagnitude(_velX, _velY);
        final float theta = _rotation * RADIANS;
        thrust(velocity, theta);
        shoot(theta);
        _rotation += _horizontalFactor * ROTATION_VELOCITY * dt;

        super.update(dt);

        updateJet(dt);

        _velX *= (1f - dt*DRAG);
        _velY *= (1f - dt*DRAG);

        blink();
    }

    public void updateJet(double dt) {
        _jet._x = _x;
        _jet._y = _y;
        _jet._rotation = _rotation + 180f;
        _jet.update(dt);
    }

    private void blink() {
        final float period = 0.15f;
        final float dutyCycle = 0.5f;
        if (_isRecovering && Utils.squareWaveBoolean(getTimer().getClock(), period, dutyCycle)){
            setColors(Config.Colors.HIGHDARK);
        } else {
            setColors(Config.Colors.FOREGROUND);
        }
    }

    private void thrust(final float velocity, final float theta) {
        if (_thrusting && velocity < MAX_VELOCITY){
            _velX += THRUST * Math.sin(theta);
            _velY -= THRUST * Math.cos(theta);
        }
    }

    private void shoot(final float theta){
        if (_shooting && _hasLoaded && !_isRecovering && !_game._fireToContinue) {
            Projectile p = _projectilePool.pull();
            if (p != null) {
                p.activate(_x, _y, _velX, _velY, theta);
                _hasLoaded = false;
                getTimer().setEvent(this, MISSILE_LOADED, SHOOTING_COOLDOWN);
                _velX -= RECOIL * Math.sin(theta);
                _velY += RECOIL * Math.cos(theta);
            }
        }
    }

    @Override
    public void render(float[] viewportMatrix) {
        if (isGameOver()) return;
        super.render(viewportMatrix);
        if (_thrusting) {
            _jet.render(viewportMatrix);
        }
    }

    public void input(InputManager inputs) {
        if (isGameOver()) {
            _horizontalFactor = 0f;
        } else {
            _horizontalFactor = inputs._horizontalFactor;
        }
        if (_isNumb || isGameOver()) {
            _thrusting = false;
            _shooting = false;
        } else {
            _thrusting = inputs._pressingA;
            _shooting = inputs._justPressedB;
        }
    }

    @Override
    public boolean isColliding(GLEntity that) {
        final float distance = (float) Utils.getVectorMagnitude(_x - that._x, _y - that._y);
        if (distance > radius() + that.radius()){
            return false;
        }

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
        _velX = -impactUnit.x * KNOCKBACK;
        _velY = -impactUnit.y * KNOCKBACK;
        if (!_isRecovering) {
            if (getScoring().loseLife()) { // if not dead
                recover();
            }
        }
    }

    private void recover() {
        _isNumb = true;
        _isRecovering = true;
        getTimer().setEvent(this, AWAKE, NUMB_TIME);
        getTimer().setEvent(this, RECOVERED, RECOVERY_TIME);
    }

    @Override
    public boolean isDangerous(GLEntity that) {
        return !_isRecovering0;
    }

    @Override
    public void onTimerEvent(int type) {
        switch (type) {
            case RECOVERED:
                _isRecovering = false;
                break;
            case AWAKE:
                _isNumb = false;
                break;
            case MISSILE_LOADED:
                _hasLoaded = true;
                break;
        }
    }

    public void reset(float x, float y) {
        _x = x;
        _y = y;
        _velX = 0f;
        _velY = 0f;
        _rotation = 0f;
        recover();
    }
}
