public class Operations {
    public Matrix add(Matrix A, Matrix B) {
        return addsub(A, B, true);
    }

    public Matrix addsub(Matrix A, Matrix B, boolean add) {
        int row = B.grid.length, col = B.grid[0].length;
        Matrix mat = new Matrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                mat.grid[i][j] =
                        add ? grid[i][j] + other.grid[i][j] : grid[i][j] - other.grid[i][j];
            }
        }
        return mat;
    }

    public Matrix subtract(Matrix other) {
        return addsub(other, false);
    }

    public Matrix multiply(Matrix other) {
        int rowA = grid.length, colA = grid[0].length, rowB = other.grid.length, colB =
                other.grid[0].length;
        Matrix mat = new Matrix(rowA, colB);
        for (int i = 0; i < rowA; i++) {
            for (int j = 0; j < colB; j++) {
                int sum = 0;
                for (int index = 0; index < colA; index++) {
                    sum += grid[i][index] * other.grid[index][j];
                }
                mat.grid[i][j] = sum;
            }
        }
        return mat;
    }

    public Matrix getRREF() {
        Matrix mat = new Matrix(this);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {

            }
        }
    }

    public int getDeterminant() {
        return 0;
    }
}
