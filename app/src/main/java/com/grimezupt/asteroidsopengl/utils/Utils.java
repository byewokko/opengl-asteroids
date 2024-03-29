package com.grimezupt.asteroidsopengl.utils;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public abstract class Utils {

    private static PointF poolPoint = new PointF();

    public static float clamp(final float value, final float min, final float max){
        if (value < min){
            return min;
        }
        if (value > max){
            return max;
        }
        return value;
    }

    public static double getVectorMagnitude(PointF point) {
        return getVectorMagnitude(point.x, point.y);
    }

    public static double getVectorMagnitude(float x, float y) {
        return Math.sqrt(x*x + y*y);
    }

    public static double getVectorMagnitude(Point point) {
        return Math.sqrt(point.x*point.x + point.y*point.y);
    }

    public static void expect(final boolean condition, final String tag) {
        Utils.expect(condition, tag, "Expectation was broken.");
    }
    public static void expect(final boolean condition, final String tag, final String message) {
        if(!condition) {
            Log.e(tag, message);
        }
    }

    public static void require(final boolean condition) {
        Utils.require(condition, "Assertion failed!");
    }
    public static void require(final boolean condition, final String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static boolean isBetween(float low, float high, float var) {
        return (var >= low && var <= high);
    }

    public static float normalize(final PointF pointF) {
        float magnitude = 0;
        if (pointF.x != 0f || pointF.y != 0f) {
            magnitude = (float) Math.sqrt(pointF.x * pointF.x + pointF.y * pointF.y);
            pointF.x /= magnitude;
            pointF.y /= magnitude;
        }
        return magnitude;
    }

    public static float weightedAvg2(float a, float wa, float b, float wb) {
        final float denom = (wa+wb == 0)? 0.5f : 1/(wa+wb);
        return (wa*a + wb*b) * denom;
    }

    public static boolean squareWaveBoolean(final double time, final float period, final float dutyCycle){
        return (time % period < period*dutyCycle);
    }

    public static void negateVector(PointF vector) {
        vector.x *= -1;
        vector.y *= -1;
    }

    public static double sawtoothWave(double time, float frequency) {
        return (time * frequency) % 1d;
    }
}
