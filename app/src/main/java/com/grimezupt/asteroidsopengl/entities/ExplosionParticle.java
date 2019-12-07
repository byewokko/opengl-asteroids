package com.grimezupt.asteroidsopengl.entities;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.GLManager;
import com.grimezupt.asteroidsopengl.mesh.Mesh;
import com.grimezupt.asteroidsopengl.utils.Random;
import com.grimezupt.asteroidsopengl.utils.TimerListener;

public class ExplosionParticle extends DynamicEntity implements Poolable, TimerListener {
    public static final float VELOCITY_MEAN = 100f;
    public static final float SIZE_CHANGE = 10f;
    public static final float VELOCITY_SD = 30f;
    public static final float INIT_POINT_SIZE = 2.5f;
    private boolean _active = false;
    private EntityPool _pool = null;
    private float _pointSize = INIT_POINT_SIZE;
    private static final float DRAG = 9f;
    private static final float LIFESPAN = 0.7f;

    private static Mesh mesh = null;

    public ExplosionParticle() {
        initMesh();
        setColors(Config.Colors.HIGHLIGHT);
        _wrap = false;
    }

    private void initMesh() {
        if (mesh == null) {
            final float[] vertices = new float[Mesh.COORDS_PER_VERTEX];
            mesh = new Mesh(vertices, GLES20.GL_POINTS);
        }
        _mesh = mesh;
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
    public void suspend() {
        _active = false;
    }

    @Override
    public boolean isDangerous(GLEntity that) {
        return false;
    }

    public void activate(final float x, final float y, final float theta){
        getTimer().setEvent(this, 0, LIFESPAN);
        _x = x;
        _y = y;
        _pointSize = INIT_POINT_SIZE;
        final float velocity = Random.norm(VELOCITY_MEAN, VELOCITY_SD);
        _velX = (float) (velocity * Math.sin(theta));
        _velY = (float) (-velocity * Math.cos(theta));
        _active = true;
    }

    @Override
    public void onTimerEvent(int type) {
        suspend();
    }

    @Override
    public void update(double dt) {
        _velX *= (1f - DRAG*dt);
        _velY *= (1f - DRAG*dt);
        _pointSize += SIZE_CHANGE*dt;
        super.update(dt);
    }

    @Override
    public void render(float[] viewportMatrix) {
        configureMatrix(viewportMatrix);
        GLManager.draw(_mesh, rotationViewportModelMatrix, _color, _pointSize);
    }
}
