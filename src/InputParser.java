import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputParser {

    private class OperationTree {
        private enum ALL_FLAGS {
            DISPLAY(1), PLACEHOLDER1(2), PLACEHOLDER2(4);

            private final int val;

            private ALL_FLAGS(int v) {
                val = v;
            }

            private int getVal() {
                return val;
            }
        }

        private String line;
        private String[] tokens;

        private ALL_FLAGS flags;

        private String ASSIGNED_MATRIX_NAME;
        private String ASSIGNED_SCALAR_NAME;

        private OperationTree(String line) {
            this.line = line;
            this.flags = ALL_FLAGS.DISPLAY;
            parseLine();
        }

        private void parseLine() {
            String[] tokens = line.split(" ");
            this.tokens = tokens;
        }

        private void start() {
            if (tokens[1].equals("=")) {
                ASSIGNED_MATRIX_NAME = tokens[0];
            }

            if (tokens[2].startsWith("RREF(")) {
                matrices.put(ASSIGNED_MATRIX_NAME, MatrixOperations.getRREF(matrices.get(tokens[2])));
            } else if (tokens[2].startsWith("REF(")) {
                matrices.put(ASSIGNED_MATRIX_NAME, MatrixOperations.getREF(matrices.get(tokens[2])));
            } else if (tokens[2].startsWith("DET(")) {
                scalars.put(ASSIGNED_SCALAR_NAME, MatrixOperations.getDeterminant(matrices.get(tokens[2])));
            } else if (tokens[2].startsWith("INV(")) {
                matrices.put(ASSIGNED_MATRIX_NAME, MatrixOperations.getInverse(matrices.get(tokens[2])));
            } else {

                switch (tokens[3]) {
                    case "+":
                        matrices.put(ASSIGNED_MATRIX_NAME, MatrixOperations.add(matrices.get(tokens[2]),
                                matrices.get(tokens[4])));
                        break;
                    case "-":
                        matrices.put(ASSIGNED_MATRIX_NAME, MatrixOperations.subtract(matrices.get(tokens[2]),
                                matrices.get(tokens[4])));
                        break;
                    case "*":
                        matrices.put(ASSIGNED_MATRIX_NAME, MatrixOperations.multiply(matrices.get(tokens[2]),
                                matrices.get(tokens[4])));
                        break;
                    default:
                        System.err.println("Couldn't parse operation: " + line);
                        System.err.println("Exiting...");
                        System.exit(1);
                        break;
                }
            }

            if ((flags.getVal() & ALL_FLAGS.DISPLAY.getVal()) != 0) {
                if (matrices.containsKey(ASSIGNED_MATRIX_NAME)) {
                    System.out.println(matrices.get(ASSIGNED_MATRIX_NAME));
                } else if (scalars.containsKey(ASSIGNED_SCALAR_NAME)) {
                    System.out.println(scalars.get(ASSIGNED_SCALAR_NAME));
                }
            }
        }
    }

    // Stores all the matrices needed to compute.
    private final HashMap<String, Matrix> matrices;

    // Stores all the operations in the order they appear in the dat file.
    private final ArrayList<OperationTree> operations;

    // Stores all the scalars when computed or provided.
    private final HashMap<String, Fraction> scalars;

    // Checks if the matrix in the file is valid.
    private final static Pattern MATRIX_FILE_LINE_FORMAT = Pattern.compile("\\w+\\s+\\d+\\s+\\d+\\s+.*\\d+.*");

    // Checks if the operation in the file is valid.
    private final static Pattern OPERATION_FILE_LINE_FORMAT = Pattern.compile(".*");

    InputParser(String[] files) throws FileNotFoundException {
        if (files.length < 2) {
            throw new IllegalArgumentException("Must have at least 2 files");
        }
        scalars = new HashMap<>();

        matrices = new HashMap<>();
        File file;
        file = new File(files[0]);
        createMatrices(file);

        operations = new ArrayList<>();
        file = new File(files[1]);
        createOperations(file);
    }

    /**
     * Reads the matrices file and creates the matrices.
     *
     * @param file File to read the matrix data from.
     * @throws FileNotFoundException if the file is not found.
     */
    private void createMatrices(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            String line = s.nextLine().replaceAll("\\s+", " ");
            if (MATRIX_FILE_LINE_FORMAT.matcher(line).matches()) {
                createMatrix(line);
            } else {
                System.err.println("Invalid matrix: " + line + ". Matrix must be in the form: name rows cols " +
                        "[[values...],[values...],[values...]]");
                System.err.println("Exiting...");
                System.exit(1);
            }
        }
    }

    /**
     * Creates a single matrix based on a single line in the file.
     *
     * @param line line of square-bracketed values to assign to a matrix.
     */
    private void createMatrix(String line) {
        Scanner lineScanner = new Scanner(line);
        String name = lineScanner.next();
        int rows = lineScanner.nextInt();
        int columns = lineScanner.nextInt();

        Matrix mat = new Matrix(rows, columns, name);

        lineScanner.useDelimiter("[\\[\\], ]+");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String cell = lineScanner.next();
                if (cell.contains("/")) {
                    String[] frac = cell.split("/");
                    mat.setMatrixValue(i, j, new Fraction(Integer.parseInt(frac[0]), Integer.parseInt(frac[1])));
                } else {
                    mat.setMatrixValue(i, j, new Fraction(Integer.parseInt(cell)));
                }
            }
        }
        Matrix old = this.matrices.put(name, mat);
        if (old != null) {
            throw new IllegalStateException("Duplicate matrix name: " + name);
        }
    }

    Matrix getMatrix(String name) {
        return matrices.get(name);
    }

    HashMap<String, Matrix> getMatrices() {
        return matrices;
    }

    private void createOperations(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            String line = s.nextLine().replaceAll("[\\s\\[\\]]+", " ");
            if (OPERATION_FILE_LINE_FORMAT.matcher(line).matches()) {
                createOperation(line);
            } else {
                System.err.println("Invalid operation: " + line + ". Operation must be in the form: [RESULT] [=] " +
                        "[OP1] " +
                        "[OP] [OP2]... or [RESULT] [=] FUNC([OP1])...");
                System.err.println("Valid operations are: +, -, *, RREF(), REF(), DET(), INV()");
                System.err.println("Exiting...");
                System.exit(1);
            }
        }
    }

    private void createOperation(String line) {
        OperationTree operation = new OperationTree(line);
        this.operations.add(operation);
    }

    public String getOperations() {
        return operations.toString();
    }

    public void start() {
        for (OperationTree op : operations) {
            op.start();
        }
    }
}