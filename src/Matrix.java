import java.util.Scanner;

public class Matrix {


    final Fraction[][] grid;

    int rows, columns;
    String name;

    public Matrix(int rows, int cols) {
        this(rows, cols, "");
    }

    public Matrix(int rows, int cols, String name) {
        this.rows = rows;
        this.columns = cols;
        this.name = name;
        grid = new Fraction[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Fraction();
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
        this.columns = other.columns;
        this.name = other.name;
        this.grid = new Fraction[this.rows][this.columns];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.grid[i][j] = new Fraction(other.grid[i][j]);
            }
        }
    }

    public void setMatrixValue(int row, int col, Fraction value) {
        if (row >= 0 && col >= 0 && row < rows && col < columns) {
            grid[row][col] = new Fraction(value);
        } else {
            throw new IllegalArgumentException("Row or column out of bounds");
        }
    }

    public Fraction getMatrixValue(int row, int col) {
        return grid[row][col];
    }

    public String printMatrix() {
        return this.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        byte widest = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                byte width = (byte) grid[i][j].toString().length();
                if (width > widest) {
                    widest = width;
                }
            }
        }
        widest++;
        for (int i = 0; i < rows; i++) {
            sb.append('|');
            for (int j = 0; j < columns; j++) {
                sb.append(String.format("%" + widest + "s|", grid[i][j]));
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}