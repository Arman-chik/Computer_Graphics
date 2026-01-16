package org.rendering_app.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Point2DTest {

    @Test
    void testConstructors() {
        Point2D p1 = new Point2D();
        assertEquals(0, p1.getX(), 0.001f);
        assertEquals(0, p1.getY(), 0.001f);

        Point2D p2 = new Point2D(5.5f, 10.2f);
        assertEquals(5.5f, p2.getX(), 0.001f);
        assertEquals(10.2f, p2.getY(), 0.001f);

        Vector2D vector = new Vector2D(3, 4);
        Point2D p3 = new Point2D(vector);
        assertEquals(3, p3.getX(), 0.001f);
        assertEquals(4, p3.getY(), 0.001f);
    }

    @Test
    void testGettersAndSetters() {
        Point2D point = new Point2D();

        point.setX(7.7f);
        point.setY(8.8f);

        assertEquals(7.7f, point.getX(), 0.001f);
        assertEquals(8.8f, point.getY(), 0.001f);

        point.set(9.9f, 10.10f);
        assertEquals(9.9f, point.getX(), 0.001f);
        assertEquals(10.10f, point.getY(), 0.001f);
    }

    @Test
    void testHashCode() {
        Point2D p1 = new Point2D(1, 2);
        Point2D p2 = new Point2D(1, 2);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

}