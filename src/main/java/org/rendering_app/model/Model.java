package org.rendering_app.model;

import org.rendering_app.math.Vector2D;
import org.rendering_app.math.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final List<Vector3D> vertices;
    private final List<Vector2D> textureVertices;
    private final List<Vector3D> normals;
    private final List<Polygon> polygons;
    private final List<Triangle> triangles;

    public Model() {
        vertices = new ArrayList<>();
        textureVertices = new ArrayList<>();
        normals = new ArrayList<>();
        polygons = new ArrayList<>();
        triangles = new ArrayList<>();
    }

    public Model(Model other) {
        this();

        for (Vector3D v : other.vertices) {
            vertices.add(new Vector3D(v.getX(), v.getY(), v.getZ()));
        }
        for (Vector2D tv : other.textureVertices) {
            textureVertices.add(new Vector2D(tv.getX(), tv.getY()));
        }
        for (Vector3D n : other.normals) {
            normals.add(new Vector3D(n.getX(), n.getY(), n.getZ()));
        }
        for (Polygon p : other.polygons) {
            polygons.add(new Polygon(p));
        }
        for (Triangle t : other.triangles) {
            triangles.add(new Triangle(t));
        }
    }

    public List<Vector3D> getVertices() {
        return vertices;
    }

    public List<Vector2D> getTextureVertices() {
        return textureVertices;
    }

    public List<Vector3D> getNormals() {
        return normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void addVertex(Vector3D vertex) {
        vertices.add(vertex);
    }

    public void addTextureVertex(Vector2D texVertex) {
        textureVertices.add(texVertex);
    }

    public void addNormal(Vector3D normal) {
        normals.add(normal);
    }

    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
    }

    public void addTriangle(Triangle triangle) {
        triangles.add(triangle);
    }

    public void deleteVertex(int vertexIndex) {
        if (vertexIndex < 0 || vertexIndex >= vertices.size()) {
            throw new IndexOutOfBoundsException("Invalid vertex index");
        }
        vertices.remove(vertexIndex);

        for (int i = 0; i < polygons.size(); ) {
            Polygon p = polygons.get(i);
            if (containsIndex(p.getVertexIndices(), vertexIndex)) {
                polygons.remove(i);
                continue;
            }
            shiftIndices(p.getVertexIndices(), vertexIndex);
            i++;
        }

        for (int i = 0; i < triangles.size(); ) {
            Triangle t = triangles.get(i);
            if (containsIndex(t.getVertexIndices(), vertexIndex)) {
                triangles.remove(i);
                continue;
            }
            shiftIndices(t.getVertexIndices(), vertexIndex);
            i++;
        }
    }

    private static boolean containsIndex(List<Integer> indices, int target) {
        for (int idx : indices) {
            if (idx == target) return true;
        }
        return false;
    }

    private static void shiftIndices(List<Integer> indices, int deletedIndex) {
        for (int i = 0; i < indices.size(); i++) {
            int cur = indices.get(i);
            if (cur > deletedIndex) {
                indices.set(i, cur - 1);
            }
        }
    }

    public boolean isEmpty() {
        return vertices.isEmpty() && polygons.isEmpty() && triangles.isEmpty();
    }

    public void clear() {
        vertices.clear();
        textureVertices.clear();
        normals.clear();
        polygons.clear();
        triangles.clear();
    }

    @Override
    public String toString() {
        return String.format("Model: %d vertices, %d polygons, %d triangles",
                vertices.size(), polygons.size(), triangles.size());
    }
}
