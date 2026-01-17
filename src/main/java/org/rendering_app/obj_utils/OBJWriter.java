package org.rendering_app.obj_utils;

import org.rendering_app.math.Vector2D;
import org.rendering_app.math.Vector3D;
import org.rendering_app.model.Model;
import org.rendering_app.model.Polygon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class OBJWriter {
    public static void write(Model model, String filePath) throws IOException {
        String content = modelToString(model);
        Files.writeString(Path.of(filePath), content);
    }

    public static String modelToString(Model model) {
        return modelToString(model, null);
    }

    public static String modelToString(Model model, String comment) {
        if (model == null) throw new OBJWriterException("Model cannot be null");

        StringBuilder sb = new StringBuilder();
        if (comment != null && !comment.isEmpty()) sb.append("# ").append(comment).append("\n");

        List<Vector3D> vertices = model.getVertices();
        List<Vector2D> textureVertices = model.getTextureVertices();
        List<Vector3D> normals = model.getNormals();
        List<Polygon> polygons = model.getPolygons();

        // v
        for (int i = 0; i < vertices.size(); i++) {
            Vector3D v = vertices.get(i);
            validateVertex(v, i);
            sb.append("v ")
                    .append(formatFloatCompact(v.getX())).append(" ")
                    .append(formatFloatCompact(v.getY())).append(" ")
                    .append(formatFloatCompact(v.getZ())).append("\n");
        }

        // vt
        if (textureVertices != null && !textureVertices.isEmpty()) {
            sb.append("\n");
            for (int i = 0; i < textureVertices.size(); i++) {
                Vector2D vt = textureVertices.get(i);
                validateTextureVertex(vt, i);
                sb.append("vt ")
                        .append(formatFloatCompact(vt.getX())).append(" ")
                        .append(formatFloatCompact(vt.getY())).append("\n");
            }
        }

        // vn
        if (normals != null && !normals.isEmpty()) {
            sb.append("\n");
            for (int i = 0; i < normals.size(); i++) {
                Vector3D n = normals.get(i);
                validateNormal(n, i);
                sb.append("vn ")
                        .append(formatFloatCompact(n.getX())).append(" ")
                        .append(formatFloatCompact(n.getY())).append(" ")
                        .append(formatFloatCompact(n.getZ())).append("\n");
            }
        }

        // f
        if (polygons != null && !polygons.isEmpty()) {
            sb.append("\n");
            for (int i = 0; i < polygons.size(); i++) {
                Polygon poly = polygons.get(i);
                validatePolygon(poly, i,
                        vertices.size(),
                        textureVertices != null ? textureVertices.size() : 0,
                        normals != null ? normals.size() : 0);

                List<Integer> vi = poly.getVertexIndices();
                List<Integer> vti = poly.getTextureVertexIndices();
                List<Integer> vni = poly.getNormalIndices();

                boolean hasVT = vti != null && !vti.isEmpty();
                boolean hasVN = vni != null && !vni.isEmpty();

                sb.append("f");
                for (int j = 0; j < vi.size(); j++) {
                    sb.append(" ").append(vi.get(j) + 1);

                    if (hasVT || hasVN) {
                        sb.append("/");
                        if (hasVT) sb.append(vti.get(j) + 1);
                        if (hasVN) sb.append("/").append(vni.get(j) + 1);
                    }
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    protected static String formatFloatCompact(float value) {
        if (Float.isNaN(value)) throw new OBJWriterException("Cannot format NaN value");
        if (Float.isInfinite(value)) throw new OBJWriterException("Cannot format infinite value");

        String result = String.format(Locale.ROOT, "%.6f", value);
        if (result.contains(".")) {
            result = result.replaceAll("0*$", "");
            if (result.endsWith(".")) result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    protected static void validateVertex(Vector3D vertex, int index) {
        if (vertex == null) throw new OBJWriterException("Vertex at index " + index + " is null");
        if (Float.isNaN(vertex.getX()) || Float.isNaN(vertex.getY()) || Float.isNaN(vertex.getZ())) {
            throw new OBJWriterException("Vertex at index " + index + " contains NaN values");
        }
        if (Float.isInfinite(vertex.getX()) || Float.isInfinite(vertex.getY()) || Float.isInfinite(vertex.getZ())) {
            throw new OBJWriterException("Vertex at index " + index + " contains infinite values");
        }
    }

    protected static void validateTextureVertex(Vector2D textureVertex, int index) {
        if (textureVertex == null) throw new OBJWriterException("Texture vertex at index " + index + " is null");
        if (Float.isNaN(textureVertex.getX()) || Float.isNaN(textureVertex.getY())) {
            throw new OBJWriterException("Texture vertex at index " + index + " contains NaN values");
        }
        if (Float.isInfinite(textureVertex.getX()) || Float.isInfinite(textureVertex.getY())) {
            throw new OBJWriterException("Texture vertex at index " + index + " contains infinite values");
        }
    }

    protected static void validateNormal(Vector3D normal, int index) {
        if (normal == null) throw new OBJWriterException("Normal at index " + index + " is null");
        if (Float.isNaN(normal.getX()) || Float.isNaN(normal.getY()) || Float.isNaN(normal.getZ())) {
            throw new OBJWriterException("Normal at index " + index + " contains NaN values");
        }
        if (Float.isInfinite(normal.getX()) || Float.isInfinite(normal.getY()) || Float.isInfinite(normal.getZ())) {
            throw new OBJWriterException("Normal at index " + index + " contains infinite values");
        }
    }

    protected static void validatePolygon(Polygon polygon, int polyIndex, int vertexCount, int textureVertexCount, int normalCount) {
        if (polygon == null) throw new OBJWriterException("Polygon at index " + polyIndex + " is null");

        List<Integer> vertexIndices = polygon.getVertexIndices();
        List<Integer> textureVertexIndices = polygon.getTextureVertexIndices();
        List<Integer> normalIndices = polygon.getNormalIndices();

        if (vertexIndices == null) throw new OBJWriterException("Polygon at index " + polyIndex + " has null vertex indices");
        if (vertexIndices.size() < 3) throw new OBJWriterException("Polygon at index " + polyIndex + " has less than 3 vertices");

        for (int v : vertexIndices) {
            if (v < 0 || v >= vertexCount) {
                throw new OBJWriterException("Polygon at index " + polyIndex + " references invalid vertex index " + v);
            }
        }

        if (textureVertexIndices != null && !textureVertexIndices.isEmpty()) {
            if (textureVertexIndices.size() != vertexIndices.size()) {
                throw new OBJWriterException("Polygon at index " + polyIndex + " has mismatched vertex and texture vertex counts");
            }
            for (int t : textureVertexIndices) {
                if (t < 0 || t >= textureVertexCount) {
                    throw new OBJWriterException("Polygon at index " + polyIndex + " references invalid texture vertex index " + t);
                }
            }
        }

        if (normalIndices != null && !normalIndices.isEmpty()) {
            if (normalIndices.size() != vertexIndices.size()) {
                throw new OBJWriterException("Polygon at index " + polyIndex + " has mismatched vertex and normal counts");
            }
            for (int n : normalIndices) {
                if (n < 0 || n >= normalCount) {
                    throw new OBJWriterException("Polygon at index " + polyIndex + " references invalid normal index " + n);
                }
            }
        }
    }
}
