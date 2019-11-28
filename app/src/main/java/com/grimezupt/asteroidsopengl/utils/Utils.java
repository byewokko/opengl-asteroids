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

    public static PointF normalize(float x, float y) {
        poolPoint.x = x;
        poolPoint.y = y;
        normalize(poolPoint);
        return poolPoint;
    }

    public static void normalize(final PointF pointF) {
        final float absX = Math.abs(pointF.x);
        final float absY = Math.abs(pointF.y);
        final float max = (absX > absY)? absX : absY;
        if (max == 0){
            pointF.x = 0f;
            pointF.y = 0f;
        } else {
            pointF.x = pointF.x / max;
            pointF.y = pointF.y / max;
        }
    }

    public static float weightedAvg2(float a, float wa, float b, float wb) {
        final float denom = (wa+wb == 0)? 0.5f : 1/(wa+wb);
        return (wa*a + wb*b) * denom;
    }
}
