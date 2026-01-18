package org.rendering_app.render;

import org.rendering_app.math.Vector2D;

import java.awt.Color;

public class Texturing {

    public static void applyTexturing(Vector2D[] textureVectors,
                                      Texture texture,
                                      float weightA, float weightB, float weightC,
                                      int[] rgb) {
        if (textureVectors == null || textureVectors.length < 3 || texture == null || texture.isNull()) return;

        Color texColor = getInterpolatedPixel(textureVectors, texture, weightA, weightB, weightC);
        rgb[0] = texColor.getRed();
        rgb[1] = texColor.getGreen();
        rgb[2] = texColor.getBlue();
    }

    public static void applyTexturing(Vector2D[] textureVectors,
                                      Texture texture,
                                      float weightA, float weightB, float weightC,
                                      int r, int g, int b) {
        int[] rgb = new int[]{r, g, b};
        applyTexturing(textureVectors, texture, weightA, weightB, weightC, rgb);
    }

    private static Color getInterpolatedPixel(Vector2D[] textureVectors,
                                              Texture texture,
                                              float weightA, float weightB, float weightC) {
        float u = weightA * textureVectors[0].getX()
                + weightB * textureVectors[1].getX()
                + weightC * textureVectors[2].getX();

        float v = weightA * textureVectors[0].getY()
                + weightB * textureVectors[1].getY()
                + weightC * textureVectors[2].getY();

        return texture.getPixel(u, v);
    }
}
