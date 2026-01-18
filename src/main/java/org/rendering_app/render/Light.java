package org.rendering_app.render;

import org.rendering_app.math.Vector3D;

import java.awt.Color;

public class Light {
    private Color color;
    private Vector3D position;

    public Light(Color color, Vector3D position) {
        this.color = color;
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }
}
