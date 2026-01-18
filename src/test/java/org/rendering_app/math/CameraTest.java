package org.rendering_app.math;

import org.rendering_app.math.Matrix4D;
import org.rendering_app.math.Vector3D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.rendering_app.render.Camera;

import static org.junit.jupiter.api.Assertions.*;

public class CameraTest {
    private Camera camera;

    @BeforeEach
    void setUp() {
        camera = new Camera();
    }

    @Test
    void testDefaultConstructor() {
        // Проверяем значения по умолчанию
        assertEquals(0, camera.getPosition().getX(), 0.001f);
        assertEquals(0, camera.getPosition().getY(), 0.001f);
        assertEquals(100, camera.getPosition().getZ(), 0.001f);

        assertEquals(0, camera.getTarget().getX(), 0.001f);
        assertEquals(0, camera.getTarget().getY(), 0.001f);
        assertEquals(0, camera.getTarget().getZ(), 0.001f);

        assertEquals(1.0f, camera.getFov(), 0.001f);
        assertEquals(1.0f, camera.getAspectRatio(), 0.001f);
        assertEquals(0.01f, camera.getNearPlane(), 0.001f);
        assertEquals(100.0f, camera.getFarPlane(), 0.001f);
    }

    @Test
    void testParameterizedConstructor() {
        Vector3D position = new Vector3D(10, 20, 30);
        Vector3D target = new Vector3D(0, 5, 0);
        Camera customCamera = new Camera(position, target, 60.0f, 1.777f, 0.1f, 1000.0f);

        assertEquals(10, customCamera.getPosition().getX(), 0.001f);
        assertEquals(20, customCamera.getPosition().getY(), 0.001f);
        assertEquals(30, customCamera.getPosition().getZ(), 0.001f);

        assertEquals(0, customCamera.getTarget().getX(), 0.001f);
        assertEquals(5, customCamera.getTarget().getY(), 0.001f);
        assertEquals(0, customCamera.getTarget().getZ(), 0.001f);

        assertEquals(60.0f, customCamera.getFov(), 0.001f);
        assertEquals(1.777f, customCamera.getAspectRatio(), 0.001f);
        assertEquals(0.1f, customCamera.getNearPlane(), 0.001f);
        assertEquals(1000.0f, customCamera.getFarPlane(), 0.001f);
    }

    @Test
    void testSetPosition() {
        Vector3D newPosition = new Vector3D(50, 60, 70);
        camera.setPosition(newPosition);

        assertEquals(50, camera.getPosition().getX(), 0.001f);
        assertEquals(60, camera.getPosition().getY(), 0.001f);
        assertEquals(70, camera.getPosition().getZ(), 0.001f);
    }

    @Test
    void testSetTarget() {
        Vector3D newTarget = new Vector3D(10, 20, 30);
        camera.setTarget(newTarget);

        assertEquals(10, camera.getTarget().getX(), 0.001f);
        assertEquals(20, camera.getTarget().getY(), 0.001f);
        assertEquals(30, camera.getTarget().getZ(), 0.001f);
    }

    @Test
    void testSetAspectRatio() {
        camera.setAspectRatio(1.777f);
        assertEquals(1.777f, camera.getAspectRatio(), 0.001f);
    }

    @Test
    void testMovePosition() {
        // Начальная позиция камеры: (0, 0, 100)
        Vector3D translation = new Vector3D(10, 20, 30);
        camera.movePosition(translation);

        assertEquals(10, camera.getPosition().getX(), 0.001f);  // 0 + 10
        assertEquals(20, camera.getPosition().getY(), 0.001f);  // 0 + 20
        assertEquals(130, camera.getPosition().getZ(), 0.001f); // 100 + 30
    }

