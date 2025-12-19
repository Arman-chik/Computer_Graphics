package org.rendering_app.math;

public class Vector2D {
    private float x;
    private float y;


    public Vector2D() {
        this(0.0f, 0.0f);
    }



    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }



    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }



    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }


    public Vector2D subtract(Vector2D other) {

        return new Vector2D(this.x - other.x, this.y - other.y);
    }


    public Vector2D multiply(float scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }


}

