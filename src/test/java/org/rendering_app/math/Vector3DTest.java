package org.rendering_app.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector3DTest {

    @Test
    void testConstructors() {
        Vector3D v1 = new Vector3D();
        assertEquals(0, v1.getX(), 0.001f);
        assertEquals(0, v1.getY(), 0.001f);
        assertEquals(0, v1.getZ(), 0.001f);

        Vector3D v2 = new Vector3D(1.5f, 2.5f, 3.5f);
        assertEquals(1.5f, v2.getX(), 0.001f);
        assertEquals(2.5f, v2.getY(), 0.001f);
        assertEquals(3.5f, v2.getZ(), 0.001f);
    }

    @Test
    void testGettersAndSetters() {
        Vector3D vector = new Vector3D();
        vector.setX(10);
        vector.setY(20);
        vector.setZ(30);

        assertEquals(10, vector.getX(), 0.001f);
        assertEquals(20, vector.getY(), 0.001f);
        assertEquals(30, vector.getZ(), 0.001f);
    }

    @Test
    void testAddSubtractMultiply() {
        Vector3D v1 = new Vector3D(1, 2, 3);
        Vector3D v2 = new Vector3D(4, 5, 6);

        Vector3D sum = v1.add(v2);
        assertEquals(5, sum.getX(), 0.001f);
        assertEquals(7, sum.getY(), 0.001f);
        assertEquals(9, sum.getZ(), 0.001f);

        Vector3D diff = v1.subtract(v2);
        assertEquals(-3, diff.getX(), 0.001f);
        assertEquals(-3, diff.getY(), 0.001f);
        assertEquals(-3, diff.getZ(), 0.001f);

        Vector3D scaled = v1.multiply(2.0f);
        assertEquals(2, scaled.getX(), 0.001f);
        assertEquals(4, scaled.getY(), 0.001f);
        assertEquals(6, scaled.getZ(), 0.001f);
    }

    @Test
    void testScalarProductAndDot() {
        Vector3D v1 = new Vector3D(1, 2, 3);
        Vector3D v2 = new Vector3D(4, 5, 6);

        float scalarProduct = v1.scalarProduct(v2);
        float dotProduct = v1.dot(v2);

        assertEquals(32, scalarProduct, 0.001f); // 1*4 + 2*5 + 3*6 = 32
        assertEquals(32, dotProduct, 0.001f);
        assertEquals(scalarProduct, dotProduct, 0.001f);
    }

    @Test
    void testLength() {
        Vector3D v = new Vector3D(1, 2, 2);
        float length = v.length();
        assertEquals(3, length, 0.001f); // sqrt(1² + 2² + 2²) = 3
    }

    @Test
    void testNormalize() {
        Vector3D v = new Vector3D(3, 0, 0);
        Vector3D normalized = v.normalize();

        assertEquals(1, normalized.getX(), 0.001f);
        assertEquals(0, normalized.getY(), 0.001f);
        assertEquals(0, normalized.getZ(), 0.001f);
        assertEquals(1, normalized.length(), 0.001f);

        // Нулевой вектор
        Vector3D zero = new Vector3D(0, 0, 0);
        Vector3D normalizedZero = zero.normalize();
        assertEquals(0, normalizedZero.length(), 0.001f);
    }

    @Test
    void testCrossProduct() {
        Vector3D v1 = new Vector3D(1, 0, 0);
        Vector3D v2 = new Vector3D(0, 1, 0);

        Vector3D cross = v1.cross(v2);
        assertEquals(0, cross.getX(), 0.001f);
        assertEquals(0, cross.getY(), 0.001f);
        assertEquals(1, cross.getZ(), 0.001f);

        Vector3D staticCross = Vector3D.cross(v1, v2);
        assertEquals(cross, staticCross);
    }

    @Test
    void testEqualsAndHashCode() {
        Vector3D v1 = new Vector3D(1, 2, 3);
        Vector3D v2 = new Vector3D(1, 2, 3);
        Vector3D v3 = new Vector3D(4, 5, 6);

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
        assertNotEquals(v1, v3);
    }


}