package org.rendering_app.render;

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

public class TestMesh {

    @Test
    void testApplyMeshWithSmallWeights() {
        int[] rgb = {100, 150, 200};
        Color bgColor = Color.BLACK;

        Mesh.applyMesh(0.01f, 0.01f, 0.01f, rgb, bgColor);

        assertEquals(bgColor.getRed(), rgb[0]);
        assertEquals(bgColor.getGreen(), rgb[1]);
        assertEquals(bgColor.getBlue(), rgb[2]);
    }

    @Test
    void testApplyMeshWithLargeWeights() {
        int[] rgb = {100, 150, 200};
        int[] original = rgb.clone();
        Color bgColor = Color.BLACK;

        Mesh.applyMesh(0.3f, 0.3f, 0.4f, rgb, bgColor);

        assertArrayEquals(original, rgb);
    }

    @Test
    void testApplyMeshMixedWeights() {
        int[] rgb = {100, 150, 200};
        Color bgColor = Color.BLACK;

        Mesh.applyMesh(0.01f, 0.49f, 0.5f, rgb, bgColor);

        assertEquals(bgColor.getRed(), rgb[0]);
        assertEquals(bgColor.getGreen(), rgb[1]);
        assertEquals(bgColor.getBlue(), rgb[2]);
    }

    @Test
    void testApplyMeshWithDifferentBackground() {
        int[] rgb = {100, 150, 200};
        Color bgColor = Color.RED;

        Mesh.applyMesh(0.01f, 0.01f, 0.01f, rgb, bgColor);

        assertEquals(Color.RED.getRed(), rgb[0]);
        assertEquals(Color.RED.getGreen(), rgb[1]);
        assertEquals(Color.RED.getBlue(), rgb[2]);
    }

    @Test
    void testApplyMeshOverloadMethod() {
        Color bgColor = Color.BLACK;

        Mesh.applyMesh(0.01f, 0.01f, 0.01f, 100, 150, 200, bgColor);
    }
}