package com.grimezupt.asteroidsopengl.entities;

import android.app.admin.DelegatedAdminReceiver;
import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.GLManager;
import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.utils.TimerListener;

public class Projectile extends DynamicEntity implements Poolable, TimerListener {
    private static final float SIZE = 11f;
    private static final float FIRE_VELOCITY = 100f;
    private static final float LIFESPAN = 3f;
    private static Mesh mesh = null; // pool
    private boolean _active = false;
    private EntityPool<Projectile> _pool = null;

    private static int DEAD = 0;

    public Projectile() {
        initMesh();
        setColors(Config.Colors.HIGHLIGHT);
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
        getTimer().setEvent(this, DEAD, LIFESPAN);
    }

    @Override
    public void update(double dt) {
        _velX0 = _velX;
        _velY0 = _velY;
        super.update(dt);
    }

    @Override
    public void render(float[] viewportMatrix) {
        configureMatrix(viewportMatrix);
        GLManager.draw(_mesh, rotationViewportModelMatrix, _color, SIZE);
    }

    @Override
    public void suspend(){
        _active = false;
        getTimer().cancelEventsOfListener(this);
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

    @Override
    public void onTimerEvent(int type) {
        if (type == DEAD){
            suspend();
        }
    }
}
