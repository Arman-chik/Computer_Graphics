package org.rendering_app.math;

public class Vector4D {

    private float x;
    private float y;
    private float z;
    private float w;


    public Vector4D() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }



    public Vector4D(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }



    public Vector4D(Vector3D v, float w) {
        this(v.getX(), v.getY(), v.getZ(), w);
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

    public float getW() {
        return w;
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

    public void setW(float w) {
        this.w = w;
    }



    public Vector4D add(Vector4D other) {
        return new Vector4D(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w);
    }



    public Vector4D subtract(Vector4D other) {

        return new Vector4D(this.x - other.x, this.y - other.y, this.z - other.z, this.w - other.w);
    }



    public Vector4D multiply(float scalar) {
        return new Vector4D(this.x * scalar, this.y * scalar, this.z * scalar, this.w * scalar);
    }


    public float dot(Vector4D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    }


    public float scalarProduct(Vector4D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    }



    public float length() {
        float length = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        return length;
    }


    public Vector4D normalize() {
        float len = length();

        if (len == 0) {
            return new Vector4D(0, 0, 0, 0);
        }

        return new Vector4D(x / len, y / len, z / len, w / len);
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Vector4D other = (Vector4D) obj;

        return Float.compare(other.x, x) == 0 && Float.compare(other.y, y) == 0
                && Float.compare(other.z, z) == 0 && Float.compare(other.w, w) == 0;
    }


    @Override
    public int hashCode() {
        return Float.hashCode(x) ^ Float.hashCode(y) ^ Float.hashCode(z) ^ Float.hashCode(w);
    }


    @Override
    public String toString() {
        return String.format("Vector4D(%.2f, %.2f, %.2f, %.2f)", x, y, z, w);
    }
}
