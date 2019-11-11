package com.grimezupt.asteroidsopengl.mesh;

import android.opengl.GLES20;

public class GLEquiPolygon extends Mesh {
    private GLEquiPolygon(final float[] geometry) {
        super(geometry, GLES20.GL_LINES);
    }

    public static GLEquiPolygon build(final int points, final float radius) {
        assert(points>=3);
        final int numVerts = points*2;
        final float[] geometry = new float[numVerts * Mesh.COORDS_PER_VERTEX];

        double step = 2.0*Math.PI/points;
        int i = 0, point = 0;
        while(point < points) { //generate verts on circle, 2 per point
            double theta = point * step;
            geometry[i++] = (float)(Math.cos(theta) * radius); //X
            geometry[i++] = (float)(Math.sin(theta) * radius); //Y
            geometry[i++] = 0f;                                //Z
            point++;
            theta = point * step;
            geometry[i++] = (float)(Math.cos(theta) * radius); //X
            geometry[i++] = (float)(Math.sin(theta) * radius); //Y
            geometry[i++] = 0f;                                //Z
        }
        return new GLEquiPolygon(geometry);
    }
}
