package com.grimezupt.asteroidsopengl.utils;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public abstract class Utils {

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
}
