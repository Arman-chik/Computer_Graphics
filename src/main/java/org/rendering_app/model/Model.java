package org.rendering_app.model;

import org.rendering_app.math.Vector2D;
import org.rendering_app.math.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Vector3D> vertices;
    private List<Vector2D> textureVertices;
    private List<Vector3D> normals;
    private List<Polygon> polygons;
    private List<Triangle> triangles;

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


    public void deleteVertex(int vertexIndex) {
        if (vertexIndex < 0 || vertexIndex >= vertices.size()) {
            throw new IndexOutOfBoundsException("Invalid vertex index");
        }


        vertices.remove(vertexIndex);

        for (int i = 0; i < polygons.size(); ) {
            boolean polygonRemoved = false;
            Polygon polygon = polygons.get(i);
            List<Integer> vertexIndices = polygon.getVertexIndices();

            for (int j = 0; j < vertexIndices.size(); j++) {
                int currentVertex = vertexIndices.get(j);


                if (currentVertex == vertexIndex) {
                    polygons.remove(i);
                    polygonRemoved = true;
                    break;
                }


                if (currentVertex > vertexIndex) {
                    vertexIndices.set(j, currentVertex - 1);
                }
            }


            if (!polygonRemoved) {
                i++;
            }
        }


        for (Triangle triangle : triangles) {
            List<Integer> vertexIndices = triangle.getVertexIndices();
            for (int j = 0; j < vertexIndices.size(); j++) {
                int currentVertex = vertexIndices.get(j);
                if (currentVertex > vertexIndex) {
                    vertexIndices.set(j, currentVertex - 1);
                }
            }
        }


        for (Polygon polygon : polygons) {
            updateIndicesAfterDeletion(polygon.getNormalIndices(), vertexIndex);
            updateIndicesAfterDeletion(polygon.getTextureIndices(), vertexIndex);
        }

        for (Triangle triangle : triangles) {
            updateIndicesAfterDeletion(triangle.getNormalIndices(), vertexIndex);
            updateIndicesAfterDeletion(triangle.getTextureIndices(), vertexIndex);
        }
    }

    private void updateIndicesAfterDeletion(List<Integer> indices, int deletedIndex) {
        for (int i = 0; i < indices.size(); i++) {
            int currentIndex = indices.get(i);
            if (currentIndex > deletedIndex) {
                indices.set(i, currentIndex - 1);
            }
        }
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