package org.rendering_app.render;

import org.rendering_app.math.MathCast;
import org.rendering_app.math.Point3D;
import org.rendering_app.math.Vector3D;

import java.awt.Color;
import java.util.List;

public class Illumination {

    public static void applyIllumination(Point3D[] normalVectors,
                                         Point3D worldPoint,
                                         List<Light> lights,
                                         float weightA, float weightB, float weightC,
                                         int[] rgb) {
        if (normalVectors == null || normalVectors.length < 3) return;
        if (lights == null || lights.isEmpty()) return;

        float k = 0.4f;
        float intensity = 1.0f / lights.size();

        rgb[0] = (int) (rgb[0] * (1 - k));
        rgb[1] = (int) (rgb[1] * (1 - k));
        rgb[2] = (int) (rgb[2] * (1 - k));

        for (Light light : lights) {
            float illumination = calculateIllumination(normalVectors, light.getPosition(), worldPoint, weightA, weightB, weightC);
            Color lightColor = light.getColor();

            rgb[0] += (int) (intensity * k * illumination * lightColor.getRed());
            rgb[1] += (int) (intensity * k * illumination * lightColor.getGreen());
            rgb[2] += (int) (intensity * k * illumination * lightColor.getBlue());

            rgb[0] = Math.max(0, Math.min(255, rgb[0]));
            rgb[1] = Math.max(0, Math.min(255, rgb[1]));
            rgb[2] = Math.max(0, Math.min(255, rgb[2]));
        }
    }

    public static void applyIllumination(Point3D[] normalVectors,
                                         Point3D worldPoint,
                                         List<Light> lights,
                                         float weightA, float weightB, float weightC,
                                         int r, int g, int b) {
        int[] rgb = new int[]{r, g, b};
        applyIllumination(normalVectors, worldPoint, lights, weightA, weightB, weightC, rgb);
    }

    private static float calculateIllumination(Point3D[] normalVectors,
                                               Vector3D lightPos,
                                               Point3D worldPoint,
                                               float weightA, float weightB, float weightC) {
        Vector3D normalA = MathCast.toVector3D(normalVectors[0]);
        Vector3D normalB = MathCast.toVector3D(normalVectors[1]);
        Vector3D normalC = MathCast.toVector3D(normalVectors[2]);

        Vector3D interpolatedNormal = normalA.multiply(weightA)
                .add(normalB.multiply(weightB))
                .add(normalC.multiply(weightC))
                .normalize();

        Vector3D pointVector = new Vector3D(worldPoint.getX(), worldPoint.getY(), worldPoint.getZ());
        Vector3D lightVector = pointVector.subtract(lightPos).normalize();

        float dotProduct = interpolatedNormal.dot(lightVector);
        return Math.max(0.0f, -dotProduct);
    }
}
