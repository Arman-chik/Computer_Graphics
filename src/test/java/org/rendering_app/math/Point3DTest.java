package org.rendering_app.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Point3DTest {

    @Test
    void testConstructors() {
        Point3D p1 = new Point3D();
        assertEquals(0, p1.getX(), 0.001f);
        assertEquals(0, p1.getY(), 0.001f);
        assertEquals(0, p1.getZ(), 0.001f);

        Point3D p2 = new Point3D(1, 2, 3);
        assertEquals(1, p2.getX(), 0.001f);
        assertEquals(2, p2.getY(), 0.001f);
        assertEquals(3, p2.getZ(), 0.001f);

        Vector3D vector = new Vector3D(4, 5, 6);
        Point3D p3 = new Point3D(vector);
        assertEquals(4, p3.getX(), 0.001f);
        assertEquals(5, p3.getY(), 0.001f);
        assertEquals(6, p3.getZ(), 0.001f);
    }

    @Test
    void testGettersAndSetters() {
        Point3D point = new Point3D();

        point.setX(10);
        point.setY(20);
        point.setZ(30);

        assertEquals(10, point.getX(), 0.001f);
        assertEquals(20, point.getY(), 0.001f);
        assertEquals(30, point.getZ(), 0.001f);

        point.set(40, 50, 60);
        assertEquals(40, point.getX(), 0.001f);
        assertEquals(50, point.getY(), 0.001f);
        assertEquals(60, point.getZ(), 0.001f);
    }

    @Test
    void testDotProduct() {
        Point3D p1 = new Point3D(1, 2, 3);
        Point3D p2 = new Point3D(4, 5, 6);

        float result = p1.dot(p2);
        assertEquals(32, result, 0.001f); // 1*4 + 2*5 + 3*6 = 32
    }

    @Test
    void testAddAndSubtract() {
        Point3D p1 = new Point3D(1, 2, 3);
        Point3D p2 = new Point3D(4, 5, 6);

        Point3D sum = p1.add(p2);
        assertEquals(5, sum.getX(), 0.001f);
        assertEquals(7, sum.getY(), 0.001f);
        assertEquals(9, sum.getZ(), 0.001f);

        Point3D diff = p1.subtract(p2);
        assertEquals(-3, diff.getX(), 0.001f);
        assertEquals(-3, diff.getY(), 0.001f);
        assertEquals(-3, diff.getZ(), 0.001f);
    }


}