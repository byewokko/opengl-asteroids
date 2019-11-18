package com.grimezupt.asteroidsopengl.utils;

import android.graphics.Point;
import android.graphics.PointF;

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
}
