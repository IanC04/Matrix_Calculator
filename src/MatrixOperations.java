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
            for(int k = 0; k < colB; k++) {
                Cell sum = new Cell();
                for (int j = 0; j < colA; j++) {
                    sum = sum.add(A.grid[i][j].multiply(B.grid[j][k]));
                }
                mat.grid[i][k] = sum;
            }
        }
        return mat;
    }

    public static Matrix getRREF(Matrix A) {
        System.out.println("Unimplemented");
        return null;
        Matrix mat = new Matrix(A);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
            }
        }
    }

    public static int getDeterminant(Matrix A) {
        return 0;
    }
}
