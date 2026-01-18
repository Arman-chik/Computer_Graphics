package org.rendering_app.render;

import org.rendering_app.math.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestRasterization {

    @Test
    void testGetTriangleArea() {
        Point3D a = new Point3D(0, 0, 0);
        Point3D b = new Point3D(4, 0, 0);
        Point3D c = new Point3D(0, 3, 0);

        float area = Rasterization.getTriangleArea(a, b, c);
        assertEquals(12.0f, area, 0.001f); // 4*3 = 12
    }

    @Test
    void testGetTriangleAreaDegenerate() {
        Point3D a = new Point3D(0, 0, 0);
        Point3D b = new Point3D(0, 0, 0);
        Point3D c = new Point3D(0, 0, 0);

        float area = Rasterization.getTriangleArea(a, b, c);
        assertEquals(0.0f, area, 0.001f);
    }

    @Test
    void testCalculateEdgeFunctions() {
        Point3D a = new Point3D(0, 0, 0);
        Point3D b = new Point3D(4, 0, 0);
        Point3D c = new Point3D(0, 3, 0);
        Point3D p = new Point3D(1, 1, 0);

        float[] edges = Rasterization.calculateEdgeFunctions(a, b, c, p);
        assertEquals(3, edges.length);
        assertTrue(edges[0] > 0);
        assertTrue(edges[1] > 0);
        assertTrue(edges[2] > 0);
    }

    @Test
    void testCalculateBarycentricCoefficients() {
        Point3D a = new Point3D(0, 0, 0);
        Point3D b = new Point3D(4, 0, 0);
        Point3D c = new Point3D(0, 3, 0);
        Point3D p = new Point3D(1, 1, 0);

        float[] coeffs = Rasterization.calculateBarycentricCoefficients(a, b, c, p);
        assertEquals(4, coeffs.length);

        float sum = coeffs[0] + coeffs[1] + coeffs[2];
        assertEquals(1.0f, sum, 0.001f);

        assertTrue(coeffs[0] > 0);
        assertTrue(coeffs[1] > 0);
        assertTrue(coeffs[2] > 0);
    }

    @Test
    void testCalculateBarycentricCoefficientsDegenerateTriangle() {
        Point3D a = new Point3D(0, 0, 0);
        Point3D b = new Point3D(0, 0, 0);
        Point3D c = new Point3D(0, 0, 0);
        Point3D p = new Point3D(1, 1, 0);

        float[] coeffs = Rasterization.calculateBarycentricCoefficients(a, b, c, p);
        assertEquals(4, coeffs.length);
        assertEquals(Float.MAX_VALUE, coeffs[3]);
    }

    @Test
    void testIsPointInTriangle() {
        Point3D a = new Point3D(0, 0, 0);
        Point3D b = new Point3D(4, 0, 0);
        Point3D c = new Point3D(0, 3, 0);

        Point3D inside = new Point3D(1, 1, 0);
        Point3D outside = new Point3D(5, 5, 0);
        Point3D edge = new Point3D(2, 0, 0);

        assertTrue(Rasterization.isPointInTriangle(inside, a, b, c));
        assertFalse(Rasterization.isPointInTriangle(outside, a, b, c));
        assertTrue(Rasterization.isPointInTriangle(edge, a, b, c)); // On edge is considered inside
    }

    @Test
    void testBarycentricInterpolationZ() {
        Point3D a = new Point3D(0, 0, 1);
        Point3D b = new Point3D(4, 0, 2);
        Point3D c = new Point3D(0, 3, 3);
        Point3D p = new Point3D(1, 1, 0);

        float[] coeffs = Rasterization.calculateBarycentricCoefficients(a, b, c, p);
        float interpolatedZ = coeffs[3];

        float expected = 1 * coeffs[0] + 2 * coeffs[1] + 3 * coeffs[2];
        assertEquals(expected, interpolatedZ, 0.001f);
    }
}