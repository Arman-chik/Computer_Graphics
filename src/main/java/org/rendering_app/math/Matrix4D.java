package org.rendering_app.math;

import java.util.Arrays;

public class Matrix4D {

    private float[][] matrix;


    public Matrix4D(float[][] matrix) {
        if (matrix.length != 4 || matrix[0].length != 4) {
            throw new IllegalArgumentException("Матрица должна быть размером 4x4");
        }

        this.matrix = new float[4][4];

        for (int i = 0; i < 4; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, 4);
        }
    }


    public Matrix4D() {
        this(new float[4][4]);
    }



    public Vector4D multiply(Vector4D vector) {  // умножение матрицы на вектор столбец
        float x = matrix[0][0] * vector.getX() + matrix[0][1] * vector.getY() +
                matrix[0][2] * vector.getZ() + matrix[0][3] * vector.getW();
        float y = matrix[1][0] * vector.getX() + matrix[1][1] * vector.getY() +
                matrix[1][2] * vector.getZ() + matrix[1][3] * vector.getW();
        float z = matrix[2][0] * vector.getX() + matrix[2][1] * vector.getY() +
                matrix[2][2] * vector.getZ() + matrix[2][3] * vector.getW();
        float w = matrix[3][0] * vector.getX() + matrix[3][1] * vector.getY() +
                matrix[3][2] * vector.getZ() + matrix[3][3] * vector.getW();

        return new Vector4D(x, y, z, w);
    }



    public static Vector3D multiplyMatrix4ByVector3D(Matrix4D matrix, Vector3D vertex) {
        float x = vertex.getX() * matrix.getElement(0, 0) + vertex.getY() * matrix.getElement(0, 1) +
                vertex.getZ() * matrix.getElement(0, 2) + matrix.getElement(0, 3);

        float y = vertex.getX() * matrix.getElement(1, 0) + vertex.getY() * matrix.getElement(1, 1) +
                vertex.getZ() * matrix.getElement(1, 2) + matrix.getElement(1, 3);

        float z = vertex.getX() * matrix.getElement(2, 0) + vertex.getY() * matrix.getElement(2, 1) +
                vertex.getZ() * matrix.getElement(2, 2) + matrix.getElement(2, 3);

        float w = vertex.getX() * matrix.getElement(3, 0) + vertex.getY() * matrix.getElement(3, 1) +
                vertex.getZ() * matrix.getElement(3, 2) + matrix.getElement(3, 3);


        if (Math.abs(w) < 1e-6f) {  // w блихок к нулю
            return new Vector3D(x, y, z);
        }

        return new Vector3D(x / w, y / w, z / w);
    }




    public static Matrix4D createIdentityMatrix() {
        float[][] identityMatrix = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};

        return new Matrix4D(identityMatrix);
    }


    public float[][] getMatrix() {
        float[][] copyMatrix = new float[4][4];

        for (int i = 0; i < 4; i++) {
            System.arraycopy(matrix[i], 0, copyMatrix[i], 0, 4);
        }

        return copyMatrix;
    }



    public float getElement(int row, int col) {
        if (row >= 0 && row < 4 && col >= 0 && col < 4) {
            return matrix[row][col];
        }

        throw new IndexOutOfBoundsException("Индекс вышел за пределы матрицы");
    }


    public void setElement(int row, int col, float value) {
        if (row >= 0 && row < 4 && col >= 0 && col < 4) {
            matrix[row][col] = value;
        } else {
            throw new IndexOutOfBoundsException("Индекс вышел за пределы матрицы");
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Matrix4D other = (Matrix4D) obj;

        return Arrays.deepEquals(matrix, other.matrix);
    }



    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

}
