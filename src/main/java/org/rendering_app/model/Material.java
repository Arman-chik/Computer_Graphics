package org.rendering_app.model;

import org.rendering_app.math.Point3D;
import org.rendering_app.math.Vector2D;
import org.rendering_app.render.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Material {
    private boolean showMesh;
    private boolean showTexture;
    private boolean showIllumination;

    private Texture texture;

    private Color baseColor;
    private Color highlightColor;
    private Color currentColor;
    private Color backgroundColor;

    private List<Light> lights;

    public Material() {
        this(false, false, false);
    }

    public Material(boolean showMesh, boolean showIllumination, boolean showTexture) {
        this.showMesh = showMesh;
        this.showTexture = showTexture;
        this.showIllumination = showIllumination;

        this.baseColor = Color.WHITE;
        this.highlightColor = new Color(255, 215, 0);
        this.currentColor = baseColor;
        this.backgroundColor = new Color(45, 45, 45);

        this.lights = new ArrayList<>();
        this.texture = new Texture();
    }

    public Color applyMaterial(float weightA, float weightB, float weightC,
                               Vector2D[] textureVectors,
                               Point3D[] normalVectors,
                               Point3D worldPoint) {

        int[] rgb = new int[]{
                currentColor.getRed(),
                currentColor.getGreen(),
                currentColor.getBlue()
        };

        if (showTexture && texture != null && !texture.isNull()) {
            Texturing.applyTexturing(textureVectors, texture, weightA, weightB, weightC, rgb);
        }

        if (showIllumination && lights != null && !lights.isEmpty()) {
            Illumination.applyIllumination(normalVectors, worldPoint, lights, weightA, weightB, weightC, rgb);
        }

        if (showMesh) {
            Mesh.applyMesh(weightA, weightB, weightC, rgb, backgroundColor);
        }

        rgb[0] = Math.max(0, Math.min(255, rgb[0]));
        rgb[1] = Math.max(0, Math.min(255, rgb[1]));
        rgb[2] = Math.max(0, Math.min(255, rgb[2]));

        return new Color(rgb[0], rgb[1], rgb[2]);
    }

    public boolean isShowMesh() {
        return showMesh;
    }

    public void setShowMesh(boolean showMesh) {
        this.showMesh = showMesh;
    }

    public boolean isShowTexture() {
        return showTexture;
    }

    public void setShowTexture(boolean showTexture) {
        this.showTexture = showTexture;
    }

    public boolean isShowIllumination() {
        return showIllumination;
    }

    public void setShowIllumination(boolean showIllumination) {
        this.showIllumination = showIllumination;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        this.showTexture = texture != null && !texture.isNull();
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor = baseColor;
        if (currentColor.equals(highlightColor)) {
            this.currentColor = baseColor;
        } else {
            this.currentColor = baseColor;
        }
    }

    public List<Light> getLights() {
        return lights;
    }

    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    public void selectHighlightColor() {
        this.currentColor = highlightColor;
    }

    public void selectBaseColor() {
        this.currentColor = baseColor;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
