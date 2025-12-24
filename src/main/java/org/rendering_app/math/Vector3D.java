package org.rendering_app.math;

public class Vector3D {
    private float x;
    private float y;
    private float z;

    
    public Vector3D() {
        this(0.0f, 0.0f, 0.0f);
    }


    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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



    public Vector3D add(Vector3D other) {
        return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
    }


    public Vector3D subtract(Vector3D other) {

        return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }



    public Vector3D multiply(float scalar) {
        return new Vector3D(this.x * scalar, this.y * scalar, this.z * scalar);
    }



    public float scalarProduct(Vector3D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }


    public float length() {
        float length = (float) Math.sqrt(x * x + y * y + z * z);
        return length;
    }



    public Vector3D normalize() {
        float len = length();

        if (len == 0) {
            return new Vector3D(0, 0, 0);
        }

        return new Vector3D(x / len, y / len, z / len);
    }

}
