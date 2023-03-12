import java.util.Arrays;
import java.util.Scanner;

public class Matrix {

    int matrixType;

    private enum MatrixTypes {
        Lower_Triangular(2), Upper_Triangular(4), Diagonal(8), Identity(16), Symmetric(32), Zero(
                64), Square(128);

        final int flag;

        MatrixTypes(int val) {
            flag = val;
        }
    }


    final Cell[][] grid;

    int numberOfRows, numberOfColumns;

    public Matrix(int rows, int cols, String[] values) {
        numberOfRows = rows;
        numberOfColumns = cols;
        grid = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell(values[(i * cols) + j]);
            }
        }
        getTypes();
    }

    private void getTypes() {
        matrixType = 0;
        MatrixTypes[] typeOfMatrix = MatrixTypes.values();
        if (MatrixTypes. && numberOfRows != numberOfColumns) {
            isSquare = false;
        }
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (i == j) {
                    if (isIdentity && !getCell(i, j).equals(1)) {
                        isIdentity = false;
                    }
                }
                if (i != j) {
                    if ((isIdentity || isDiagonal) && !getCell(i, j).equals(0)){
                        isIdentity = false;
                        isDiagonal = false;
                    }
                    if (isSymmetric && !getCell(i, j).equals(getCell(j, i))) {
                        isSymmetric = false;
                    }
                }
                if (i >= j) {
                    if (isUpper && !getCell(i, j).equals(0)) {
                        isUpper = false;
                    }
                }
                if (i < j) {
                    if (isLower && !getCell(i, j).equals(0)) {
                        isLower = false;
                    }
                }
                if (isZero && !getCell(i, j).equals(0)) {
                    isZero = false;
                }
            }
        }
    }

    /**
     * Copies other matrix to this matrix
     *
     * @param other
     */
    public Matrix(Matrix other) {
        numberOfRows = other.numberOfRows;
        numberOfColumns = other.numberOfColumns;
        grid = new Cell[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; i++) {
            System.arraycopy(other.grid[i], 0, grid[i], 0, numberOfColumns);
        }
    }

    public Matrix(Scanner s) {
        System.out.print("How many rows: ");
        numberOfRows = s.nextInt();
        System.out.print("How many columns: ");
        numberOfColumns = s.nextInt();
        s.nextLine();
        grid = new Cell[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; i++) {
            System.out.println("Enter numbers of " + i + " row seperated by " + "spaces: ");
            Scanner line = new Scanner(s.nextLine());
            line.useDelimiter(" +");
            for (int j = 0; j < numberOfColumns; j++) {
                grid[i][j] = new Cell(line.next());
            }
            printMatrix(i + 1);
        }
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    /**
     * Prints the matrix from row 0 to rowind exclusive
     *
     * @param end
     */
    public void printMatrix(int end) {
        byte widest = 0;
        for (int i = 0; i < end; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                byte width = (byte) grid[i][j].toString().length();
                if (width > widest) {
                    widest = width;
                }
            }
        }
        widest++;
        for (int i = 0; i < end; i++) {
            System.out.print('|');
            for (int j = 0; j < grid[i].length; j++) {
                System.out.printf("%" + widest + "d|", grid[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printMatrix() {
        printMatrix(grid.length);
    }


}