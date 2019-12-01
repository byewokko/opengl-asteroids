package com.grimezupt.asteroidsopengl.entities;

import android.graphics.PointF;
import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.utils.Random;
import com.grimezupt.asteroidsopengl.utils.Utils;

public class Asteroid extends DynamicEntity implements Poolable {
    private static final float DEFAULT_SCALE = 10;
    private boolean _active = false;
    private EntityPool<Asteroid> _pool = null;
    private int _points = 5;

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
        super.update(dt);
        _color = Config.Colors.FOREGROUND;
    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
    }

    public void suspend(){
        _active = false;
    }

    public void activate(final float x, final float y, final int points){
        _x = x;
        _y = y;
        setMesh(points);
        _mass = DEFAULT_SCALE * 1.5f;
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
        pointPool.x = impactVelX;
        pointPool.y = impactVelY;
        // TODO: make nicer
        final float magnitude = Utils.normalize(pointPool);
        Asteroid a = _pool.pull();
        if (a != null) {
            a.setScale((float) (0.6 * _scale));
            a._velX = pointPool.y * magnitude * 1.3f;
            a._velY = -pointPool.x * magnitude * 1.3f;
            a.activate(_x, _y, _points);
        }
        a = _pool.pull();
        if (a != null) {
            a.setScale((float) (0.6 * _scale));
            a._velX = -pointPool.y * magnitude * 1.3f;
            a._velY = pointPool.x * magnitude * 1.3f;
            a.activate(_x, _y, _points);
        }
    }

    @Override
    public void onCollision(GLEntity that) {
        super.onCollision(that);
        suspend();
        if (that instanceof DynamicEntity && _scale > 0.4 * DEFAULT_SCALE) {
            splitIntoTwo(((DynamicEntity) that)._velX, ((DynamicEntity) that)._velY);
        }
    }

    @Override
    public boolean isDangerous(GLEntity that) {
        return true;
    }
}
