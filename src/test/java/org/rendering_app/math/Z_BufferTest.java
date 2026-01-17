package org.rendering_app.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Z_BufferTest {

    @Test
    void testConstructorAndSize() {
        Z_Buffer buffer = new Z_Buffer(800, 600);

        assertEquals(800, buffer.getWidth());
        assertEquals(600, buffer.getHeight());
        assertEquals(800 * 600, buffer.size());
    }

    @Test
    void testGetAndSet() {
        Z_Buffer buffer = new Z_Buffer(100, 100);

        buffer.set(10, 20, 0.5f);
        assertEquals(0.5f, buffer.get(10, 20), 0.001f);

        buffer.set(50, 60, -1.0f);
        assertEquals(-1.0f, buffer.get(50, 60), 0.001f);
    }

    @Test
    void testMultipleOperations() {
        Z_Buffer buffer = new Z_Buffer(200, 200);

        // Заполнение буфера значениями
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                buffer.set(i, j, (float) (i + j) / 400.0f);
            }
        }

        // Проверка некоторых значений
        assertEquals(0.0f, buffer.get(0, 0), 0.001f);          // (0+0)/400 = 0
        assertEquals(0.5f, buffer.get(100, 100), 0.001f);      // (100+100)/400 = 200/400 = 0.5
        assertEquals(0.995f, buffer.get(199, 199), 0.001f);    // (199+199)/400 = 398/400 = 0.995
    }

}