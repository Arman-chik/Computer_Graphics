package org.rendering_app.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2DTest {

    @Test
    void testConstructors() {
        Vector2D v1 = new Vector2D();
        assertEquals(0, v1.getX(), 0.001f);
        assertEquals(0, v1.getY(), 0.001f);

        Vector2D v2 = new Vector2D(3.5f, 4.5f);
        assertEquals(3.5f, v2.getX(), 0.001f);
        assertEquals(4.5f, v2.getY(), 0.001f);
    }

    @Test
    void testGettersAndSetters() {
        Vector2D vector = new Vector2D();
        vector.setX(10);
        vector.setY(20);

        assertEquals(10, vector.getX(), 0.001f);
        assertEquals(20, vector.getY(), 0.001f);
    }

    @Test
    void testAddSubtractMultiply() {
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(3, 4);

        Vector2D sum = v1.add(v2);
        assertEquals(4, sum.getX(), 0.001f);
        assertEquals(6, sum.getY(), 0.001f);

        Vector2D diff = v1.subtract(v2);
        assertEquals(-2, diff.getX(), 0.001f);
        assertEquals(-2, diff.getY(), 0.001f);

        Vector2D scaled = v1.multiply(2.5f);
        assertEquals(2.5f, scaled.getX(), 0.001f);
        assertEquals(5, scaled.getY(), 0.001f);
    }

    @Test
    void testScalarProduct() {
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(3, 4);

        float product = v1.scalarProduct(v2);
        assertEquals(11, product, 0.001f); // 1*3 + 2*4 = 11
    }

    @Test
    void testLength() {
        Vector2D v = new Vector2D(3, 4);
        float length = v.length();
        assertEquals(5, length, 0.001f); // sqrt(3² + 4²) = 5
    }

    @Test
    void testNormalize() {
        Vector2D v = new Vector2D(3, 4);
        Vector2D normalized = v.normalize();

        float length = normalized.length();
        assertEquals(1, length, 0.001f);

        // Проверка нормализации нулевого вектора
        Vector2D zero = new Vector2D(0, 0);
        Vector2D normalizedZero = zero.normalize();
        assertEquals(0, normalizedZero.getX(), 0.001f);
        assertEquals(0, normalizedZero.getY(), 0.001f);
    }

    @Test
    void testEqualsAndHashCode() {
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(1, 2);
        Vector2D v3 = new Vector2D(3, 4);

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
        assertNotEquals(v1, v3);
    }
}