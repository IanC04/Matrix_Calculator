public class MatrixOperations {

    private static final Object ZERO = 0;

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
     *
     * @param A
     * @return
     */
    public static Matrix getRREF(Matrix A) {
        Matrix mat = getREF(A);

        normalizePivots(mat);
        return mat;
    }

    /**
     * Gets the reduced echelon form of a matrix.
     * TODO: Unfinished
     *
     * @param A
     * @return the upper-triangular matrix of A
     */
    public static Matrix getREF(Matrix A) {
        Matrix mat = new Matrix(A);
        int pivotColumn = 0;
        for (int i = 0; i < mat.grid.length && pivotColumn < mat.grid[i].length; i++) {
            Fraction pivot = mat.grid[i][pivotColumn];
            if (pivot.equals(ZERO)) {
                // Looks bad, refactor later
                boolean hasPivot = swapPivot(mat, i, pivotColumn);
                if (!hasPivot) {
                    ++pivotColumn;
                    --i;
                    continue;
                }
            }
            for (int j = i + 1; j < mat.grid.length; j++) {
                Fraction divisionRatio = mat.grid[j][pivotColumn].divide(pivot);
                for (int k = pivotColumn; k < mat.grid[j].length; k++) {
                    mat.grid[j][k] = mat.grid[j][k].subtract(divisionRatio.multiply(mat.grid[i][k]));
                }
            }
        }

        return mat;
    }

    private static boolean swapPivot(Matrix A, int row, int col) {
        for (int i = row + 1; i < A.grid.length; i++) {
            if (!A.grid[i][col].equals(ZERO)) {
                flipRows(A, row, i);
                return true;
            }
        }
        return false;
    }

    /**
     * Flips two rows in a matrix.
     *
     * @param A    matrix to flip rows
     * @param row1
     * @param row2
     */
    private static void flipRows(Matrix A, int row1, int row2) {
        if (row1 < 0 || row1 >= A.grid.length || row2 < 0 || row2 >= A.grid.length) {
            System.err.printf("Cannot flip rows %d and %d in matrix with dimensions (%dx%d)\n", row1, row2,
                    A.grid.length, A.grid[0].length);
            throw new IllegalArgumentException("Invalid row indices");
        }
        for (int i = 0; i < A.grid.length; i++) {
            Fraction[] temp = A.grid[row1];
            A.grid[row1] = A.grid[row2];
            A.grid[row2] = temp;
        }
    }

    private static void normalizePivots(Matrix A) {
        int pivotColumn = 0;
        for (int i = 0; i < A.grid.length && pivotColumn < A.grid[i].length; i++) {
            Fraction pivot = A.grid[i][pivotColumn];
            if (pivot.equals(ZERO)) {
                ++pivotColumn;
                --i;
                continue;
            }
            divideRow(A, i, pivot);
        }
    }

    private static void divideRow(Matrix A, int row, Fraction denominator) {
        if (denominator.equals(ZERO)) {
            System.err.println("Cannot divide by zero");
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        for (int i = 0; i < A.grid[row].length; i++) {
            A.grid[row][i] = A.grid[row][i].divide(denominator);
        }
    }

    /**
     * Gets the inverse of a matrix.
     * TODO: Unfinished
     *
     * @param A
     * @return
     */
    public static Matrix getInverse(Matrix A) {
        return null;
    }

    /**
     * Recursive method to get the determinant of a matrix using cofactor expansion.
     *
     * @param A matrix to calculate determinant of
     * @return the value of the determinant
     */
    public static Fraction getDeterminant(Matrix A) {
        if (A.grid.length != A.grid[0].length) {
            System.err.printf("Cannot get determinant of non-square matrix %dx%d\n%s\n", A.grid.length,
                    A.grid[0].length, A);
            throw new IllegalArgumentException("Matrix is not square");
        }
        return getSubDeterminant(A);
    }

    /**
     * @param A
     * @return
     */
    private static Fraction getSubDeterminant(Matrix A) {
        if (A.grid.length != A.grid[0].length) {
            System.err.printf("Couldn't resolve matrix %s\n", A);
            throw new IllegalStateException("Calculation error in solving determinant");
        }
        if (A.grid.length == 2) {
            return A.grid[0][0].multiply(A.grid[1][1]).subtract(A.grid[0][1].multiply(A.grid[1][0]));
        }
        Fraction det = new Fraction();
        boolean sign = true;
        for (int i = 0; i < A.grid.length; i++) {
            Matrix sub = new Matrix(A.grid.length - 1, A.grid.length - 1);
            for (int j = 1; j < A.grid.length; j++) {
                for (int k = 0; k < A.grid.length; k++) {
                    if (k < i) {
                        sub.grid[j - 1][k] = A.grid[j][k];
                    } else if (k > i) {
                        sub.grid[j - 1][k - 1] = A.grid[j][k];
                    }
                }
            }
            Fraction result = A.grid[0][i].multiply(getSubDeterminant(sub));
            if (!sign) {
                result.invertSign();
            }
            det = det.add(result);
            sign = !sign;
        }
        return det;
    }
}
