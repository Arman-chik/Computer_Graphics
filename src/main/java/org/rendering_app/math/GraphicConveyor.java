package org.rendering_app.math;

import org.rendering_app.model.Model;

public class GraphicConveyor {


    public static void rotateScaleTranslate(Model model, float sx, float sy, float sz,
                                            float phi, float psi, float theta, float tx, float ty, float tz) {


        Matrix4D scaleMatrix = getScaleMatrix(sx, sy, sz);
        Matrix4D rotateMatrix = getRotateMatrix(phi, psi, theta);
        Matrix4D translateMatrix = getTranslateMatrix(tx, ty, tz);


        // T * R * S
        Matrix4D transformMatrix = translateMatrix.multiply(rotateMatrix.multiply(scaleMatrix));



        for (Vector3D vertex : model.getVertices()) {
            Vector4D v4 = MathCast.toVector4D(vertex);
            Vector4D t = transformMatrix.multiply(v4);

            float w = t.getW();
            if (w == 0) w = 1.0f;

            vertex.setX(t.getX() / w);
            vertex.setY(t.getY() / w);
            vertex.setZ(t.getZ() / w);
        }
    }



    public static void translate(Model model, float T_x, float T_y, float T_z) {
        Matrix4D translateMatrix = getTranslateMatrix(T_x, T_y, T_z);

        for (Vector3D vertex :  model.getVertices()){

            Vector4D v4 = MathCast.toVector4D(vertex);
            Vector4D t = translateMatrix.multiply(v4);

            float w = t.getW();

            if (w == 0){
                w = 1.0f;
            }

            vertex.setX(t.getX() / w);
            vertex.setY(t.getY() / w);
            vertex.setZ(t.getZ() / w);
        }
    }


    public static void rotate(Model model, float phi, float psi, float theta) {
        Matrix4D rotateMatrix = getRotateMatrix(phi, psi, theta);

        for (Vector3D vertex : model.getVertices()) {
            Vector4D v4 = MathCast.toVector4D(vertex);
            Vector4D r = rotateMatrix.multiply(v4);

            float w = r.getW();
            if (w == 0) {
                w = 1.0f;
            }

            vertex.setX(r.getX() / w);
            vertex.setY(r.getY() / w);
            vertex.setZ(r.getZ() / w);
        }
    }


    public static void scale(Model model, float sx, float sy, float sz) {
        Matrix4D scaleMatrix = getScaleMatrix(sx, sy, sz);

        for (Vector3D vertex : model.getVertices()) {
            Vector4D v4 = MathCast.toVector4D(vertex);
            Vector4D s = scaleMatrix.multiply(v4);

            float w = s.getW();

            if (w == 0) {
                w = 1.0f;
            }

            vertex.setX(s.getX() / w);
            vertex.setY(s.getY() / w);
            vertex.setZ(s.getZ() / w);
        }
    }


    private static Matrix4D getRotateMatrix(float phiDegrees, float psiDegrees, float thetaDegrees) {
        float phi = (float) Math.toRadians(phiDegrees);
        float psi = (float) Math.toRadians(psiDegrees);
        float theta = (float) Math.toRadians(thetaDegrees);


        // Вращение вокруг z
        float[][] rz = {
                {(float) Math.cos(phi), (float) Math.sin(phi), 0, 0},
                {-(float) Math.sin(phi), (float) Math.cos(phi), 0, 0},
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



    public static Matrix4D lookAt(Vector3D eye, Vector3D target, Vector3D up) {
        Vector3D zAxis = target.subtract(eye).normalize();  //направление от цели к камере

        if (Math.abs(zAxis.dot(up)) > 0.99f) {
            up = (zAxis.getX() == 0) ? new Vector3D(0, 0, 1) : new Vector3D(1, 0, 0);
        }

        // строим ортонормированный базис
        Vector3D xAxis = Vector3D.cross(up, zAxis).normalize(); //вправо
        Vector3D yAxis = Vector3D.cross(zAxis, xAxis).normalize();  // выерх


        float[][] view = {
                {xAxis.getX(), xAxis.getY(), xAxis.getZ(), -xAxis.dot(eye)},
                {yAxis.getX(), yAxis.getY(), yAxis.getZ(), -yAxis.dot(eye)},
                {zAxis.getX(), zAxis.getY(), zAxis.getZ(), -zAxis.dot(eye)},
                {0, 0, 0, 1}
        };


        return new Matrix4D(view);
    }

    public static Matrix4D lookAt(Vector3D eye, Vector3D target) {
        return lookAt(eye, target, new Vector3D(0, 1, 0));
    }


    public static Matrix4D perspective(float fov, float aspectRatio, float nearPlane, float farPlane) {
        float tanHalfFov = (float) Math.tan(fov / 2.0f);

        float[][] p = {
                {1.0f / (aspectRatio * tanHalfFov), 0, 0, 0},
                {0, 1.0f / tanHalfFov, 0, 0},
                {0, 0, (farPlane + nearPlane) / (farPlane - nearPlane),
                        2.0f * nearPlane * farPlane / (nearPlane - farPlane)},
                {0, 0, 1, 0}
        };

        return new Matrix4D(p);
    }
}
