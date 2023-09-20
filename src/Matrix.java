import java.util.Scanner;

public class Matrix {


    final Cell[][] grid;

    int rows, columns;
    String name;

    public Matrix(int rows, int cols, String name) {
        this.rows = rows;
        this.columns = cols;
        this.name = name;
        grid = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell();
            }
        }
    }


    /**
     * Copies other matrix to this matrix
     *
     * @param other
     */
    public Matrix(Matrix other) {
        this.rows = other.rows;
        columns = other.columns;
        grid = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(other.grid[i], 0, grid[i], 0, columns);
        }
    }

    public Matrix(Scanner s) {
        System.out.print("How many rows: ");
        this.rows = s.nextInt();
        System.out.print("How many columns: ");
        columns = s.nextInt();
        s.nextLine();
        grid = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            System.out.println("Enter numbers of " + i + " row seperated by " + "spaces: ");
            Scanner line = new Scanner(s.nextLine());
            line.useDelimiter(" +");
            for (int j = 0; j < columns; j++) {
                grid[i][j] = new Cell(line.next());
            }
            printMatrix(i + 1);
        }
    }

    public boolean setCell(int row, int col, Cell cell) {
        if (row >= 0 && col >= 0 && row < rows && col < columns) {
            grid[row][col] = cell;
            return true;
        }
        return false;
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