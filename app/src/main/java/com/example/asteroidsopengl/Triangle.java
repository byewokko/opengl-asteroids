package com.example.asteroidsopengl;

public class Triangle extends Mesh {
    static float vertices[] = {
             0.0f,  0.4f,  0.0f,
            -0.2f, -0.2f,  0.0f,
             0.2f, -0.3f,  0.0f
    };

    public Triangle() {
        super(vertices);
    }
}
