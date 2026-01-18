package org.rendering_app.objutils;

import org.junit.jupiter.api.Test;
import org.rendering_app.model.Model;
import org.rendering_app.obj_utils.OBJReader;
import org.rendering_app.obj_utils.OBJWriter;

import static org.junit.jupiter.api.Assertions.*;

class OBJTest {

    // Проверяет, что OBJReader корректно парсит минимальный OBJ
    @Test
    void reader_parsesMinimalTriangleAndTriangulates() {
        String obj =
                "v 0 0 0\n" +
                        "v 1 0 0\n" +
                        "v 0 1 0\n" +
                        "f 1 2 3\n";

        Model m = OBJReader.read(obj);

        assertEquals(3, m.getVertices().size());
        assertEquals(1, m.getPolygons().size());
        assertEquals(1, m.getTriangles().size(), "Single face with 3 vertices -> 1 triangle");
    }

    // Проверяет поддержку отрицательных индексов в OBJ (ссылки "от конца массива")
    @Test
    void reader_supportsNegativeIndices() {
        String obj =
                "v 0 0 0\n" +
                        "v 1 0 0\n" +
                        "v 0 1 0\n" +
                        "f -3 -2 -1\n";

        Model m = OBJReader.read(obj);
        assertEquals(1, m.getPolygons().size());
        assertEquals(1, m.getTriangles().size());
    }

    // Интеграционный round-trip: записали модель в OBJ через OBJWriter, прочитали обратно через OBJReader
    @Test
    void writer_then_reader_roundTripKeepsCounts() {
        String obj =
                "v 0 0 0\n" +
                        "v 1 0 0\n" +
                        "v 1 1 0\n" +
                        "v 0 1 0\n" +
                        "f 1 2 3 4\n";

        Model m1 = OBJReader.read(obj);
        String out = OBJWriter.modelToString(m1);
        Model m2 = OBJReader.read(out);

        assertEquals(m1.getVertices().size(), m2.getVertices().size());
        assertEquals(m1.getPolygons().size(), m2.getPolygons().size());
        assertEquals(m1.getTriangles().size(), m2.getTriangles().size());
    }
}
