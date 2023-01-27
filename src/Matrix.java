import java.util.Scanner;

public class Matrix {

    private static class Fraction {
        int numerator, denominator;

        private Fraction(String num) {
            if (num.contains("/")) {
                numerator = Integer.parseInt(num.substring(0, num.indexOf('/')));
                denominator = Integer.parseInt(num.substring(num.indexOf('/') + 1));
            }
            if (num.contains(".")) {
                numerator = Integer.parseInt(num.substring(0, num.indexOf('.')));
                num = num.substring(num.indexOf('.'));
                do {
                    denominator *= 10;
                    numerator *= 10;
                    num = num.substring(1);
                } while (num.length() > 0 && num.charAt(0) == '0');
                numerator += denominator * Integer.parseInt(num.substring(denominator));
            }
        }

        private Fraction(int integral) {
            numerator = integral;
            denominator = 1;
        }

        private Fraction(int num, int den) {
            numerator = num;
            denominator = den;
            simplify(numerator, denominator);
            if (denominator < 0) {
                numerator = ~numerator + 1;
                denominator = ~denominator + 1;
            }
            if (denominator < 0) {
                throw new IllegalStateException("Denominator is: " + denominator);
            }
        }

        private void simplify(int a, int b) {
            if (b != 0) {
                simplify(b, a % b);
            } else {
                numerator %= a;
                denominator %= a;
            }
        }

        private Fraction add(Fraction other) {
            int num = numerator * other.denominator + other.numerator * denominator, dem =
                    denominator * other.denominator;
            return new Fraction(num, dem);
        }

        private Fraction subtract(Fraction other) {
            int num = numerator * other.denominator + -other.numerator * denominator, dem =
                    denominator * other.denominator;
            return new Fraction(num, dem);
        }

        private double getValDec() {
            return (double) numerator / denominator;
        }

        private String getValFrac() {
            return numerator + "/" + denominator;
        }

        public String toString() {
            if (numerator == 0 || denominator == 1) {
                return Integer.toString(numerator);
            }
            return numerator + "/" + denominator;
        }
    }

    private final Fraction[][] grid;

    public Matrix(int row, int col) {
        grid = new Fraction[row][col];
    }

    /**
     * Copies other matrix to this matrix
     *
     * @param other
     */
    public Matrix(Matrix other) {
        int row = other.grid.length, col = other.grid[0].length;
        grid = new Fraction[row][col];
        for (int i = 0; i < row; i++) {
            System.arraycopy(other.grid[i], 0, grid[i], 0, col);
        }
    }

    public Matrix(Scanner s) {
        System.out.print("How many rows: ");
        int row = s.nextInt();
        System.out.print("How many columns: ");
        int col = s.nextInt();
        s.nextLine();
        grid = new Fraction[row][col];
        for (int i = 0; i < row; i++) {
            System.out.println(
                    "Enter numbers of " + Suffix.values()[i] + " row seperated by " + "spaces: ");
            Scanner line = new Scanner(s.nextLine());
            line.useDelimiter(" +");
            for (int j = 0; j < col; j++) {
                grid[i][j] = line.next();
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
                byte width = (byte) Integer.toString(grid[i][j]).length();
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