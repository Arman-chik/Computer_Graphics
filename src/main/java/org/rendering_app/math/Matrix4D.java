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


    public static Vector3D multiplyMatrix4DByVector3D(Matrix4D matrix, Vector3D vertex) {

        float x = vertex.getX();
        float y = vertex.getY();
        float z = vertex.getZ();

        float w = 1.0f;

        float resX = matrix.matrix[0][0] * x + matrix.matrix[0][1] * y
                + matrix.matrix[0][2] * z + matrix.matrix[0][3] * w;

        float resY = matrix.matrix[1][0] * x + matrix.matrix[1][1] * y
                + matrix.matrix[1][2] * z + matrix.matrix[1][3] * w;

        float resZ = matrix.matrix[2][0] * x + matrix.matrix[2][1] * y
                + matrix.matrix[2][2] * z + matrix.matrix[2][3] * w;

        float resW = matrix.matrix[3][0] * x + matrix.matrix[3][1] * y
                + matrix.matrix[3][2] * z + matrix.matrix[3][3] * w;

        if (resW == 0 || resW == 1.0f) {
            return new Vector3D(resX, resY, resZ);
        }


        return new Vector3D(resX / resW, resY/ resW, resZ / resW);
    }




    public Matrix4D multiply(Matrix4D other) {
        float[][] result = new float[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++){
                float summa = 0.0f;

                for (int k = 0; k < 4; k++) {
                    summa += matrix[i][k] * other.matrix[k][j];
                }

                result[i][j] = summa;
            }
        }

        return new Matrix4D(result);
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
