package com.grimezupt.asteroidsopengl.entities;

public abstract class DynamicEntity extends GLEntity {
    public static final float DRAG = 3f;
    public float _velX = 0f;
    public float _velY = 0f;
    public float _velW = 0f; //angular velocity
    public float _mass = 1f;
    public float _drag = 1f;


    @Override
    public void update(double dt){
        _x += _velX * dt;
        _y += _velY * dt;
        _velX *= (1f - dt*DRAG/(_mass*_mass));
        _velY *= (1f - dt*DRAG/(_mass*_mass));
        _rotation += _velW * dt;
        wrap();
    }

    void wrap() {
        if (left() > World.WIDTH){
            setRight(0);
        } else if (right() < 0){
            setLeft(World.WIDTH);
        }
        if (top() > World.HEIGHT){
            setBottom(0);
        } else if (bottom() < 0){
            setTop(World.HEIGHT);
        }
    }
}
