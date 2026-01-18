package org.rendering_app.model;

import org.junit.jupiter.api.Test;
import org.rendering_app.math.Vector3D;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelPolygonRemoverTest {

    @Test
    void parseRange_singleNumber() {
        assertArrayEquals(new int[]{5, 5}, ModelPolygonRemover.parseRange("5"));
    }

    @Test
    void parseRange_rangeAndSwap() {
        assertArrayEquals(new int[]{5, 12}, ModelPolygonRemover.parseRange("5-12"));
        assertArrayEquals(new int[]{5, 12}, ModelPolygonRemover.parseRange("12-5"));
        assertArrayEquals(new int[]{5, 12}, ModelPolygonRemover.parseRange(" 12 - 5 "));
    }

    @Test
    void parseRange_badFormat_throws() {
        assertThrows(IllegalArgumentException.class, () -> ModelPolygonRemover.parseRange(""));
        assertThrows(IllegalArgumentException.class, () -> ModelPolygonRemover.parseRange("5-"));
        assertThrows(IllegalArgumentException.class, () -> ModelPolygonRemover.parseRange("-2"));
        assertThrows(NumberFormatException.class, () -> ModelPolygonRemover.parseRange("a"));
    }

    @Test
    void deletePolygonsRange_rebuildsTriangles() {
        Model m = new Model();
        // 4 вершины под quad
        m.addVertex(new Vector3D(0, 0, 0));
        m.addVertex(new Vector3D(1, 0, 0));
        m.addVertex(new Vector3D(1, 1, 0));
        m.addVertex(new Vector3D(0, 1, 0));

        Polygon quad = new Polygon();
        quad.setVertexIndices(List.of(0, 1, 2, 3));
        m.addPolygon(quad);

        // вручную "сломаем" triangles, чтобы проверить rebuild
        m.getTriangles().add(new Triangle(0, 1, 2));

        ModelPolygonRemover.rebuildTrianglesFromPolygons(m);
        assertEquals(2, m.getTriangles().size(), "Quad must triangulate into 2 triangles");

        int removed = ModelPolygonRemover.deletePolygonsRange(m, 0, 0);
        assertEquals(1, removed);
        assertEquals(0, m.getPolygons().size());
        assertEquals(0, m.getTriangles().size(), "After deleting all polys, triangles must be empty");
    }
}
