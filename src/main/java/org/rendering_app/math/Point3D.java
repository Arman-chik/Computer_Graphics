package org.rendering_app.math;

public class Point3D {

    private double x;
    private double y;
    private double z;


    public Point3D() {
        this(0.0f, 0.0f, 0.0f);
    }
    
    
    public Point3D(double x, double y, double z)  {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    
    public Point3D(Vector3D vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
    }
    
    
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    
    
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    
    
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    
}
