package org.rendering_app.math;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PixelBuffer {
    private final Map<Point2D, Color> data;


    public PixelBuffer() {
        this.data = new HashMap<>();
    }


    public Map<Point2D, Color> getData() {
        return new HashMap<>(data);
    }
}
