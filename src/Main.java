import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Matrix m = new Matrix(keyboard);
    }

    private static class Matrix {
        private final int[][] grid;

        private Matrix(int row, int col) {
            grid = new int[row][col];
        }

        /**
         * Copies other matrix to this matrix
         *
         * @param other
         */
        private Matrix(Matrix other) {
            int row = other.grid.length, col = other.grid[0].length;
            grid = new int[row][col];
            for (int i = 0; i < row; i++) {
                System.arraycopy(other.grid[i], 0, grid[i], 0, col);
            }
        }

        private Matrix(Scanner s) {
            System.out.print("How many rows: ");
            int row = s.nextInt();
            System.out.print("How many columns: ");
            int col = s.nextInt();
            s.nextLine();
            grid = new int[row][col];
            for (int i = 0; i < row; i++) {
                System.out.println(
                        "Enter ints of " + Suffix.values()[i] + " row seperated by " + "spaces: ");
                Scanner line = new Scanner(s.nextLine());
                line.useDelimiter(" +");
                for (int j = 0; j < col; j++) {
                    grid[i][j] = line.nextInt();
                }
                printGrid(i + 1);
            }
        }

        /**
         * Prints the matrix from row 0 to rowind exclusive
         *
         * @param rowind
         */
        private void printGrid(int rowind) {
            byte widest = 0;
            for (int[] row : grid) {
                for (int col : row) {
                    byte width = (byte) Integer.toString(col).length();
                    if (width > widest) {
                        widest = width;
                    }
                }
            }
            for (int i = 0; i < rowind; i++) {
                System.out.print('|');
                for (int j = 0; j < grid[i].length; j++) {
                    System.out.printf("%" + widest + "d|", grid[i][j]);
                }
                System.out.println();
            }
            System.out.println();
        }

        private Matrix add(Matrix other) {
            return addsub(other, true);
        }

        private Matrix addsub(Matrix other, boolean add) {
            int row = other.grid.length, col = other.grid[0].length;
            Matrix mat = new Matrix(row, col);
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    mat.grid[i][j] =
                            add ? grid[i][j] + other.grid[i][j] : grid[i][j] - other.grid[i][j];
                }
            }
            return mat;
        }

        private Matrix subtract(Matrix other) {
            return addsub(other, false);
        }

        private Matrix multiply(Matrix other) {
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

        private int getDeterminant() {
            return 0;
        }
    }
}