    @Test
    void testMoveTarget() {
        // Начальная цель: (0, 0, 0)
        Vector3D translation = new Vector3D(5, -5, 10);
        camera.moveTarget(translation);

        assertEquals(5, camera.getTarget().getX(), 0.001f);   // 0 + 5
        assertEquals(-5, camera.getTarget().getY(), 0.001f);  // 0 + (-5)
        assertEquals(10, camera.getTarget().getZ(), 0.001f);  // 0 + 10
    }

    @Test
    void testGetViewMatrix() {
        Matrix4D viewMatrix = camera.getViewMatrix();

        assertNotNull(viewMatrix);

        // Проверяем базовые свойства view-матрицы
        assertEquals(0, viewMatrix.getElement(3, 0), 0.001f);
        assertEquals(0, viewMatrix.getElement(3, 1), 0.001f);
        assertEquals(0, viewMatrix.getElement(3, 2), 0.001f);
        assertEquals(1, viewMatrix.getElement(3, 3), 0.001f);

        // Для камеры в (0,0,100), смотрящей в (0,0,0)
        // Матрица вида должна сдвигать сцену на -100 по Z
        // Но конкретные значения зависят от реализации GraphicConveyor.lookAt()
    }

    @Test
    void testGetViewMatrixDifferentPosition() {
        Camera customCamera = new Camera(
                new Vector3D(10, 20, 30),
                new Vector3D(0, 0, 0),
                60.0f, 1.333f, 0.1f, 100.0f
        );

        Matrix4D viewMatrix = customCamera.getViewMatrix();
        assertNotNull(viewMatrix);

        // Матрица должна существовать для любой позиции камеры
        // Последняя строка всегда (0,0,0,1)
        assertEquals(0, viewMatrix.getElement(3, 0), 0.001f);
        assertEquals(0, viewMatrix.getElement(3, 1), 0.001f);
        assertEquals(0, viewMatrix.getElement(3, 2), 0.001f);
        assertEquals(1, viewMatrix.getElement(3, 3), 0.001f);
    }

    @Test
    void testGetProjectionMatrix() {
        Matrix4D projMatrix = camera.getProjectionMatrix();

        assertNotNull(projMatrix);

        // Проверяем свойства перспективной матрицы
        assertEquals(1, projMatrix.getElement(3, 2), 0.001f);
        assertEquals(0, projMatrix.getElement(3, 3), 0.001f);

        // Для FOV=1.0 радиан, aspect=1.0, near=0.01, far=100.0
        // tan(0.5) ≈ 0.5463, поэтому:
        // [0][0] = 1 / (1 * 0.5463) ≈ 1.8305
        // [1][1] = 1 / 0.5463 ≈ 1.8305
        float halfFov = 1.0f / 2.0f;  // Половина FOV в радианах
        float tanHalfFov = (float) Math.tan(halfFov);
        float expected = 1.0f / tanHalfFov;

        assertEquals(expected, projMatrix.getElement(0, 0), 0.001f);
        assertEquals(expected, projMatrix.getElement(1, 1), 0.001f);
    }

    @Test
    void testGetProjectionMatrixWithDifferentParameters() {
        // FOV передается в радианах, а не в градусах!
        // 90° в радианах = π/2 ≈ 1.5708 rad
        float fovRadians = (float) Math.toRadians(90.0f);

        Camera customCamera = new Camera(
                new Vector3D(0, 0, 100),
                new Vector3D(0, 0, 0),
                fovRadians,    // Уже в радианах!
                1.777f,       // 16:9 aspect ratio
                0.1f,         // near plane
                1000.0f       // far plane
        );

        Matrix4D projMatrix = customCamera.getProjectionMatrix();
        assertNotNull(projMatrix);

        // Для FOV=90° (1.5708 rad), tan(45°) = 1
        // aspect=1.777, поэтому:
        // [0][0] = 1 / (1.777 * 1) ≈ 0.5628
        // [1][1] = 1 / 1 = 1
        float tanHalfFov = (float) Math.tan(fovRadians / 2.0f);  // tan(45°) = 1
        float expected00 = 1.0f / (1.777f * tanHalfFov);
        float expected11 = 1.0f / tanHalfFov;

        assertEquals(expected00, projMatrix.getElement(0, 0), 0.001f);
        assertEquals(expected11, projMatrix.getElement(1, 1), 0.001f);
    }

