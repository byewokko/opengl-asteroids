package com.example.asteroidsopengl.entities;

import com.example.asteroidsopengl.Game;
import com.example.asteroidsopengl.GLManager;
import com.example.asteroidsopengl.Mesh;

import java.util.Objects;

public class GLEntity {
    public static Game _game = null;
    Mesh _mesh = null;
    float[] _color = {1.0f, 1.0f, 1.0f, 1.0f};
    float _x = 0f;
    float _y = 0f;
    float _depth = 0f;
    float _scale = 1f;
    float _roatation = 0f;

    public GLEntity() {
    }

    public void update(double dt){}

    public void render() {
        GLManager.draw(_mesh, _color);
    } // FIXME: part of the screen is covered by a random rectangle

    public void onCollision(final GLEntity that){}

    public void setColors(final float[] colors){
        Objects.requireNonNull(colors);
        assert(colors.length >= 4);
        setColors(colors[0], colors[1], colors[2], colors[3]);
    }

    private void setColors(final float r, final float g, final float b, final float a) {
        _color[0] = r;
        _color[1] = g;
        _color[2] = b;
        _color[3] = a;
    }
}
