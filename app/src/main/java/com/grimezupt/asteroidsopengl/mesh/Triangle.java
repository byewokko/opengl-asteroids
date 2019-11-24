package com.grimezupt.asteroidsopengl.mesh;

public class Triangle extends Mesh {
    static float[] vertices = {
            0.0f, -0.6f, 0.0f,
            -0.2f, 0.3f, 0.0f,
            0.2f, 0.3f, 0.0f
    };

    public Triangle() {
        super(vertices);
    }
}
