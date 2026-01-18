package org.rendering_app.render;

import org.rendering_app.math.Vector2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class TestTexture {

    private Texture texture;
    private BufferedImage testImage;

    @BeforeEach
    void setUp() {
        testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                int color = (x * 2 + y * 2) % 256;
                testImage.setRGB(x, y, new Color(color, color, color).getRGB());
            }
        }
        texture = new Texture(testImage);
    }

    @Test
    void testTextureCreation() {
        assertNotNull(texture);
        assertFalse(texture.isNull());
        assertEquals(100, texture.getWidth());
        assertEquals(100, texture.getHeight());
    }

    @Test
    void testNullTexture() {
        Texture nullTexture = new Texture();
        assertTrue(nullTexture.isNull());
        assertEquals(0, nullTexture.getWidth());
        assertEquals(0, nullTexture.getHeight());
        assertEquals(Color.BLACK, nullTexture.getPixel(0, 0));
    }

    @Test
    void testGetPixelWithCoordinates() {
        Color pixel = texture.getPixel(50, 50);
        assertNotNull(pixel);

        Color outOfBounds = texture.getPixel(150, 150);
        assertEquals(Color.BLACK, outOfBounds);

        Color negativeBounds = texture.getPixel(-10, -10);
        assertEquals(Color.BLACK, negativeBounds);
    }

    @Test
    void testGetPixelWithUV() {
        Color center = texture.getPixel(0.5f, 0.5f);
        assertNotNull(center);

        Color topLeft = texture.getPixel(0.0f, 0.0f);
        Color bottomRight = texture.getPixel(1.0f, 1.0f);
        assertNotNull(topLeft);
        assertNotNull(bottomRight);

        Color beyond = texture.getPixel(1.5f, 1.5f);
        assertNotNull(beyond); // Should be clamped
    }

    @Test
    void testSetImage() {
        Texture tex = new Texture();
        assertTrue(tex.isNull());

        tex.setImage(testImage);
        assertFalse(tex.isNull());
        assertEquals(100, tex.getWidth());

        tex.setImage(null);
        assertTrue(tex.isNull());
        assertEquals(0, tex.getWidth());
    }
}