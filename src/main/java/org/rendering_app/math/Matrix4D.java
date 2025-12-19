package org.rendering_app.math;

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



    public float get(int row, int col) {
        if (row >= 0 && row < 4 && col >= 0 && col < 4) {
            return matrix[row][col];
        }

        throw new IndexOutOfBoundsException("Индекс вышел за пределы матрицы");
    }


    public void set(int row, int col, float value) {
        if (row >= 0 && row < 4 && col >= 0 && col < 4) {
            matrix[row][col] = value;
        } else {
            throw new IndexOutOfBoundsException("Индекс вышел за пределы матрицы");
        }
    }

}
