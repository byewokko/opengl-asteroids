package com.grimezupt.asteroidsopengl;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

public class GLManager {
    private static final String TAG = "GLManager";
    private static final int OFFSET = 0;

    private static int _glProgramHandle;
    private static int _colorUniformHandle;
    private static int _positionAttributeHandle;

    private final static String _vertexShaderCode =
            "attribute vec4 position;\n" + // Per-vertex position information we will pass in.
                    "void main() {\n" + // The entry point for our vertex shader.
                    "  gl_Position = position;\n" + // gl_Position is a special variable used to store the final position.
                    "}\n";
    private final static String _fragmentShaderCode =
            "precision mediump float;\n" + //we don't need high precision floats for fragments
                    "uniform vec4 color;\n" + // a constant color to apply to all pixels
                    "void main() {\n" + // The entry point for our fragment shader.
                    "  gl_FragColor = color;\n" + // Pass the color directly through the pipeline.
                    "}\n";

    public GLManager() { }

    public static void checkGLError(final String func){
        int error;
        while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR){
            Log.e(func, "glError " + error);
        }
    }

    private static int compileShader(final int type, final String shaderCode){
        assert(type == GLES20.GL_VERTEX_SHADER || type == GLES20.GL_FRAGMENT_SHADER);
        final int handle = GLES20.glCreateShader(type);
        GLES20.glShaderSource(handle, shaderCode);
        GLES20.glCompileShader(handle);
        Log.d(TAG, String.format("Shader compile log:\n0%s", GLES20.glGetShaderInfoLog(handle)));
        checkGLError("compileShader");
        return handle;
    }

    private static int linkShaders(final int vertexShader, final int fragmentShader){
        final int handle = GLES20.glCreateProgram();
        GLES20.glAttachShader(handle, vertexShader);
        GLES20.glAttachShader(handle, fragmentShader);
        GLES20.glLinkProgram(handle);
        Log.d(TAG, String.format("Shader link log:\n0%s", GLES20.glGetProgramInfoLog(handle)));
        checkGLError("linkShaders");
        return handle;
    }

    public static void buildProgram(){
        final int vertex = compileShader(GLES20.GL_VERTEX_SHADER, _vertexShaderCode);
        final int fragment = compileShader(GLES20.GL_FRAGMENT_SHADER, _fragmentShaderCode);
        _glProgramHandle = linkShaders(vertex, fragment);
        GLES20.glDeleteShader(vertex);
        GLES20.glDeleteShader(fragment);
        _positionAttributeHandle = GLES20.glGetAttribLocation(_glProgramHandle, "position");
        _colorUniformHandle = GLES20.glGetUniformLocation(_glProgramHandle, "color");
        GLES20.glUseProgram(_glProgramHandle);
        checkGLError("buildProgram");
    }

    public static void draw(Mesh mesh, float[] color) {
        setShaderColor(color);
        uploadMesh(mesh._vertexBuffer);
        drawMesh(mesh._drawMode, mesh._vertexCount);
    }

    private static void drawMesh(final int drawMode, final int vertexCount) {
        assert(drawMode == GLES20.GL_TRIANGLES ||
                drawMode == GLES20.GL_POINTS ||
                drawMode == GLES20.GL_LINES);
        GLES20.glDrawArrays(drawMode, OFFSET, vertexCount);
        GLES20.glDisableVertexAttribArray(_positionAttributeHandle);
        checkGLError("drawMesh");
    }

    private static void uploadMesh(final FloatBuffer vertexBuffer) {
        final boolean NORMALIZED = false;
        GLES20.glEnableVertexAttribArray(GLManager._positionAttributeHandle);
        GLES20.glVertexAttribPointer(GLManager._positionAttributeHandle,
                Mesh.COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                NORMALIZED,
                Mesh.VERTEX_STRIDE,
                vertexBuffer);
        checkGLError("uploadMesh");
    }

    private static void setShaderColor(final float[] color) {
        final int COUNT = 1;
        GLES20.glUniform4fv(GLManager._colorUniformHandle, COUNT, color, OFFSET);
        checkGLError("setShaderColor");
    }
}