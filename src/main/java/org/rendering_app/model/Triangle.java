package org.rendering_app.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Triangle {
    private List<Integer> vertexIndices;
    private List<Integer> normalIndices;
    private List<Integer> textureVertexIndices;

    public Triangle() {
        this.vertexIndices = new ArrayList<>(3);
        this.normalIndices = new ArrayList<>(3);
        this.textureVertexIndices = new ArrayList<>(3);
    }

    public Triangle(Triangle other) {
        this();
        this.vertexIndices.addAll(other.vertexIndices);
        this.normalIndices.addAll(other.normalIndices);
        this.textureVertexIndices.addAll(other.textureVertexIndices);
    }

    public Triangle(int v1, int v2, int v3) {
        this();
        this.vertexIndices = new ArrayList<>(Arrays.asList(v1, v2, v3));
    }

    public Triangle(int v1, int v2, int v3, int n1, int n2, int n3, int t1, int t2, int t3) {
        this();
        this.vertexIndices = new ArrayList<>(Arrays.asList(v1, v2, v3));
        this.normalIndices = new ArrayList<>(Arrays.asList(n1, n2, n3));
        this.textureVertexIndices = new ArrayList<>(Arrays.asList(t1, t2, t3));
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public List<Integer> getNormalIndices() {
        return normalIndices;
    }

    public List<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public void setVertexIndices(List<Integer> indices) {
        if (indices == null || indices.size() != 3) {
            throw new IllegalArgumentException("Triangle must have exactly 3 vertices");
        }
        this.vertexIndices = new ArrayList<>(indices);
    }

    public void setNormalIndices(List<Integer> indices) {
        if (indices == null) {
            this.normalIndices = new ArrayList<>();
            return;
        }
        if (!indices.isEmpty() && indices.size() != 3) {
            throw new IllegalArgumentException("Triangle normals must have size 3 (or be empty)");
        }
        this.normalIndices = new ArrayList<>(indices);
    }

    public void setTextureVertexIndices(List<Integer> indices) {
        if (indices == null) {
            this.textureVertexIndices = new ArrayList<>();
            return;
        }
        if (!indices.isEmpty() && indices.size() != 3) {
            throw new IllegalArgumentException("Triangle texture indices must have size 3 (or be empty)");
        }
        this.textureVertexIndices = new ArrayList<>(indices);
    }

    public int getVertexIndex(int position) {
        if (position < 0 || position > 2) throw new IndexOutOfBoundsException("Triangle vertex position must be 0, 1, or 2");
        return vertexIndices.get(position);
    }

    public int getNormalIndex(int position) {
        if (position < 0 || position > 2) throw new IndexOutOfBoundsException("Triangle normal position must be 0, 1, or 2");
        return normalIndices.get(position);
    }

    public int getTextureIndex(int position) {
        if (position < 0 || position > 2) throw new IndexOutOfBoundsException("Triangle texture position must be 0, 1, or 2");
        return textureVertexIndices.get(position);
    }

    public boolean isValid() {
        return vertexIndices != null && vertexIndices.size() == 3;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Triangle other = (Triangle) obj;
        return Objects.equals(vertexIndices, other.vertexIndices)
                && Objects.equals(normalIndices, other.normalIndices)
                && Objects.equals(textureVertexIndices, other.textureVertexIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexIndices, normalIndices, textureVertexIndices);
    }

    @Override
    public String toString() {
        return String.format("Triangle[vertices=%s, normals=%s, textures=%s]",
                vertexIndices, normalIndices, textureVertexIndices);
    }
}
