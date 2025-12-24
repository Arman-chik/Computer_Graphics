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



    public float scalarProduct(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }


    public float length() {
        float length =  (float) Math.sqrt(x * x + y * y);
        return length;
    }


    public Vector2D normalize() {
        float len = length();

        if (len == 0) {
            return new Vector2D(0, 0);
        }

        return new Vector2D(x / len, y / len);
    }




}

