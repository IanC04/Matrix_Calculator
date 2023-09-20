import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputParser {

    // Stores all the matrices needed to compute.
    HashMap<String, Matrix> matrices;

    // Checks if the line is in the form of a matrix.
    final static Pattern FILE_LINE_FORMAT = Pattern.compile("\\[\\w+]\\[\\d+]\\[\\d+]\\[\\[.*\\d+.*]]");

    InputParser(String[] files) throws FileNotFoundException {
        if (files.length < 2) {
            throw new IllegalArgumentException("Must have at least 2 files");
        }
        matrices = new HashMap<>();
        File file;
        file = new File(files[0]);
        createMatrices(file);

        file = new File(files[1]);
        getOperations(file);
    }

    /**
     * Reads the matrices file and creates the matrices.
     * @param file File to read the matrix data from.
     * @throws FileNotFoundException if the file is not found.
     */
    void createMatrices(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            String line = s.nextLine().replaceAll("\\s+", "");
            if (FILE_LINE_FORMAT.matcher(line).matches()) {
                createMatrix(line);
            } else {
                System.err.println("Invalid matrix: " + line + ". Matrix must be in the form: [name] [rows] [cols] " +
                        "[[values...],[values...],[values...]]");
                System.err.println("Exiting...");
                System.exit(1);
            }
        }
    }

    /**
     * Creates a single matrix based on a single line in the file.
     * @param line line of square-bracketed values to assign to a matrix.
     */
    void createMatrix(String line) {
        Scanner lineScanner = new Scanner(line);
        lineScanner.useDelimiter("[\\[\\],]+");
        String name = lineScanner.next();
        int rows = lineScanner.nextInt();
        int columns = lineScanner.nextInt();

        Matrix mat = new Matrix(rows, columns, name);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String cell = lineScanner.next();
                if (cell.contains("/")) {
                    String[] frac = cell.split("/");
                    mat.setCell(i, j, new Cell(Integer.parseInt(frac[0]), Integer.parseInt(frac[1])));
                } else {
                    mat.setCell(i, j, new Cell(Integer.parseInt(cell)));
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

    void getOperations(File file) {

    }
}