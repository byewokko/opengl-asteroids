package com.grimezupt.asteroidsopengl.entities;

import android.graphics.PointF;
import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.utils.Random;
import com.grimezupt.asteroidsopengl.utils.Utils;

public class Asteroid extends DynamicEntity implements Poolable {
    private static final float DEFAULT_SCALE = 10;
    public static final int DEFAULT_SIZE = 2;
    public static final int MAX_SIZE = 3;
    private boolean _active = false;
    private EntityPool<Asteroid> _pool = null;
    private int _points = 5;
    private int _size = DEFAULT_SIZE;
    private int _scoreWorth = 1;
    private float _life = 1f;
    private float _speed = 1f;

    private static PointF pointPool = new PointF();

    public Asteroid(){
        setScale(DEFAULT_SCALE);
    }

    public void setRandomVelocity() {
        final float velocity = Random.between(10f, 20f);
        final float angle = Random.between(0f, (float) (Math.PI * 2f));
        _velX = (float) (Math.cos(angle) * velocity);
        _velY = (float) (Math.sin(angle) * velocity);
        _velW = Random.between(-30f, 30f);
    }

    public void setMesh(int points) {
        _points = points;
        final float[] vertices = Mesh.regularPolygonGeometry(points);
        _mesh = new Mesh(vertices, GLES20.GL_LINES);
        _mesh.applyAspectRatio();
//        _mesh.setVertexAverageOrigin();
    }

    @Override
    public void update(double dt) {
        _velX0 = _velX;
        _velY0 = _velY;
        _x0 = _x;
        _y0 = _y;
        super.update(dt);
        _color = Config.Colors.FOREGROUND;
    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
    }

    public void suspend(){
        getScoring().scorePoints(_scoreWorth);
        _active = false;
    }

    public void activate(final float x, final float y, final int points, final int asteroidSize){
        _x = x;
        _y = y;
        setMesh(points);
        _size = asteroidSize;
        _life = (0.5f + _size) * 200f;
        _scoreWorth = (DEFAULT_SIZE - _size + 2) * 5;
        _speed = (5 - _size) * 6;
        setScale(4 + _size * 3);
        pointPool.x = _velX;
        pointPool.y = _velY;
        Utils.normalize(pointPool);
        _velX = pointPool.x * _speed;
        _velY = pointPool.y * _speed;
        _active = true;
    }

    public void setPool(EntityPool pool){
        _pool = pool;
    }

    @Override
    public boolean isActive() {
        return _active;
    }

    public void splitIntoTwo(float impactVelX, float impactVelY) {
        Asteroid a = _pool.pull();
        if (a != null) {
            a._velW = -2*_velW;
            a._velX = impactVelY;
            a._velY = -impactVelX;
            a.activate(_x, _y, _points, _size-1);
        }
        a = _pool.pull();
        if (a != null) {
            a._velW = 2*_velW;
            a._velX = -impactVelY;
            a._velY = impactVelX;
            a.activate(_x, _y, _points, _size-1);
        }
    }

    @Override
    public void onCollision(GLEntity that) {
        super.onCollision(that);
        if (that.isDangerous(this)) {
            final float damage = impactMagnitude * that._mass;
            _life -= damage;
            if (_life <= 0f) {
                suspend();
                if (_size > 0) {
                    splitIntoTwo(impactUnit.x, impactUnit.y);
                }
            }
        }
        _velX = -impactUnit.x * _speed;
        _velY = -impactUnit.y * _speed;
    }

    @Override
    public boolean isDangerous(GLEntity that) {
        return true;
    }
}
