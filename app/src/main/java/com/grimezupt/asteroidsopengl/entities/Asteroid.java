package com.grimezupt.asteroidsopengl.entities;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.utils.Random;
import com.grimezupt.asteroidsopengl.utils.Utils;

public class Asteroid extends GLEntity implements Suspendable {
    private static final float DEFAULT_SCALE = 10;
    private boolean _suspended = false;
    private EntityPool<Asteroid> _pool = null;

    public Asteroid(){
        _suspended = true;
        setScale(DEFAULT_SCALE);
    }

    public Asteroid(final float x, final float y, final int points) {
        setScale(DEFAULT_SCALE);
        _x = x;
        _y = y;
        setRandomVelocity();
        setMesh(points);
    }

    public void setRandomVelocity() {
        final float velocity = Random.between(10f, 15f);
        final float angle = Random.between(0f, (float) (Math.PI * 2f));
        _velX = (float) (Math.cos(angle) * velocity);
        _velY = (float) (Math.sin(angle) * velocity);
        _velW = Random.between(-30f, 30f);
    }

    public void setMesh(int points) {
        final float[] vertices = Mesh.regularPolygonGeometry(points);
        _mesh = new Mesh(vertices, GLES20.GL_LINES);
        _mesh.applyAspectRatio();
    }

    @Override
    public void update(double dt) {
        super.update(dt);
//        wrap();
    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
    }

    public void destroy(){
        _suspended = true;
        // TODO: make nicer
        if (_xScale > 0.3 * DEFAULT_SCALE) {
            final float velocity = (float) Utils.getVectorMagnitude(_velX, _velY);
            final float theta = _rotation * RADIANS;
            Asteroid a = _pool.pull();
            if (a != null) {
                a.setScale((float) (0.6 * _xScale));
                a._velX = (float) -Math.sin(theta) * velocity * 1.5f;
                a._velY = (float) Math.cos(theta) * velocity * 1.5f;
                a.activate(_x, _y, 5);
            }
            a = _pool.pull();
            if (a != null) {
                a.setScale((float) (0.6 * _xScale));
                a._velX = (float) Math.sin(theta) * velocity * 1.5f;
                a._velY = (float) -Math.cos(theta) * velocity * 1.5f;
                a.activate(_x, _y, 5);
            }
        }
    }

    public void activate(final float x, final float y, final int points){
        _x = x;
        _y = y;
        setMesh(points);
        setRandomVelocity();
        _suspended = false;
    }

    public void setPool(EntityPool<Asteroid> pool){
        _pool = pool;
    }

    @Override
    public boolean isSuspended() {
        return _suspended;
    }

    @Override
    public void onCollision(GLEntity that) {
        super.onCollision(that);
        destroy();
    }
}
