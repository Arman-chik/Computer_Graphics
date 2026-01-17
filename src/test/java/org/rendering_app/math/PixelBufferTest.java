package org.rendering_app.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;

public class PixelBufferTest {

    @Test
    void testAddAndGetColor() {
        PixelBuffer buffer = new PixelBuffer();
        Point2D point = new Point2D(10, 20);
        Color color = Color.RED;

        buffer.add(point, color);

        assertEquals(color, buffer.getColor(point));
        assertEquals(Color.BLACK, buffer.getColor(new Point2D(100, 200)));
    }

    @Test
    void testGetData() {
        PixelBuffer buffer = new PixelBuffer();
        Point2D point1 = new Point2D(10, 20);
        Point2D point2 = new Point2D(30, 40);

        buffer.add(point1, Color.BLUE);
        buffer.add(point2, Color.GREEN);

        var data = buffer.getData();
        assertEquals(2, data.size());
        assertEquals(Color.BLUE, data.get(point1));
        assertEquals(Color.GREEN, data.get(point2));
    }

    @Test
    void testForEach() {
        PixelBuffer buffer = new PixelBuffer();
        Point2D point1 = new Point2D(10, 20);
        Point2D point2 = new Point2D(30, 40);

        buffer.add(point1, Color.RED);
        buffer.add(point2, Color.BLUE);

        int[] count = {0};
        buffer.forEach((p, c) -> count[0]++);

        assertEquals(2, count[0]);
    }

    @Test
    void testClear() {
        PixelBuffer buffer = new PixelBuffer();
        buffer.add(new Point2D(10, 20), Color.RED);

        assertEquals(1, buffer.getData().size());

        buffer.clear();
        assertEquals(0, buffer.getData().size());
    }
}