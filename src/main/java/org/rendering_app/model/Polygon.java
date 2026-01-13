package org.rendering_app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Polygon {
    private List<Integer> vertexIndices;
    private List<Integer> normalIndices;
    private List<Integer> textureIndices;

    public Polygon() {
        vertexIndices = new ArrayList<>();
        normalIndices = new ArrayList<>();
        textureIndices = new ArrayList<>();
    }


    public Polygon(Polygon other) {
        this();
        vertexIndices.addAll(other.vertexIndices);
        normalIndices.addAll(other.normalIndices);
        textureIndices.addAll(other.textureIndices);
    }

    public void setVertexIndices(List<Integer> indices) {
        if (indices.size() >= 3) {
            vertexIndices = new ArrayList<>(indices);
        } else {
            throw new IllegalArgumentException("Polygon must have at least 3 vertices");
        }
    }

    public void setNormalIndices(List<Integer> indices) {
        if (indices.size() >= 3) {
            normalIndices = new ArrayList<>(indices);
        } else {
            throw new IllegalArgumentException("Polygon must have at least 3 normals");
        }
    }

    public void setTextureIndices(List<Integer> indices) {
        if (indices.size() >= 3) {
            textureIndices = new ArrayList<>(indices);
        } else {
            throw new IllegalArgumentException("Polygon must have at least 3 texture coordinates");
        }
    }


    public List<Integer> getVertexIndices() {
        return new ArrayList<>(vertexIndices);
    }

    public List<Integer> getNormalIndices() {
        return new ArrayList<>(normalIndices);
    }

    public List<Integer> getTextureIndices() {
        return new ArrayList<>(textureIndices);
    }


    public void addVertexIndex(int index) {
        vertexIndices.add(index);
    }

    public void addNormalIndex(int index) {
        normalIndices.add(index);
    }

    public void addTextureIndex(int index) {
        textureIndices.add(index);
    }


    public boolean isValid() {
        return vertexIndices.size() >= 3;
    }


    public int getVertexCount() {
        return vertexIndices.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Polygon other = (Polygon) obj;
        return Objects.equals(vertexIndices, other.vertexIndices) &&
                Objects.equals(normalIndices, other.normalIndices) &&
                Objects.equals(textureIndices, other.textureIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexIndices, normalIndices, textureIndices);
    }

    @Override
    public String toString() {
        return String.format("Polygon[vertices=%s, normals=%s, textures=%s]",
                vertexIndices, normalIndices, textureIndices);
    }
}
