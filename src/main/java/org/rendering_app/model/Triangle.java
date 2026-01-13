package org.rendering_app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Triangle {
    private List<Integer> vertexIndices;
    private List<Integer> normalIndices;
    private List<Integer> textureIndices;

    public Triangle() {
        vertexIndices = new ArrayList<>();
        normalIndices = new ArrayList<>();
        textureIndices = new ArrayList<>();
    }


    public Triangle(Triangle other) {
        this();
        vertexIndices.addAll(other.vertexIndices);
        normalIndices.addAll(other.normalIndices);
        textureIndices.addAll(other.textureIndices);
    }


    public Triangle(int v1, int v2, int v3) {
        this();
        vertexIndices.add(v1);
        vertexIndices.add(v2);
        vertexIndices.add(v3);
    }


    public Triangle(int v1, int v2, int v3,
                    int n1, int n2, int n3,
                    int t1, int t2, int t3) {
        this();
        vertexIndices.add(v1);
        vertexIndices.add(v2);
        vertexIndices.add(v3);

        normalIndices.add(n1);
        normalIndices.add(n2);
        normalIndices.add(n3);

        textureIndices.add(t1);
        textureIndices.add(t2);
        textureIndices.add(t3);
    }

    public void setVertexIndices(List<Integer> indices) {
        if (indices.size() == 3) {
            vertexIndices = new ArrayList<>(indices);
        } else {
            throw new IllegalArgumentException("Triangle must have exactly 3 vertices");
        }
    }

    public void setNormalIndices(List<Integer> indices) {
        if (indices.size() == 3) {
            normalIndices = new ArrayList<>(indices);
        } else {
            throw new IllegalArgumentException("Triangle must have exactly 3 normals");
        }
    }

    public void setTextureIndices(List<Integer> indices) {
        if (indices.size() == 3) {
            textureIndices = new ArrayList<>(indices);
        } else {
            throw new IllegalArgumentException("Triangle must have exactly 3 texture coordinates");
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



    public int getVertexIndex(int position) {
        if (position >= 0 && position < 3) {
            return vertexIndices.get(position);
        }
        throw new IndexOutOfBoundsException("Triangle vertex position must be 0, 1, or 2");
    }

    public int getNormalIndex(int position) {
        if (position >= 0 && position < 3) {
            return normalIndices.get(position);
        }
        throw new IndexOutOfBoundsException("Triangle normal position must be 0, 1, or 2");
    }

    public int getTextureIndex(int position) {
        if (position >= 0 && position < 3) {
            return textureIndices.get(position);
        }
        throw new IndexOutOfBoundsException("Triangle texture position must be 0, 1, or 2");
    }



    public boolean isValid() {
        return vertexIndices.size() == 3;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Triangle other = (Triangle) obj;
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
        return String.format("Triangle[vertices=%s, normals=%s, textures=%s]",
                vertexIndices, normalIndices, textureIndices);
    }
}
