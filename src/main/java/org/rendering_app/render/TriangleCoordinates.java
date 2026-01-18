package org.rendering_app.render;

import org.rendering_app.math.Point2D;

public class TriangleCoordinates {
    private int triangleId;
    private int vertexId;
    private Point2D vertex;

    public TriangleCoordinates() {
        this(-1, -1, new Point2D(0, 0));
    }

    public TriangleCoordinates(int triangleId, int vertexId, Point2D vertex) {
        this.triangleId = triangleId;
        this.vertexId = vertexId;
        this.vertex = vertex;
    }

    public int getTriangleId() {
        return triangleId;
    }

    public void setTriangleId(int triangleId) {
        this.triangleId = triangleId;
    }

    public int getVertexId() {
        return vertexId;
    }

    public void setVertexId(int vertexId) {
        this.vertexId = vertexId;
    }

    public Point2D getVertex() {
        return vertex;
    }

    public void setVertex(Point2D vertex) {
        this.vertex = vertex;
    }
}
