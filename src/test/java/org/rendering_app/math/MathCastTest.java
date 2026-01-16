package org.rendering_app.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MathCastTest {

    @Test
    void testToPoint2D() {
        Vector3D vertex = new Vector3D(0.5f, 0.5f, 0);
        Point2D point = MathCast.toPoint2D(vertex, 800, 600);

        assertEquals(0.5f * 800 + 400, point.getX(), 0.001f);
        assertEquals(-0.5f * 600 + 300, point.getY(), 0.001f);
    }

    @Test
    void testToPoint3D() {
        Vector3D vertex = new Vector3D(0.5f, 0.5f, 0.7f);
        Point3D point = MathCast.toPoint3D(vertex, 800, 600);

        assertEquals(0.5f * 800 + 400, point.getX(), 0.001f);
        assertEquals(-0.5f * 600 + 300, point.getY(), 0.001f);
        assertEquals(0.7f, point.getZ(), 0.001f);
    }

    @Test
    void testToVector3DFromVector4D() {
        Vector4D v4 = new Vector4D(2, 4, 6, 2);
        Vector3D v3 = MathCast.toVector3D(v4);

        assertEquals(1, v3.getX(), 0.001f);
        assertEquals(2, v3.getY(), 0.001f);
        assertEquals(3, v3.getZ(), 0.001f);
    }

    @Test
    void testToVector3DFromVector4DWithZeroW() {
        Vector4D v4 = new Vector4D(2, 4, 6, 0);
        Vector3D v3 = MathCast.toVector3D(v4);

        assertEquals(2, v3.getX(), 0.001f);
        assertEquals(4, v3.getY(), 0.001f);
        assertEquals(6, v3.getZ(), 0.001f);
    }

    @Test
    void testToVector3DFromPoint3D() {
        Point3D point = new Point3D(1, 2, 3);
        Vector3D vector = MathCast.toVector3D(point);

        assertEquals(1, vector.getX(), 0.001f);
        assertEquals(2, vector.getY(), 0.001f);
        assertEquals(3, vector.getZ(), 0.001f);
    }

    @Test
    void testToVector4DFromVector3D() {
        Vector3D v3 = new Vector3D(1, 2, 3);
        Vector4D v4 = MathCast.toVector4D(v3);

        assertEquals(1, v4.getX(), 0.001f);
        assertEquals(2, v4.getY(), 0.001f);
        assertEquals(3, v4.getZ(), 0.001f);
        assertEquals(1, v4.getW(), 0.001f);
    }
}