package org.rendering_app.math;

public class GraphicConveyor {


    private static Matrix4D getRotateMatrix(float phiDegrees, float psiDegrees, float thetaDegrees) {
        float phi = (float) Math.toRadians(phiDegrees);
        float psi = (float) Math.toRadians(psiDegrees);
        float theta = (float) Math.toRadians(thetaDegrees);


        // Вращение вокруг z
        float[][] rz = {
                {(float) Math.cos(phi), -(float) Math.sin(phi), 0, 0},
                {(float) Math.sin(phi), (float) Math.cos(phi), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        // вращение по y
        float[][] ry = {
                {(float) Math.cos(psi), 0, (float) Math.sin(psi), 0},
                {0, 1, 0, 0},
                {-(float) Math.sin(psi), 0, (float) Math.cos(psi), 0},
                {0, 0, 0, 1}
        };


        // вращение по x
        float[][] rx = {
                {1, 0, 0, 0},
                {0, (float) Math.cos(theta), -(float) Math.sin(theta), 0},
                {0, (float) Math.sin(theta), (float) Math.cos(theta), 0},
                {0, 0, 0, 1}
        };


        Matrix4D Rx = new Matrix4D(rx);
        Matrix4D Ry = new Matrix4D(ry);
        Matrix4D Rz = new Matrix4D(rz);


        return Rz.multiply(Ry.multiply(Rx));
    }



    private static Matrix4D getScaleMatrix(float sx, float sy, float sz) {
        float[][] s = {
                {sx, 0, 0, 0},
                {0, sy, 0, 0},
                {0, 0, sz, 0},
                {0, 0, 0, 1}};


        return new Matrix4D(s);
    }


    private static Matrix4D getTranslateMatrix(float tx, float ty, float tz) {
        float[][] t = {
                {1, 0, 0, tx},
                {0, 1, 0, ty},
                {0, 0, 1, tz},
                {0, 0, 0, 1}};


        return new Matrix4D(t);
    }

}
