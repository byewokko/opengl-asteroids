package com.grimezupt.asteroidsopengl.entities;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.GLManager;
import com.grimezupt.asteroidsopengl.mesh.Mesh;

public class Projectile extends DynamicEntity implements Poolable {
    private static final float SIZE = 10f;
    private static final float FIRE_VELOCITY = 100f;
    private static final float LIFESPAN = 3f;
    private static Mesh mesh = null; // pool
    private boolean _active = false;
    private double _lifespan = 0d;
    private EntityPool<Projectile> _pool = null;

    public Projectile() {
        initMesh();
        _mass = 2f;
    }

    private void initMesh() {
        if (mesh == null) {
            final float[] vertices = new float[Mesh.COORDS_PER_VERTEX];
            mesh = new Mesh(vertices, GLES20.GL_POINTS);
        }
        _mesh = mesh;
    }

    public void activate(final float x, final float y, final float velX, final float velY, final float theta){
        _x = x;
        _y = y;
        _velX = (float) (velX + FIRE_VELOCITY * Math.sin(theta));
        _velY = (float) (velY - FIRE_VELOCITY * Math.cos(theta));
        _active = true;
        _lifespan = LIFESPAN;
    }

    @Override
    public void update(double dt) {
        _velX0 = _velX;
        _velY0 = _velY;
        super.update(dt);
        _lifespan -= dt;
        if (_lifespan <= 0){
            suspend();
        }
    }

    @Override
    public void render(float[] viewportMatrix) {
        configureMatrix(viewportMatrix);
        GLManager.draw(_mesh, rotationViewportModelMatrix, _color, SIZE);
    }

    @Override
    public void suspend(){
        _active = false;
    }

    @Override
    public void onCollision(GLEntity that) {
        super.onCollision(that);
        suspend();
    }

    @Override
    public boolean isActive() {
        return _active;
    }

    @Override
    public void setPool(EntityPool pool) {
        _pool = pool;
    }

    @Override
    public boolean isDangerous(GLEntity that) {
        return true;
    }
}
