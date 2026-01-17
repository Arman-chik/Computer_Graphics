package org.rendering_app.math;

import org.rendering_app.model.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class GraphicConveyorTest {
    private Model model;

    @BeforeEach
    void setUp() {
        model = new Model();
        model.addVertex(new Vector3D(1, 1, 1));
        model.addVertex(new Vector3D(2, 2, 2));
        model.addVertex(new Vector3D(3, 3, 3));
    }

    @Test
    void testTranslate() {
        GraphicConveyor.translate(model, 5, 5, 5);
        List<Vector3D> vertices = model.getVertices();

        assertEquals(6, vertices.get(0).getX(), 0.001f);
        assertEquals(6, vertices.get(0).getY(), 0.001f);
        assertEquals(6, vertices.get(0).getZ(), 0.001f);
    }

    @Test
    void testScale() {
        GraphicConveyor.scale(model, 2, 3, 4);
        List<Vector3D> vertices = model.getVertices();

        assertEquals(2, vertices.get(0).getX(), 0.001f);
        assertEquals(3, vertices.get(0).getY(), 0.001f);
        assertEquals(4, vertices.get(0).getZ(), 0.001f);
    }

    @Test
    void testRotateZ() {
        // Создаем простую модель с одной вершиной
        Model simpleModel = new Model();
        simpleModel.addVertex(new Vector3D(1, 0, 0));

        // Вращаем на 90 градусов только вокруг Z
        GraphicConveyor.rotate(simpleModel, 90, 0, 0);
        Vector3D result = simpleModel.getVertices().get(0);

        // (1,0,0) после вращения на 90° вокруг Z должно стать (0,1,0)
        assertEquals(0, result.getX(), 0.001f);
        assertEquals(1, result.getY(), 0.001f);
        assertEquals(0, result.getZ(), 0.001f);
    }

    @Test
    void testRotateY() {
        Model simpleModel = new Model();
        simpleModel.addVertex(new Vector3D(1, 0, 0));

        // Вращаем на 90 градусов только вокруг Y (psi=90)
        GraphicConveyor.rotate(simpleModel, 0, 90, 0);
        Vector3D result = simpleModel.getVertices().get(0);

        // (1,0,0) после вращения на 90° вокруг Y должно стать (0,0,-1)
        assertEquals(0, result.getX(), 0.001f);
        assertEquals(0, result.getY(), 0.001f);
        assertEquals(-1, result.getZ(), 0.001f);
    }

    @Test
    void testRotateX() {
        Model simpleModel = new Model();
        simpleModel.addVertex(new Vector3D(0, 1, 0));

        // Вращаем на 90 градусов только вокруг X (theta=90)
        GraphicConveyor.rotate(simpleModel, 0, 0, 90);
        Vector3D result = simpleModel.getVertices().get(0);

        // (0,1,0) после вращения на 90° вокруг X должно стать (0,0,1)
        assertEquals(0, result.getX(), 0.001f);
        assertEquals(0, result.getY(), 0.001f);
        assertEquals(1, result.getZ(), 0.001f);
    }

    @Test
    void testRotateXYZ() {
        // Тест с исходной точкой (1,1,1)
        Model testModel = new Model();
        testModel.addVertex(new Vector3D(1, 1, 1));

        GraphicConveyor.rotate(testModel, 90, 0, 0); // Только вращение вокруг Z
        Vector3D result = testModel.getVertices().get(0);

        // (1,1,1) после вращения на 90° вокруг Z:
        // x' = 1*cos(90) - 1*sin(90) = 0 - 1 = -1
        // y' = 1*sin(90) + 1*cos(90) = 1 + 0 = 1
        // z' = 1
        assertEquals(-1, result.getX(), 0.001f);
        assertEquals(1, result.getY(), 0.001f);
        assertEquals(1, result.getZ(), 0.001f);
    }

    @Test
    void testLookAt() {
        Vector3D eye = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Vector3D up = new Vector3D(0, 1, 0);

        Matrix4D viewMatrix = GraphicConveyor.lookAt(eye, target, up);

        assertNotNull(viewMatrix);
        assertEquals(1, viewMatrix.getElement(3, 3), 0.001f); // Последний элемент должен быть 1
    }

    @Test
    void testPerspective() {
        Matrix4D projMatrix = GraphicConveyor.perspective(60.0f, 1.333f, 0.1f, 100.0f);

        assertNotNull(projMatrix);
        assertEquals(1, projMatrix.getElement(3, 2), 0.001f); // Элемент [3][2] должен быть 1
        assertEquals(0, projMatrix.getElement(3, 3), 0.001f); // Элемент [3][3] должен быть 0

        assertNotEquals(0, projMatrix.getElement(0, 0), 0.001f); // Масштаб по X не нулевой
        assertNotEquals(0, projMatrix.getElement(1, 1), 0.001f); // Масштаб по Y не нулевой
        assertEquals(0, projMatrix.getElement(0, 1), 0.001f);    // Недиагональные элементы нулевые
        assertEquals(0, projMatrix.getElement(1, 0), 0.001f);
    }
}