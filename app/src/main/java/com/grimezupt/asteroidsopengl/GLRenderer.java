package com.grimezupt.asteroidsopengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Debug;

import com.grimezupt.asteroidsopengl.entities.GLText;
import com.grimezupt.asteroidsopengl.entities.World;
import com.grimezupt.asteroidsopengl.utils.SignalWindow;

import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "GLRenderer";
    static Game _game = null;

    public static float METERS_TO_SHOW_X = 160f; //160m x 90m, the entire game world in view
    public static float METERS_TO_SHOW_Y = 90f; //TODO: calculate to match screen aspect ratio
    private static final double NANOSECOND = 0.000000001d;
    private World _world = null;

    private float[] _viewportMatrix = new float[4*4];

    private float[] _bgColor;
    private long lastFrame;
    private GLText _fpsText = null;
    private static SignalWindow _fpsQueue = new SignalWindow(10);

    public GLRenderer(World world) {
        _world = world;
        lastFrame = System.nanoTime();
        _fpsText = new GLText("HELLO world", 5, 5);
        _fpsText.setScale(2f);
    }

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
        GLManager.buildProgram();
        setBgColor(Config.Colors.BACKGROUND);
    }

    @Override
    public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(final GL10 unused) {
        input();
        update();
        render();
    }

    private void input() {
        _world.input(_game.getInputs());
    }

    //trying a fixed time-step with accumulator, courtesy of
    //   https://gafferongames.com/post/fix_your_timestep/
    final double dt = 0.01;
    double accumulator = 0.0;
    double currentTime = System.nanoTime()*NANOSECOND;
    public void update() {
        final double newTime = System.nanoTime() * NANOSECOND;
        final double frameTime = newTime - currentTime;
        if (Debug.isDebuggerConnected()) {
            _world.update(0.03);
        } else {
            currentTime = newTime;
            accumulator += frameTime;
            while (accumulator >= dt) {
                _game._timer.update(dt);
                _world.update(dt);
                accumulator -= dt;
            }
        }
        _fpsQueue.put((float) frameTime);
        _fpsText.setString(String.format(Locale.ENGLISH,"%.0ffps", 1d/_fpsQueue.readAverage()));
    }

    public void render() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        final int offset = 0;
        final float left = 0;
        final float right = METERS_TO_SHOW_X;
        final float bottom = METERS_TO_SHOW_Y;
        final float top = 0;
        final float near = 0f;
        final float far = 1f;
        Matrix.orthoM(_viewportMatrix, offset, left, right, bottom, top, near, far);
        _world.render(_viewportMatrix);
        _fpsText.render(_viewportMatrix);
    }

    public void setBgColor(float[] bgColor) {
        _bgColor = bgColor;
        GLES20.glClearColor(_bgColor[0], _bgColor[1], _bgColor[2], _bgColor[3]);
    }
}
