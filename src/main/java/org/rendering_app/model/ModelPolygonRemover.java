package org.rendering_app.model;

import java.util.List;

public final class ModelPolygonRemover {

    private ModelPolygonRemover() {}

    public static int deletePolygonsRange(Model model, int fromInclusive, int toInclusive) {
        if (model == null) return 0;

        List<Polygon> polys = model.getPolygons();
        if (polys == null || polys.isEmpty()) return 0;

        int from = Math.max(0, fromInclusive);
        int to = Math.min(polys.size() - 1, toInclusive);
        if (from > to) return 0;

        int removed = 0;
        for (int i = to; i >= from; i--) {
            polys.remove(i);
            removed++;
        }

        rebuildTrianglesFromPolygons(model);
        return removed;
    }

    public static int[] parseRange(String s) {
        if (s == null) throw new IllegalArgumentException("Empty input");
        String t = s.trim().replace(" ", "");
        if (t.isEmpty()) throw new IllegalArgumentException("Empty input");

        if (!t.contains("-")) {
            int v = Integer.parseInt(t);
            return new int[]{v, v};
        }

        String[] parts = t.split("-", -1);
        if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new IllegalArgumentException("Use N or N-M");
        }
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[1]);
        if (a <= b) return new int[]{a, b};
        return new int[]{b, a};
    }

    public static void rebuildTrianglesFromPolygons(Model model) {
        List<Triangle> tris = model.getTriangles();
        if (tris == null) return;
        tris.clear();
        List<Polygon> polys = model.getPolygons();
        if (polys == null || polys.isEmpty()) return;
        for (Polygon p : polys) {
            List<Integer> v = p.getVertexIndices();
            if (v == null || v.size() < 3) continue;
            List<Integer> vn = p.getNormalIndices();
            List<Integer> vt = p.getTextureVertexIndices();
            boolean hasVN = vn != null && !vn.isEmpty() && vn.size() == v.size();
            boolean hasVT = vt != null && !vt.isEmpty() && vt.size() == v.size();
            for (int i = 1; i < v.size() - 1; i++) {
                int v0 = v.get(0), v1 = v.get(i), v2 = v.get(i + 1);
                Triangle t = new Triangle(v0, v1, v2);

                if (hasVN) t.setNormalIndices(List.of(vn.get(0), vn.get(i), vn.get(i + 1)));
                if (hasVT) t.setTextureVertexIndices(List.of(vt.get(0), vt.get(i), vt.get(i + 1)));

                tris.add(t);
            }
        }
    }
}
