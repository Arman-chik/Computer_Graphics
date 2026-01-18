package org.rendering_app.render;

import org.rendering_app.math.Vector3D;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

public class TestLight {

    @Test
    void testLightCreation() {
        Color color = Color.RED;
        Vector3D position = new Vector3D(1, 2, 3);

        Light light = new Light(color, position);

        assertEquals(color, light.getColor());
        assertEquals(position, light.getPosition());
    }

    @Test
    void testLightSetters() {
        Light light = new Light(Color.RED, new Vector3D(1, 2, 3));

        Color newColor = Color.BLUE;
        Vector3D newPos = new Vector3D(4, 5, 6);

        light.setColor(newColor);
        light.setPosition(newPos);

        assertEquals(newColor, light.getColor());
        assertEquals(newPos, light.getPosition());
    }

    @Test
    void testLightEquality() {
        Light light1 = new Light(Color.RED, new Vector3D(1, 2, 3));
        Light light2 = new Light(Color.RED, new Vector3D(1, 2, 3));

        assertEquals(light1.getColor(), light2.getColor());
        assertEquals(light1.getPosition(), light2.getPosition());
    }
}