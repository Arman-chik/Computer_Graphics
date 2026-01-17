package org.rendering_app.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector4DTest {

    @Test
    void testConstructors() {
        Vector4D v1 = new Vector4D();
        assertEquals(0, v1.getX(), 0.001f);
        assertEquals(0, v1.getY(), 0.001f);
        assertEquals(0, v1.getZ(), 0.001f);
        assertEquals(0, v1.getW(), 0.001f);

        Vector4D v2 = new Vector4D(1, 2, 3, 4);
        assertEquals(1, v2.getX(), 0.001f);
        assertEquals(2, v2.getY(), 0.001f);
        assertEquals(3, v2.getZ(), 0.001f);
        assertEquals(4, v2.getW(), 0.001f);

        Vector3D v3 = new Vector3D(5, 6, 7);
        Vector4D v4 = new Vector4D(v3, 1.0f);
        assertEquals(5, v4.getX(), 0.001f);
        assertEquals(6, v4.getY(), 0.001f);
        assertEquals(7, v4.getZ(), 0.001f);
        assertEquals(1, v4.getW(), 0.001f);
    }

    @Test
    void testGettersAndSetters() {
        Vector4D vector = new Vector4D();
        vector.setX(10);
        vector.setY(20);
        vector.setZ(30);
        vector.setW(40);

        assertEquals(10, vector.getX(), 0.001f);
        assertEquals(20, vector.getY(), 0.001f);
        assertEquals(30, vector.getZ(), 0.001f);
        assertEquals(40, vector.getW(), 0.001f);
    }

    @Test
    void testAddSubtractMultiply() {
        Vector4D v1 = new Vector4D(1, 2, 3, 4);
        Vector4D v2 = new Vector4D(5, 6, 7, 8);

        Vector4D sum = v1.add(v2);
        assertEquals(6, sum.getX(), 0.001f);
        assertEquals(8, sum.getY(), 0.001f);
        assertEquals(10, sum.getZ(), 0.001f);
        assertEquals(12, sum.getW(), 0.001f);

        Vector4D diff = v1.subtract(v2);
        assertEquals(-4, diff.getX(), 0.001f);
        assertEquals(-4, diff.getY(), 0.001f);
        assertEquals(-4, diff.getZ(), 0.001f);
        assertEquals(-4, diff.getW(), 0.001f);

        Vector4D scaled = v1.multiply(2.0f);
        assertEquals(2, scaled.getX(), 0.001f);
        assertEquals(4, scaled.getY(), 0.001f);
        assertEquals(6, scaled.getZ(), 0.001f);
        assertEquals(8, scaled.getW(), 0.001f);
    }

    @Test
    void testDotAndScalarProduct() {
        Vector4D v1 = new Vector4D(1, 2, 3, 4);
        Vector4D v2 = new Vector4D(5, 6, 7, 8);

        float dot = v1.dot(v2);
        float scalarProduct = v1.scalarProduct(v2);

        assertEquals(70, dot, 0.001f); // 1*5 + 2*6 + 3*7 + 4*8 = 70
        assertEquals(70, scalarProduct, 0.001f);
        assertEquals(dot, scalarProduct, 0.001f);
    }

    @Test
    void testLength() {
        Vector4D v = new Vector4D(1, 2, 2, 1);
        float length = v.length();
        assertEquals(3.16227766f, length, 0.001f); // sqrt(1² + 2² + 2² + 1²) = sqrt(10)
    }

    @Test
    void testNormalize() {
        Vector4D v = new Vector4D(4, 0, 0, 0);
        Vector4D normalized = v.normalize();

        assertEquals(1, normalized.getX(), 0.001f);
        assertEquals(0, normalized.getY(), 0.001f);
        assertEquals(0, normalized.getZ(), 0.001f);
        assertEquals(0, normalized.getW(), 0.001f);
        assertEquals(1, normalized.length(), 0.001f);

        // Нулевой вектор
        Vector4D zero = new Vector4D(0, 0, 0, 0);
        Vector4D normalizedZero = zero.normalize();
        assertEquals(0, normalizedZero.length(), 0.001f);
    }

    @Test
    void testEqualsAndHashCode() {
        Vector4D v1 = new Vector4D(1, 2, 3, 4);
        Vector4D v2 = new Vector4D(1, 2, 3, 4);
        Vector4D v3 = new Vector4D(5, 6, 7, 8);

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
        assertNotEquals(v1, v3);
    }

}