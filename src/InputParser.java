import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputParser {

    HashMap<File, HashMap<Character, Matrix>> matrices;
    final static Pattern FILE_LINE_FORMAT = Pattern.compile("\\[\\w+]\\[\\d+]\\[\\d+]\\[\\[.*\\d+.*]]");

    InputParser(String[] files) throws FileNotFoundException {
        matrices = new HashMap<>();
        for (String name : files) {
            File file = new File(name);
            matrices.put(file, new HashMap<>());
            HashMap<Character, Matrix> current = matrices.get(file);
            createMatrices(current, file);
        }
    }

    void createMatrices(HashMap<Character, Matrix> map, File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            String line = s.nextLine().replaceAll("\\s+", "");
            if (FILE_LINE_FORMAT.matcher(line).matches()) {
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter("[\\[\\],]");
                String name = lineScanner.next();
                lineScanner.next();
                int rows = lineScanner.nextInt();
                lineScanner.next();
                int columns = lineScanner.nextInt();
                lineScanner.next();
                Matrix mat = new Matrix(rows, columns, name);
                lineScanner.next();
                for (int i = 0; i < rows; i++) {
                    lineScanner.next();
                    for (int j = 0; j < columns; j++) {
                        String cell = lineScanner.next();
                        if (cell.contains("/")) {
                            String[] frac = cell.split("/");
                            mat.setCell(i, j, new Cell(Integer.parseInt(frac[0]), Integer.parseInt(frac[1])));
                        } else
                        mat.setCell(i, j, new Cell(Integer.parseInt(cell)));
                    }
                    lineScanner.next();
                }
                lineScanner.next();
            }
            else {
                System.out.println("Invalid matrix: " + line + ". Matrix must be in the form: [name] [rows] [cols] " +
                        "[[values...],[values...],[values...]]");
            }
        }
    }

    void getMatrices() {

    }

    void runOperations() {

    }
}