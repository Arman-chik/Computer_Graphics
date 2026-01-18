package org.rendering_app.render;

import org.rendering_app.math.GraphicConveyor;
import org.rendering_app.math.Matrix4D;
import org.rendering_app.math.Vector3D;

public class Camera {
    private Vector3D position;
    private Vector3D target;
    private float fov;
    private float aspectRatio;

    private float nearPlane;
    private float farPlane;


    public Camera() {
        this(new Vector3D(0, 0, 100), new Vector3D(0, 0, 0),
                1.0f, 1.0f, 0.01f, 100.0f);
    }



    public Camera(Vector3D position, Vector3D target, float fov, float aspectRatio, float nearPlane, float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }



    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public void setTarget(Vector3D target) {
        this.target = target;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }



    public void movePosition(Vector3D translation) {
        this.position = new Vector3D(position.getX() + translation.getX(),
                position.getY() + translation.getY(), position.getZ() + translation.getZ());
    }


    public void moveTarget(Vector3D translation) {
        this.target = target.add(translation);
    }



    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getTarget() {
        return target;
    }


    public Matrix4D getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    public Matrix4D getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    public float getFov() {
        return fov;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public float getNearPlane() {
        return nearPlane;
    }

    public float getFarPlane() {
        return farPlane;
    }
}

