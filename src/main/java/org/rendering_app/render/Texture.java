package org.rendering_app.render;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Texture {

    private BufferedImage image;
    private int width;
    private int height;

    public Texture() {
        setImage(null);
    }

    public Texture(BufferedImage image) {
        setImage(image);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        } else {
            this.width = 0;
            this.height = 0;
        }
    }

    public boolean isNull() {
        return image == null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getPixel(int x, int y) {
        if (image == null) return Color.BLACK;
        if (x < 0 || x >= width || y < 0 || y >= height) return Color.BLACK;
        return new Color(image.getRGB(x, y), true);
    }

    public Color getPixel(float u, float v) {
        if (image == null) return Color.BLACK;

        int x = (int) (u * (width - 1));
        int y = (int) (v * (height - 1));

        x = Math.max(0, Math.min(x, width - 1));
        y = Math.max(0, Math.min(y, height - 1));

        return getPixel(x, y);
    }
}
