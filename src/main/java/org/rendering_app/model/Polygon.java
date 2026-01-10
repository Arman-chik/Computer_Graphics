package org.rendering_app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Polygon {

    private List<Integer> vertexIndices;
    private List<Integer> normalIndices;
    private List<Integer> textureVertexIndices;

    public Polygon() {
        this.vertexIndices = new ArrayList<>();
        this.normalIndices = new ArrayList<>();
        this.textureVertexIndices = new ArrayList<>();
    }

    public Polygon(Polygon other) {
        this();
        this.vertexIndices.addAll(other.vertexIndices);
        this.normalIndices.addAll(other.normalIndices);
        this.textureVertexIndices.addAll(other.textureVertexIndices);
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
        if (indices == null || indices.size() < 3) {
            throw new IllegalArgumentException("Polygon must have at least 3 vertices");
        }
        this.vertexIndices = new ArrayList<>(indices);
    }

    public void setNormalIndices(List<Integer> indices) {
        if (indices == null) {
            this.normalIndices = new ArrayList<>();
            return;
        }
        if (!indices.isEmpty() && indices.size() != vertexIndices.size()) {
            throw new IllegalArgumentException("Normal indices count must match vertex indices count (or be empty)");
        }
        this.normalIndices = new ArrayList<>(indices);
    }

    public void setTextureVertexIndices(List<Integer> indices) {
        if (indices == null) {
            this.textureVertexIndices = new ArrayList<>();
            return;
        }
        if (!indices.isEmpty() && indices.size() != vertexIndices.size()) {
            throw new IllegalArgumentException("Texture vertex indices count must match vertex indices count (or be empty)");
        }
        this.textureVertexIndices = new ArrayList<>(indices);
    }

    public boolean isValid() {
        return vertexIndices != null && vertexIndices.size() >= 3;
    }

    public int getVertexCount() {
        return vertexIndices.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Polygon other = (Polygon) obj;
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
        return String.format("Polygon[vertices=%s, normals=%s, textures=%s]",
                vertexIndices, normalIndices, textureVertexIndices);
    }
}
