import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputParser {

    // Stores all the matrices needed to compute.
    private final HashMap<String, Matrix> matrices;

    // Stores all the operations in the order they appear in the dat file.
    private ArrayList<Command> commands;

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

        commands = new ArrayList<>();
        file = new File(files[1]);
        createCommands(file);
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

    /**
     * Gets a matrix by name.
     * @param name
     * @return
     */
    Matrix getMatrix(String name) {
        return matrices.getOrDefault(name, null);
    }

    /**
     * Gets all the matrices.
     * @return
     */
    HashMap<String, Matrix> getMatrices() {
        return matrices;
    }

    private void createCommands(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            String line = s.nextLine().replaceAll("[\\s\\[\\]]+", " ");
            if (OPERATION_FILE_LINE_FORMAT.matcher(line).matches()) {
                createCommand(line);
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

    private void createCommand(String line) {
        Command command = new Command(line, matrices, scalars);
        this.commands.add(command);
    }

    public String getCommands() {
        return commands.toString();
    }

    public String getCommand(int index) {
        return commands.get(index).toString();
    }

    public void start() {
        for (Command op : commands) {
            op.start();
        }
    }
}