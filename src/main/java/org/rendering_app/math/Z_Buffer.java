package org.rendering_app.math;

import java.util.Arrays;

public class Z_Buffer {
    private final int width;
    private final int height;

    private final float[] buffer;

    public Z_Buffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = new float[width * height];
    }


    public float get(int x, int y) {
        return buffer[y * width + x];
    }


    public int size() {
        return width * height;
    }


    public void set(int x, int y, float depth) {
        buffer[y * width + x] = depth;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
