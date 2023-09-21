public class MatrixOperations {
    public static Matrix add(Matrix A, Matrix B) {
        int row = B.grid.length, col = B.grid[0].length;
        Matrix mat = new Matrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                mat.grid[i][j] = A.grid[i][j].add(B.grid[i][j]);
            }
        }
        return mat;
    }

    public static Matrix subtract(Matrix A, Matrix B) {
        int row = B.grid.length, col = B.grid[0].length;
        Matrix mat = new Matrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                mat.grid[i][j] = A.grid[i][j].subtract(B.grid[i][j]);
            }
        }
        return mat;
    }

    public static Matrix multiply(Matrix A, Matrix B) {
        int rowA = A.grid.length, colA = A.grid[0].length, rowB = B.grid.length, colB =
                B.grid[0].length;
        if (colA != rowB) {
            System.err.printf("Cannot multiply matrices of dimensions %dx%d and %dx%d. Must be in form jxm and mxk\n",
                    rowA, colA, rowB, colB);
            throw new IllegalArgumentException("Matrices invalid size to be multiplied.");
        }
        Matrix mat = new Matrix(rowA, colB);
        for (int i = 0; i < rowA; i++) {
            for (int k = 0; k < colB; k++) {
                for (int j = 0; j < colA; j++) {
                    mat.grid[i][j] = mat.grid[i][j].add(A.grid[i][k].multiply(B.grid[k][j]));
                }
            }
        }
        return mat;
    }

    /**
     * Gets the reduced row echelon form of a matrix.
     * TODO: Unfinished
     * @param A
     * @return
     */
    public static Matrix getRREF(Matrix A) {
        Matrix mat = new Matrix(A);
        for (int i = 0; i < mat.grid.length; i++) {
            Fraction pivot = mat.grid[i][i];
            for (int j = 0; j < mat.grid.length; j++) {
                mat.grid[i][j] = mat.grid[i][j].divide(pivot);
            }
        }
        return mat;
    }

    /**
     * Gets the reduced echelon form of a matrix.
     * TODO: Unfinished
     * @param A
     * @return
     */
    public static Matrix getREF(Matrix A) {
        Matrix mat = new Matrix(A);
        for (int i = 0; i < mat.grid.length; i++) {
            Fraction pivot = mat.grid[i][i];
            for (int j = 0; j < mat.grid.length; j++) {
                mat.grid[i][j] = mat.grid[i][j].divide(pivot);
            }
        }
        return mat;
    }

    /**
     * Gets the inverse of a matrix.
     * TODO: Unfinished
     * @param A
     * @return
     */
    public static Matrix getInverse(Matrix A) {
        return null;
    }

    public static Fraction getDeterminant(Matrix A) {
        if (A.grid.length != A.grid[0].length) {
            System.err.printf("Cannot get determinant of non-square matrix %dx%d\n", A.grid.length, A.grid[0].length);
            throw new IllegalArgumentException("Matrix is not square");
        }
        return getDeterminant(A, true);
    }

    /**
     * Recursive method to get the determinant of a matrix using cofactor expansion.
     * Currently wrong. TODO: Fix this.
     *
     * @param A
     * @param even
     * @return
     */
    private static Fraction getDeterminant(Matrix A, boolean even) {
        if (A.grid.length != A.grid[0].length) {
            System.err.printf("Cannot get determinant of non-square matrix %dx%d\n", A.grid.length, A.grid[0].length);
            throw new IllegalArgumentException("Matrix is not square");
        }
        if (A.grid.length == 2) {
            return A.grid[0][0].multiply(A.grid[1][1]).subtract(A.grid[0][1].multiply(A.grid[1][0]));
        }
        Fraction det = new Fraction();
        for (int i = 0; i < A.grid.length; i++) {
            Matrix subMatrix = new Matrix(A.grid.length - 1, A.grid[0].length - 1);
            for (int j = 0; j < A.grid.length; j++) {
                for (int k = 0; k < A.grid.length; k++) {
                    if (k < i) {
                        subMatrix.grid[j - 1][k] = A.grid[j][k];
                    } else if (k > i) {
                        subMatrix.grid[j - 1][k - 1] = A.grid[j][k];
                    }
                }
            }
            det = det.add(A.grid[0][i].multiply(getDeterminant(subMatrix, !even)));
        }
        return det;
    }
}
