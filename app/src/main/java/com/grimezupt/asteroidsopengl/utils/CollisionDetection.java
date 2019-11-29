package com.grimezupt.asteroidsopengl.utils;

import android.graphics.PointF;

import com.grimezupt.asteroidsopengl.Point3D;

public abstract class CollisionDetection {
    private static final String TAG = "CollisionDetection";
    public static final float TO_DEGREES = (float)(180.0 / Math.PI);
    public static final float TO_RADIANS = (float)(Math.PI / 180.0);
    public static final int POINTLIST_SIZE = 20;
    public static final PointF[] pointListA = new PointF[POINTLIST_SIZE]; //POOL
    public static final PointF[] pointListB = new PointF[POINTLIST_SIZE]; //POOL

    static {
        for (int i = 0; i < POINTLIST_SIZE; i++){
            pointListA[i] = new PointF();
            pointListB[i] = new PointF();
        }
    }

    public static boolean polygonVsPolygon(PointF[] polyA, PointF[] polyB){
        int j = polyB.length - 1;
        for (int i = 0; i < polyB.length; i++) {
            if (polygonVsSegment(polyA, polyB[j], polyB[i])){
                return true;
            }
            j = i;
        }
        return false; //TODO
    }

    public static boolean polygonVsSegment(PointF[] polyA, PointF segB1, PointF segB2){
        int j = polyA.length - 1;
        for (int i = 0; i < polyA.length; i++) {
            if (segmentVsSegment(polyA[j], polyA[i], segB1, segB2)){
                return true;
            }
            j = i;
        }
        return false;
    }

    public static boolean segmentVsSegment(PointF a1, PointF a2, PointF b1, PointF b2){
        final float dxa = a2.x - a1.x;
        final float dya = a2.y - a1.y;
        final float dxb = b2.x - b1.x;
        final float dyb = b2.y - b1.y;
        final float cInv = 1f / (dyb*dxa - dxb*dya);
        float uA = (dxb*(a1.y-b1.y) - dyb*(a1.x-b1.x)) * cInv;
        float uB = (dxa*(a1.y-b1.y) - dya*(a1.x-b1.x)) * cInv;
        return (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1);
    }

    public static boolean triangleVsPolygon(PointF[] triangle, PointF[] polygon) {
        for (int i = 0; i < polygon.length; i++){
            if (triangleVsPoint(triangle, polygon[i])){
                return true;
            }
        }
        return false;
    }

    public static boolean triangleVsPoint(PointF[] triangle, PointF p) {
        Utils.require(triangle.length >= 3, "Expecting at least 3 points.");
        final PointF t1 = triangle[0];
        final PointF t2 = triangle[1];
        final PointF t3 = triangle[2];

        //calculate the area of the original triangle using Cramers Rule
        final float triangleArea = Math.abs( (t2.x-t1.x)*(t3.y-t1.y) - (t3.x-t1.x)*(t2.y-t1.y) );

        final float area1 = Math.abs((t1.x-p.x)*(t2.y-p.y) - (t2.x-p.x)*(t1.y-p.y));
        final float area2 = Math.abs((t2.x-p.x)*(t3.y-p.y) - (t3.x-p.x)*(t2.y-p.y));
        final float area3 = Math.abs((t3.x-p.x)*(t1.y-p.y) - (t1.x-p.x)*(t3.y-p.y));

        return ((area1 + area2 + area3) <= triangleArea);
    }
}
