package org.rendering_app.render;

import java.awt.Color;

public class Mesh {

    public static void applyMesh(float weightA, float weightB, float weightC, int[] rgb, Color backgroundColor) {
        if (weightA < 0.02f || weightB < 0.02f || weightC < 0.02f) {
            rgb[0] = backgroundColor.getRed();
            rgb[1] = backgroundColor.getGreen();
            rgb[2] = backgroundColor.getBlue();
        }
    }

    public static void applyMesh(float weightA, float weightB, float weightC, int r, int g, int b, Color backgroundColor) {
        int[] rgb = new int[]{r, g, b};
        applyMesh(weightA, weightB, weightC, rgb, backgroundColor);
    }
}
