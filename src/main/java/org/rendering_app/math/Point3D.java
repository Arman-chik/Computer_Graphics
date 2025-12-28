package org.rendering_app.math;

public class Point3D {

    private float x;
    private float y;
    private float z;


    public Point3D() {
        this(0.0f, 0.0f, 0.0f);
    }
    
    
    public Point3D(float x, float y, float z)  {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    
    public Point3D(Vector3D vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
    }
    
    
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    
    
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    
    
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    
}
