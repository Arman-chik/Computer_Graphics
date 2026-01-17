package org.rendering_app.math;

public class MathCast {

    public static Point2D toPoint2D(Vector3D vertex, int width, int height) {
        return new Point2D(vertex.getX() * width + width / 2.0f, -vertex.getY() * height + height / 2.0f);
    }

    public static Point3D toPoint3D(Vector3D vertex, int width, int height) {
        return new Point3D(vertex.getX() * width + width / 2.0f, -vertex.getY() * height + height / 2.0f, vertex.getZ());
    }

    public static Vector3D toVector3D(Vector4D v) {
        if (v.getW() == 0) {
            return new Vector3D(v.getX(), v.getY(), v.getZ());
        }

        return new Vector3D(v.getX() / v.getW(), v.getY() / v.getW(), v.getZ() / v.getW());
    }


    public static Vector3D toVector3D(Point3D point) {
        return new Vector3D(point.getX(), point.getY(), point.getZ());
    }


    public static Vector4D toVector4D(Vector3D v) {
        return new Vector4D(v.getX(), v.getY(), v.getZ(), 1.0f);
    }


}

