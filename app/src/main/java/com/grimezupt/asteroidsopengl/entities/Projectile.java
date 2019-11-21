package com.grimezupt.asteroidsopengl.entities;

import android.opengl.GLES20;

import com.grimezupt.asteroidsopengl.GLManager;
import com.grimezupt.asteroidsopengl.mesh.Mesh;

public class Projectile extends GLEntity implements Suspendable {
    private static final float SIZE = 10f;
    private static final float FIRE_VELOCITY = 100f;
    private static Mesh mesh = null; // pool
    private boolean _suspended = true;

    public Projectile() {
        initMesh();
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
        _velX = (float) (velX - FIRE_VELOCITY * Math.sin(theta));
        _velY = (float) (velY + FIRE_VELOCITY * Math.cos(theta));
        _suspended = false;
    }

    @Override
    public void render(float[] viewportMatrix) {
        configureMatrix(viewportMatrix);
        GLManager.draw(_mesh, rotationViewportModelMatrix, _color, SIZE);
    }

    public void destroy(){
        _suspended = true;
    }

    void wrap() {
        if (left() > World.WIDTH || right() < 0 || bottom() > World.HEIGHT || top() < 0){
            destroy();
        }
    }

    @Override
    public boolean isSuspended() {
        return _suspended;
    }
}
