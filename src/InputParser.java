import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputParser {

    HashMap<File, HashMap<String, Matrix>> matrices;
    final static Pattern FILE_LINE_FORMAT = Pattern.compile("\\[\\w+]\\[\\d+]\\[\\d+]\\[\\[.*\\d+.*]]");

    InputParser(String[] files) throws FileNotFoundException {
        matrices = new HashMap<>();
        for (String name : files) {
            File file = new File(name);
            matrices.put(file, new HashMap<>());
            HashMap<String, Matrix> current = matrices.get(file);
            createMatrices(current, file);
        }
    }

    void createMatrices(HashMap<String, Matrix> map, File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            String line = s.nextLine().replaceAll("\\s+", "");
            if (FILE_LINE_FORMAT.matcher(line).matches()) {
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
                map.put(name, mat);
            }
            else {
                System.err.println("Invalid matrix: " + line + ". Matrix must be in the form: [name] [rows] [cols] " +
                        "[[values...],[values...],[values...]]");
                System.err.println("Exiting...");
                System.exit(1);
            }
        }
    }

    void getMatrices() {

    }

    void runOperations() {

    }
}