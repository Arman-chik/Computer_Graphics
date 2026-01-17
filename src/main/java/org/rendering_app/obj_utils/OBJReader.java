package org.rendering_app.obj_utils;

import org.rendering_app.math.Vector2D;
import org.rendering_app.math.Vector3D;
import org.rendering_app.model.Model;
import org.rendering_app.model.Polygon;
import org.rendering_app.model.Triangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class OBJReader {
    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public static Model read(String fileContent) {
        Model model = new Model();

        int lineInd = 0;
        Scanner scanner = new Scanner(fileContent);

        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            ++lineInd;

            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }

            ArrayList<String> words = new ArrayList<>(Arrays.asList(trimmed.split("\\s+")));
            if (words.isEmpty()) continue;

            String token = words.getFirst();
            words.removeFirst();

            switch (token) {
                case OBJ_VERTEX_TOKEN -> model.getVertices().add(parseVertex(words, lineInd));
                case OBJ_TEXTURE_TOKEN -> model.getTextureVertices().add(parseTextureVertex(words, lineInd));
                case OBJ_NORMAL_TOKEN -> model.getNormals().add(parseNormal(words, lineInd));
                case OBJ_FACE_TOKEN -> model.getPolygons().add(parseFace(words, lineInd, model));
                default -> {
                }
            }
        }

        triangulateToModelTriangles(model);

        return model;
    }

    private static float parseObjFloat(String raw, int lineInd) {
        try {
            return Float.parseFloat(raw.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new OBJReaderException("Failed to parse float value: " + raw, lineInd, e);
        }
    }

    protected static Vector3D parseVertex(final List<String> w, int lineInd) {
        try {
            return new Vector3D(
                    parseObjFloat(w.get(0), lineInd),
                    parseObjFloat(w.get(1), lineInd),
                    parseObjFloat(w.get(2), lineInd)
            );
        } catch (IndexOutOfBoundsException e) {
            throw new OBJReaderException("Too few vertex arguments.", lineInd, e);
        }
    }

    protected static Vector2D parseTextureVertex(final List<String> w, int lineInd) {
        try {
            return new Vector2D(
                    parseObjFloat(w.get(0), lineInd),
                    parseObjFloat(w.get(1), lineInd)
            );
        } catch (IndexOutOfBoundsException e) {
            throw new OBJReaderException("Too few texture vertex arguments.", lineInd, e);
        }
    }

    protected static Vector3D parseNormal(final List<String> w, int lineInd) {
        try {
            return new Vector3D(
                    parseObjFloat(w.get(0), lineInd),
                    parseObjFloat(w.get(1), lineInd),
                    parseObjFloat(w.get(2), lineInd)
            );
        } catch (IndexOutOfBoundsException e) {
            throw new OBJReaderException("Too few normal arguments.", lineInd, e);
        }
    }

    protected static Polygon parseFace(final List<String> words, int lineInd, Model model) {
        ArrayList<Integer> v = new ArrayList<>();
        ArrayList<Integer> vt = new ArrayList<>();
        ArrayList<Integer> vn = new ArrayList<>();

        for (String s : words) {
            parseFaceWord(s, v, vt, vn, lineInd, model);
        }

        if (v.size() < 3) {
            throw new OBJReaderException("Polygon must have at least 3 vertices.", lineInd);
        }

        if (vn.isEmpty()) {
            int normalIndex = generateFlatNormalForFace(model, v, lineInd);
            for (int i = 0; i < v.size(); i++) vn.add(normalIndex);
        }

        Polygon p = new Polygon();
        p.setVertexIndices(v);
        p.setTextureVertexIndices(vt);
        p.setNormalIndices(vn);
        return p;
    }

    private static int generateFlatNormalForFace(Model model, List<Integer> vertexIndices, int lineInd) {
        try {
            Vector3D a = model.getVertices().get(vertexIndices.get(0));
            Vector3D b = model.getVertices().get(vertexIndices.get(1));
            Vector3D c = model.getVertices().get(vertexIndices.get(2));

            float abx = b.getX() - a.getX();
            float aby = b.getY() - a.getY();
            float abz = b.getZ() - a.getZ();

            float acx = c.getX() - a.getX();
            float acy = c.getY() - a.getY();
            float acz = c.getZ() - a.getZ();

            float nx = aby * acz - abz * acy;
            float ny = abz * acx - abx * acz;
            float nz = abx * acy - aby * acx;

            float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
            if (len < 1e-8f) {
                nx = 0; ny = 1; nz = 0;
                len = 1;
            }

            Vector3D n = new Vector3D(nx / len, ny / len, nz / len);
            model.getNormals().add(n);
            return model.getNormals().size() - 1;
        } catch (Exception e) {
            throw new OBJReaderException("Failed to generate normal for face.", lineInd, e);
        }
    }

    protected static void parseFaceWord(
            String wordInLine,
            List<Integer> onePolygonVertexIndices,
            List<Integer> onePolygonTextureVertexIndices,
            List<Integer> onePolygonNormalIndices,
            int lineInd,
            Model model
    ) {
        try {
            String[] idx = wordInLine.split("/", -1);

            int vIdx = parseObjIndex(idx[0], model.getVertices().size(), lineInd, wordInLine);
            onePolygonVertexIndices.add(vIdx);

            if (idx.length >= 2 && !idx[1].isEmpty()) {
                int vtIdx = parseObjIndex(idx[1], model.getTextureVertices().size(), lineInd, wordInLine);
                onePolygonTextureVertexIndices.add(vtIdx);
            }

            if (idx.length >= 3 && !idx[2].isEmpty()) {
                int vnIdx = parseObjIndex(idx[2], model.getNormals().size(), lineInd, wordInLine);
                onePolygonNormalIndices.add(vnIdx);
            }

            if (idx.length > 3) {
                throw new OBJReaderException("Invalid face element: " + wordInLine, lineInd);
            }

        } catch (NumberFormatException e) {
            throw new OBJReaderException("Failed to parse int value: " + wordInLine, lineInd, e);
        }
    }

    private static int parseObjIndex(String raw, int currentSize, int lineInd, String context) {
        int idx = Integer.parseInt(raw);
        if (idx == 0) {
            throw new OBJReaderException("OBJ index cannot be 0: " + context, lineInd);
        }
        if (idx > 0) {
            return idx - 1;
        }
        return currentSize + idx;
    }

    private static void triangulateToModelTriangles(Model model) {
        model.getTriangles().clear();

        for (Polygon p : model.getPolygons()) {
            List<Integer> v = p.getVertexIndices();
            List<Integer> vn = p.getNormalIndices();
            List<Integer> vt = p.getTextureVertexIndices();

            boolean hasVN = vn != null && !vn.isEmpty() && vn.size() == v.size();
            boolean hasVT = vt != null && !vt.isEmpty() && vt.size() == v.size();

            for (int i = 1; i < v.size() - 1; i++) {
                int v0 = v.get(0), v1 = v.get(i), v2 = v.get(i + 1);

                Triangle t = new Triangle(v0, v1, v2);

                if (hasVN) {
                    t.setNormalIndices(List.of(vn.get(0), vn.get(i), vn.get(i + 1)));
                }

                if (hasVT) {
                    t.setTextureVertexIndices(List.of(vt.get(0), vt.get(i), vt.get(i + 1)));
                }

                model.getTriangles().add(t);
            }
        }
    }
}
