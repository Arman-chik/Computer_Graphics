package org.rendering_app.math;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PixelBuffer {
    private final Map<Point2D, Color> data;


    public PixelBuffer() {
        this.data = new HashMap<>();
    }


    public void add(Point2D point, Color color) {
        data.put(point, color);
    }


    public Color getColor(Point2D point) {
        return data.getOrDefault(point, Color.BLACK);
    }



    public Map<Point2D, Color> getData() {
        return new HashMap<>(data);
    }


    public void forEach(BiConsumer<Point2D, Color> consumer) {
        data.forEach(consumer);
    }


    public void clear() {
        data.clear();
    }
}
