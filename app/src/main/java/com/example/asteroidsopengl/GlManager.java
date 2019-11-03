package com.example.asteroidsopengl;

import android.opengl.GLES20;
import android.util.Log;

public class GlManager {
    private static final String TAG = "GlManager";

    private static int _glProgramHandle;
    private static int _colorUniformHandle;
    private static int _positionAttributeHandle;

    private final static String vertexShaderCode =
            "attribute vec4 position;\n" + // Per-vertex position information we will pass in.
                    "void main() {\n" + // The entry point for our vertex shader.
                    "  gl_Position = position;\n" + // gl_Position is a special variable used to store the final position.
                    "}\n";
    private final static String fragmentShaderCode =
            "precision mediump float;\n" + //we don't need high precision floats for fragments
                    "uniform vec4 color;\n" + // a constant color to apply to all pixels
                    "void main() {\n" + // The entry point for our fragment shader.
                    "  gl_FragColor = color;\n" + // Pass the color directly through the pipeline.
                    "}\n";

    public GlManager() {

    }

    public static void checkGLError(final String func){
        int error;
        while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR){
            Log.e(func, "glError " + error);
        }
    }
}
