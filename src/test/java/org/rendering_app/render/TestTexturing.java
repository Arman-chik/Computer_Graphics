package org.rendering_app.render;

import org.rendering_app.math.Vector2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class TestTexturing {

    private Texture texture;
    private Vector2D[] textureVectors;

    @BeforeEach
    void setUp() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, Color.RED.getRGB());
        img.setRGB(1, 0, Color.GREEN.getRGB());
        img.setRGB(0, 1, Color.BLUE.getRGB());
        img.setRGB(1, 1, Color.WHITE.getRGB());

        texture = new Texture(img);

        textureVectors = new Vector2D[] {
                new Vector2D(0.0f, 0.0f),  // Red
                new Vector2D(1.0f, 0.0f),  // Green
                new Vector2D(0.0f, 1.0f)   // Blue
        };
    }

    @Test
    void testApplyTexturingWithNullTexture() {
        int[] rgb = {100, 100, 100};
        int[] original = rgb.clone();

        Texturing.applyTexturing(textureVectors, null, 0.33f, 0.33f, 0.34f, rgb);

        assertArrayEquals(original, rgb);
    }

    @Test
    void testApplyTexturingWithNullVectors() {
        int[] rgb = {100, 100, 100};

        Texturing.applyTexturing(null, texture, 0.33f, 0.33f, 0.34f, rgb);

        assertEquals(100, rgb[0]);
    }

    @Test
    void testApplyTexturingWithValidInput() {
        int[] rgb = {0, 0, 0};

        Texturing.applyTexturing(textureVectors, texture, 1.0f, 0.0f, 0.0f, rgb);

        assertEquals(Color.RED.getRed(), rgb[0]);
        assertEquals(Color.RED.getGreen(), rgb[1]);
        assertEquals(Color.RED.getBlue(), rgb[2]);
    }

    @Test
    void testApplyTexturingWithMixedWeights() {
        int[] rgb = {0, 0, 0};

        Texturing.applyTexturing(textureVectors, texture, 0.33f, 0.33f, 0.34f, rgb);

        assertTrue(rgb[0] != 0 || rgb[1] != 0 || rgb[2] != 0);
    }

    @Test
    void testGetInterpolatedPixel() {
        Vector2D[] exactVectors = new Vector2D[] {
                new Vector2D(0.0f, 0.0f),
                new Vector2D(0.0f, 0.0f),
                new Vector2D(0.0f, 0.0f)
        };

        Color result = texture.getPixel(0.0f, 0.0f);
        assertEquals(Color.RED, result);
    }
}
