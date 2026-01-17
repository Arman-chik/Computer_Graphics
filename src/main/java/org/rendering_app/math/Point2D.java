package org.rendering_app.math;

public class Point2D {
    
    private float x;
    private float y;


    public Point2D() {
        this(0.0f, 0.0f);
    }


    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public Point2D(Vector2D vector) {
        this(vector.getX(), vector.getY());
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



    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(x) ^ Float.hashCode(y);
    }

    @Override
    public String toString() {
        return String.format("Point2D(%.2f, %.2f)", x, y);
    }

}
