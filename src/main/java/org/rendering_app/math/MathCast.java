package org.rendering_app.math;

public class MathCast {
    public static Point2D toPoint2D() {
        return new Point2D();
    }

    public static Point3D toPoint3D() {
        return new Point3D();
    }

    public static Vector3D toVector3D() {
        return new Vector3D();
    }


    public static Vector3D toVector3D(Point3D point) {
        return new Vector3D(point.getX(), point.getY(), point.getZ());
    }


    public static Vector4D toVector4D(Vector3D v) {
        return new Vector4D(v.getX(), v.getY(), v.getZ(), 1.0f);
    }


}

