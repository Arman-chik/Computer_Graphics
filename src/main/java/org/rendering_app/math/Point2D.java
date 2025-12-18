package org.rendering_app.math;

public class Point2D {
    
    private double x;
    private double y;


    public Point2D() {
        this(0.0, 0.0);
    }


    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public Point2D(Vector2D vector) {
        this(vector.getX(), vector.getY());
    }



    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }



    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

}
