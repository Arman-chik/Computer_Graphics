package org.rendering_app.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Matrix4DTest {

    @Test
    void testConstructorAndValidation() {
        float[][] validMatrix = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        Matrix4D matrix = new Matrix4D(validMatrix);
        assertNotNull(matrix);

        // Проверка невалидной матрицы
        float[][] invalidMatrix = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };

        assertThrows(IllegalArgumentException.class, () -> new Matrix4D(invalidMatrix));
    }

    @Test
    void testMultiplyMatrixByVector4D() {
        float[][] matrixData = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        Matrix4D matrix = new Matrix4D(matrixData);
        Vector4D vector = new Vector4D(1, 2, 3, 4);
        Vector4D result = matrix.multiply(vector);

        assertEquals(30, result.getX(), 0.001f);
        assertEquals(70, result.getY(), 0.001f);
        assertEquals(110, result.getZ(), 0.001f);
        assertEquals(150, result.getW(), 0.001f);
    }

    @Test
    void testMultiplyMatrixByMatrix() {
        float[][] m1Data = {
                {1, 0, 0, 2},
                {0, 2, 0, 3},
                {0, 0, 3, 4},
                {0, 0, 0, 1}
        };

        float[][] m2Data = {
                {2, 0, 0, 0},
                {0, 3, 0, 0},
                {0, 0, 4, 0},
                {0, 0, 0, 1}
        };

        Matrix4D m1 = new Matrix4D(m1Data);
        Matrix4D m2 = new Matrix4D(m2Data);
        Matrix4D result = m1.multiply(m2);

        assertEquals(2, result.getElement(0, 0), 0.001f);
        assertEquals(6, result.getElement(1, 1), 0.001f);
        assertEquals(12, result.getElement(2, 2), 0.001f);
    }

    @Test
    void testMultiplyMatrix4DByVector3D() {
        float[][] matrixData = {
                {1, 0, 0, 10},
                {0, 1, 0, 20},
                {0, 0, 1, 30},
                {0, 0, 0, 1}
        };

        Matrix4D matrix = new Matrix4D(matrixData);
        Vector3D vector = new Vector3D(1, 2, 3);
        Vector3D result = Matrix4D.multiplyMatrix4DByVector3D(matrix, vector);

        assertEquals(11, result.getX(), 0.001f);
        assertEquals(22, result.getY(), 0.001f);
        assertEquals(33, result.getZ(), 0.001f);
    }

    @Test
    void testCreateIdentityMatrix() {
        Matrix4D identity = Matrix4D.createIdentityMatrix();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j) {
                    assertEquals(1, identity.getElement(i, j), 0.001f);
                } else {
                    assertEquals(0, identity.getElement(i, j), 0.001f);
                }
            }
        }
    }

    @Test
    void testGetSetElement() {
        Matrix4D matrix = new Matrix4D();
        matrix.setElement(1, 2, 42.5f);

        assertEquals(42.5f, matrix.getElement(1, 2), 0.001f);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getElement(5, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setElement(5, 5, 0));
    }

    @Test
    void testEqualsAndHashCode() {
        float[][] data = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        Matrix4D m1 = new Matrix4D(data);
        Matrix4D m2 = new Matrix4D(data);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());

        Matrix4D m3 = Matrix4D.createIdentityMatrix();
        assertNotEquals(m1, m3);
    }
}