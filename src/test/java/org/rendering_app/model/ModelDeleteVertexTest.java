package org.rendering_app.model;

import org.junit.jupiter.api.Test;
import org.rendering_app.math.Vector3D;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelDeleteVertexTest {

    @Test
    void deleteVertex_removesVertexAndUpdatesReferences() {
        Model m = new Model();

        // 4 вершины: индексы 0,1,2,3
        m.addVertex(new Vector3D(0, 0, 0));
        m.addVertex(new Vector3D(1, 0, 0));
        m.addVertex(new Vector3D(0, 1, 0));
        m.addVertex(new Vector3D(0, 0, 1));

        // Полигон использует вершину 1 => её необходимо удалить
        Polygon p1 = new Polygon();
        p1.setVertexIndices(List.of(0, 1, 2));
        m.addPolygon(p1);

        // Полигон не использует индекс 1, но имеет индекс 3 (>1) => необходимо сдвинуться к индексу 2
        Polygon p2 = new Polygon();
        p2.setVertexIndices(List.of(0, 2, 3));
        m.addPolygon(p2);

        // triangles аналогично
        Triangle t1 = new Triangle(0, 1, 2);
        m.addTriangle(t1);

        Triangle t2 = new Triangle(0, 2, 3);
        m.addTriangle(t2);

        m.deleteVertex(1);

        assertEquals(3, m.getVertices().size(), "Vertex list must shrink by 1");
        assertEquals(1, m.getPolygons().size(), "Polygon using deleted vertex must be removed");
        assertEquals(List.of(0, 1, 2), m.getPolygons().get(0).getVertexIndices(),
                "Indices > deleted must shift down");

        assertEquals(1, m.getTriangles().size(), "Triangle using deleted vertex must be removed");
        assertEquals(List.of(0, 1, 2), m.getTriangles().get(0).getVertexIndices(),
                "Triangle indices > deleted must shift down");
    }

    @Test
    void deleteVertex_invalidIndex_throws() {
        Model m = new Model();
        m.addVertex(new Vector3D(0, 0, 0));

        assertThrows(IndexOutOfBoundsException.class, () -> m.deleteVertex(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.deleteVertex(1));
    }
}