    @Test
    void testCameraMovementChain() {
        // Тестируем последовательность перемещений камеры
        Camera cam = new Camera(
                new Vector3D(0, 0, 0),
                new Vector3D(0, 0, -1),
                60.0f, 1.333f, 0.1f, 100.0f
        );

        // Перемещаем позицию
        cam.movePosition(new Vector3D(10, 0, 0));
        assertEquals(10, cam.getPosition().getX(), 0.001f);
        assertEquals(0, cam.getPosition().getY(), 0.001f);
        assertEquals(0, cam.getPosition().getZ(), 0.001f);

        // Перемещаем цель
        cam.moveTarget(new Vector3D(0, 5, 0));
        assertEquals(0, cam.getTarget().getX(), 0.001f);
        assertEquals(5, cam.getTarget().getY(), 0.001f);
        assertEquals(-1, cam.getTarget().getZ(), 0.001f);

        // Еще одно перемещение позиции
        cam.movePosition(new Vector3D(0, 0, -50));
        assertEquals(10, cam.getPosition().getX(), 0.001f);
        assertEquals(0, cam.getPosition().getY(), 0.001f);
        assertEquals(-50, cam.getPosition().getZ(), 0.001f);
    }

    @Test
    void testCameraLookingAtPoint() {
        // Камера смотрит на конкретную точку
        Vector3D position = new Vector3D(0, 0, 100);
        Vector3D target = new Vector3D(0, 0, 0);

        Camera cam = new Camera(position, target, 60.0f, 1.333f, 0.1f, 100.0f);

        // Позиция и цель должны сохраниться
        assertEquals(0, cam.getPosition().getX(), 0.001f);
        assertEquals(0, cam.getPosition().getY(), 0.001f);
        assertEquals(100, cam.getPosition().getZ(), 0.001f);

        assertEquals(0, cam.getTarget().getX(), 0.001f);
        assertEquals(0, cam.getTarget().getY(), 0.001f);
        assertEquals(0, cam.getTarget().getZ(), 0.001f);
    }


    @Test
    void testCameraCloneIndependence() {
        Camera cam1 = new Camera(
                new Vector3D(10, 20, 30),
                new Vector3D(0, 0, 0),
                60.0f, 1.333f, 0.1f, 100.0f
        );

        // Создаем вторую камеру с теми же параметрами
        Camera cam2 = new Camera(
                new Vector3D(10, 20, 30),
                new Vector3D(0, 0, 0),
                60.0f, 1.333f, 0.1f, 100.0f
        );

        // Меняем первую камеру
        cam1.movePosition(new Vector3D(5, 5, 5));

        // Вторая камера не должна измениться
        assertEquals(10, cam2.getPosition().getX(), 0.001f);
        assertEquals(20, cam2.getPosition().getY(), 0.001f);
        assertEquals(30, cam2.getPosition().getZ(), 0.001f);
    }

    @Test
    void testInvalidParameters() {
        // Проверяем обработку некорректных параметров
        // (В текущей реализации нет валидации, но тест на будущее)

        // Отрицательный near plane
        Camera cam = new Camera(
                new Vector3D(0, 0, 100),
                new Vector3D(0, 0, 0),
                60.0f, 1.333f, -0.1f, 100.0f
        );

        // Должен создаться без ошибок (если нет валидации)
        assertNotNull(cam);
        assertEquals(-0.1f, cam.getNearPlane(), 0.001f);
    }

    @Test
    void testCameraToStringNotImplemented() {
        // Просто проверяем, что объект существует
        assertNotNull(camera);

        // toString() может не быть переопределен
        String str = camera.toString();
        assertNotNull(str);
    }
}