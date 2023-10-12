public class Matrix {


    final Fraction[][] grid;

    private final int rows;
    private final int columns;
    private String name;

    /**
     * For transitions between scalars and matrices
     * @param val
     */
    public Matrix(Fraction val) {
        this(1, 1);
        setMatrixValue(0, 0, val);
    }

    public Matrix(int rows, int cols) {
        this(rows, cols, "");
    }

    public Matrix(int rows, int cols, String name) {
        this.rows = rows;
        this.columns = cols;
        this.name = name;
        this.grid = new Fraction[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Fraction();
            }
        }
    }


    /**
     * Copies other matrix to this matrix
     * Ensures name is null
     *
     * @param other other matrix to copy from
     */
    public Matrix(Matrix other) {
        this(other, null);
    }

    /**
     * Copies other matrix to this matrix
     *
     * @param other other matrix to copy from
     */
    private Matrix(Matrix other, String name) {
        this.rows = other.rows;
        this.columns = other.columns;
        this.name = name;
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

    public void setName(String name) {
        if(this.name == null || this.name.isBlank()) {
            this.name = name;
        } else if(!this.name.equals(name)) {
            throw new IllegalArgumentException("Matrix name already set to " + this.name);
        }
    }

    public Fraction getMatrixValue(int row, int col) {
        return grid[row][col];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append('\n');
        byte widest = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                byte width = (byte) grid[i][j].toString().length();
                if (width > widest) {
                    widest = width;
                }
            }
        }

        // Extra space inside each cell
        ++widest;

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