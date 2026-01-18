package org.rendering_app.render;

import org.rendering_app.math.Point3D;
import org.rendering_app.math.Vector3D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestIllumination {

    private List<Light> lights;
    private Point3D[] normals;
    private Point3D worldPoint;

    @BeforeEach
    void setUp() {
        lights = new ArrayList<>();
        lights.add(new Light(Color.WHITE, new Vector3D(0, 0, 10)));

        normals = new Point3D[] {
                new Point3D(0, 0, 1),
                new Point3D(0, 0, 1),
                new Point3D(0, 0, 1)
        };

        worldPoint = new Point3D(0, 0, 0);
    }

    @Test
    void testApplyIlluminationWithNullNormals() {
        int[] rgb = {100, 100, 100};
        int[] original = rgb.clone();

        Illumination.applyIllumination(null, worldPoint, lights, 0.33f, 0.33f, 0.34f, rgb);

        assertArrayEquals(original, rgb);
    }

    @Test
    void testApplyIlluminationWithEmptyLights() {
        int[] rgb = {100, 100, 100};
        int[] original = rgb.clone();

        Illumination.applyIllumination(normals, worldPoint, new ArrayList<>(),
                0.33f, 0.33f, 0.34f, rgb);

        assertArrayEquals(original, rgb);
    }

    @Test
    void testApplyIlluminationWithValidInput() {
        int[] rgb = {100, 100, 100};

        Illumination.applyIllumination(normals, worldPoint, lights,
                0.33f, 0.33f, 0.34f, rgb);

        assertTrue(rgb[0] != 100 || rgb[1] != 100 || rgb[2] != 100);
        assertTrue(rgb[0] >= 0 && rgb[0] <= 255);
        assertTrue(rgb[1] >= 0 && rgb[1] <= 255);
        assertTrue(rgb[2] >= 0 && rgb[2] <= 255);
    }

    @Test
    void testApplyIlluminationWithOverflowProtection() {
        int[] rgb = {300, -50, 1000}; // Extreme values

        Illumination.applyIllumination(normals, worldPoint, lights,
                0.33f, 0.33f, 0.34f, rgb);

        assertTrue(rgb[0] >= 0 && rgb[0] <= 255);
        assertTrue(rgb[1] >= 0 && rgb[1] <= 255);
        assertTrue(rgb[2] >= 0 && rgb[2] <= 255);
    }

    @Test
    void testCalculateIlluminationDirectLight() {
        int[] rgb = {100, 100, 100};

        Illumination.applyIllumination(normals, worldPoint, lights,
                0.33f, 0.33f, 0.34f, rgb);

        assertTrue(rgb[0] != 100 || rgb[1] != 100 || rgb[2] != 100);
    }

    @Test
    void testCalculateIlluminationPerpendicularLight() {
        Point3D[] sideNormals = new Point3D[] {
                new Point3D(1, 0, 0),
                new Point3D(1, 0, 0),
                new Point3D(1, 0, 0)
        };

        int[] rgb = {100, 100, 100};

        Illumination.applyIllumination(sideNormals, worldPoint, lights,
                0.33f, 0.33f, 0.34f, rgb);
        assertTrue(rgb[0] >= 0 && rgb[0] <= 255);
    }
}