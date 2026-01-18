package org.rendering_app.render;

import org.rendering_app.math.Point3D;

public class Rasterization {

    public static float getTriangleArea(Point3D a, Point3D b, Point3D c) {
        float a1 = b.getX() - a.getX();
        float a2 = c.getY() - a.getY();
        float b1 = b.getY() - a.getY();
        float b2 = c.getX() - a.getX();
        return a1 * a2 - b1 * b2;
    }

    public static float[] calculateEdgeFunctions(Point3D A, Point3D B, Point3D C, Point3D P) {
        float ABP = getTriangleArea(A, B, P);
        float BCP = getTriangleArea(B, C, P);
        float CAP = getTriangleArea(C, A, P);
        return new float[]{ABP, BCP, CAP};
    }

    public static float[] calculateBarycentricCoefficients(Point3D A, Point3D B, Point3D C, Point3D P) {
        float ABC = getTriangleArea(A, B, C);

        if (Math.abs(ABC) < 1e-6f) {
            return new float[]{0, 0, 0, Float.MAX_VALUE};
        }

        float ABP = getTriangleArea(A, B, P);
        float BCP = getTriangleArea(B, C, P);
        float CAP = getTriangleArea(C, A, P);

        float weightA = BCP / ABC;
        float weightB = CAP / ABC;
        float weightC = ABP / ABC;

        float z = A.getZ() * weightA + B.getZ() * weightB + C.getZ() * weightC;

        return new float[]{weightA, weightB, weightC, z};
    }

    public static boolean isPointInTriangle(Point3D P, Point3D A, Point3D B, Point3D C) {
        float[] weights = calculateBarycentricCoefficients(A, B, C, P);
        return weights[0] >= 0 && weights[1] >= 0 && weights[2] >= 0;
    }
}
