package org.rendering_app.render;

import org.rendering_app.math.*;
import org.rendering_app.model.Material;
import org.rendering_app.model.Model;
import org.rendering_app.model.Triangle;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class RenderEngine {

    private static final int VERTEXSCANRADIUS = 100;

    private final Camera camera;
    private final Model model;
    private final int width;
    private final int height;
    private final Z_Buffer zBuffer;
    private final PixelBuffer pixelBuffer;
    private final Material material;

    private int currentTriangle;

    private int nearestVertex = -1;
    private Point2D nearestVertexPoint;
    private int nearestTriangle = -1;

    private int mouseX = 0;
    private int mouseY = 0;

    private float nearestDepth = Float.POSITIVE_INFINITY;

    public RenderEngine(Camera camera,
                        Model model,
                        int width,
                        int height,
                        Z_Buffer zBuffer,
                        PixelBuffer pixelBuffer,
                        Material material) {
        this.camera = camera;
        this.model = model;
        this.width = width;
        this.height = height;
        this.zBuffer = zBuffer;
        this.pixelBuffer = pixelBuffer;
        this.material = material;
    }

    public void render() {
        Matrix4D modelMatrix = Matrix4D.createIdentityMatrix();
        Matrix4D viewMatrix = camera.getViewMatrix();
        Matrix4D projectionMatrix = camera.getProjectionMatrix();

        Matrix4D mvpMatrix = projectionMatrix.multiply(viewMatrix).multiply(modelMatrix);

        renderTriangles(mvpMatrix, model.getTriangles().size());
    }

    public TriangleCoordinates renderWithSelection(int x, int y) {
        mouseX = x;
        mouseY = y;
        render();
        return new TriangleCoordinates(nearestTriangle, nearestVertex, nearestVertexPoint);
    }

    private void renderTriangles(Matrix4D mvpMatrix, int triangleCount) {
        nearestVertex = -1;
        nearestTriangle = -1;
        nearestVertexPoint = null;
        nearestDepth = Float.POSITIVE_INFINITY;

        Point3D[] worldPoints = new Point3D[3];
        Point3D[] screenPoints = new Point3D[3];
        Point3D[] normalVectors = new Point3D[3];
        Vector2D[] textureVectors = new Vector2D[3];

        for (int triangleIndex = 0; triangleIndex < triangleCount; triangleIndex++) {
            Arrays.fill(worldPoints, null);
            Arrays.fill(screenPoints, null);
            Arrays.fill(normalVectors, null);
            Arrays.fill(textureVectors, null);

            getTriangleVectors(screenPoints, normalVectors, textureVectors, mvpMatrix, triangleIndex, worldPoints);

            currentTriangle = triangleIndex;
            renderTriangle(screenPoints, normalVectors, textureVectors, worldPoints);
        }

        if (nearestVertex == -1 && nearestTriangle != -1) {
            material.selectHighlightColor();

            boolean oldShowIllumination = material.isShowIllumination();
            boolean oldShowTexture = material.isShowTexture();

            material.setShowIllumination(false);
            material.setShowTexture(false);

            Arrays.fill(worldPoints, null);
            Arrays.fill(screenPoints, null);
            Arrays.fill(normalVectors, null);
            Arrays.fill(textureVectors, null);

            getTriangleVectors(screenPoints, normalVectors, textureVectors, mvpMatrix, nearestTriangle, worldPoints);
            renderTriangleForDisplay(screenPoints, normalVectors, textureVectors, worldPoints);

            material.setShowIllumination(oldShowIllumination);
            material.setShowTexture(oldShowTexture);
            material.selectBaseColor();
        }
    }

    private void getTriangleVectors(Point3D[] screenPoints,
                                    Point3D[] normalVectors,
                                    Vector2D[] textureVectors,
                                    Matrix4D mvpMatrix,
                                    int triangleIndex,
                                    Point3D[] worldPoints) {

        Triangle triangle = model.getTriangles().get(triangleIndex);

        List<Integer> nIdx = triangle.getNormalIndices();
        List<Integer> tIdx = triangle.getTextureVertexIndices();

        for (int i = 0; i < 3; i++) {
            int vertexIndex = triangle.getVertexIndex(i);
            Vector3D vertex = model.getVertices().get(vertexIndex);

            worldPoints[i] = new Point3D(vertex.getX(), vertex.getY(), vertex.getZ());

            Vector3D transformed = Matrix4D.multiplyMatrix4DByVector3D(mvpMatrix, vertex);
            screenPoints[i] = MathCast.toPoint3D(transformed, width, height);

            if (material.isShowIllumination() && nIdx != null && nIdx.size() == 3) {
                int normalIndex = triangle.getNormalIndex(i);
                normalVectors[i] = new Point3D(model.getNormals().get(normalIndex));
            }

            if (material.isShowTexture() && tIdx != null && tIdx.size() == 3) {
                int textureIndex = triangle.getTextureIndex(i);
                textureVectors[i] = model.getTextureVertices().get(textureIndex);
            }
        }
    }

    private void renderTriangle(Point3D[] screenPoints,
                                Point3D[] normalVectors,
                                Vector2D[] textureVectors,
                                Point3D[] worldPoints) {

        Point3D A = screenPoints[0];
        Point3D B = screenPoints[1];
        Point3D C = screenPoints[2];

        float area2 = (float) ((B.getX() - A.getX()) * (C.getY() - A.getY()) - (B.getY() - A.getY()) * (C.getX() - A.getX()));
        if (Math.abs(area2) < 1e-6f) return;

        if (area2 < 0) return;

        checkPointInTriangle(new Point3D(mouseX, mouseY, 0), A, B, C);
        renderTriangleForDisplay(screenPoints, normalVectors, textureVectors, worldPoints);
    }

    private void renderTriangleForDisplay(Point3D[] screenPoints,
                                          Point3D[] normalVectors,
                                          Vector2D[] textureVectors,
                                          Point3D[] worldPoints) {

        Point3D A = screenPoints[0];
        Point3D B = screenPoints[1];
        Point3D C = screenPoints[2];

        int xMin = (int) Math.max(0, Math.min(Math.min(A.getX(), B.getX()), C.getX()));
        int xMax = (int) Math.min(width - 1, Math.max(Math.max(A.getX(), B.getX()), C.getX()));
        int yMin = (int) Math.max(0, Math.min(Math.min(A.getY(), B.getY()), C.getY()));
        int yMax = (int) Math.min(height - 1, Math.max(Math.max(A.getY(), B.getY()), C.getY()));

        for (int y = yMin; y <= yMax; y++) {
            for (int x = xMin; x <= xMax; x++) {
                Point3D P = new Point3D(x, y, 0);

                float[] barycentric = Rasterization.calculateBarycentricCoefficients(A, B, C, P);
                float weightA = barycentric[0];
                float weightB = barycentric[1];
                float weightC = barycentric[2];
                float z = barycentric[3];

                if (weightA < 0 || weightB < 0 || weightC < 0) continue;

                if (zBuffer.get(x, y) < z) continue;

                float worldX = worldPoints[0].getX() * weightA + worldPoints[1].getX() * weightB + worldPoints[2].getX() * weightC;
                float worldY = worldPoints[0].getY() * weightA + worldPoints[1].getY() * weightB + worldPoints[2].getY() * weightC;
                float worldZ = worldPoints[0].getZ() * weightA + worldPoints[1].getZ() * weightB + worldPoints[2].getZ() * weightC;

                Point3D worldPoint = new Point3D(worldX, worldY, worldZ);

                Color color = material.applyMaterial(weightA, weightB, weightC, textureVectors, normalVectors, worldPoint);
                pixelBuffer.add(new Point2D(x, y), color);
                zBuffer.set(x, y, z);
            }
        }
    }

    private void checkPointInTriangle(Point3D P, Point3D A, Point3D B, Point3D C) {
        float[] barycentric = Rasterization.calculateBarycentricCoefficients(A, B, C, P);
        float weightA = barycentric[0];
        float weightB = barycentric[1];
        float weightC = barycentric[2];
        float z = barycentric[3];

        if (weightA >= 0 && weightB >= 0 && weightC >= 0) {
            if (nearestTriangle == -1 || z <= nearestDepth) {
                nearestDepth = z;
                nearestTriangle = currentTriangle;

                Point3D[] vertices = new Point3D[]{A, B, C};
                for (int i = 0; i < 3; i++) {
                    float dx = (float) (vertices[i].getX() - mouseX);
                    float dy = (float) (vertices[i].getY() - mouseY);
                    float distance = dx * dx + dy * dy;

                    if (distance <= VERTEXSCANRADIUS) {
                        nearestVertex = i;
                        nearestVertexPoint = new Point2D(vertices[i].getX(), vertices[i].getY());
                        break;
                    }
                }
            }
        }
    }
}
