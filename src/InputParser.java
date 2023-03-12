import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InputParser {
    File input = new File("input.txt");

    HashMap<String, ArrayList<Matrix>> matrices;

    InputParser(String[] files) {
        for (String file : files) {
            matrices.put(file, new ArrayList<>());
        }
    }

    void getMatrices() {

    }

    void runOperations() {

    }
}