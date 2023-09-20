import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        InputParser input = new InputParser(args);
        HashMap<String, Matrix> matrices = input.getMatrices();
        for (Matrix m: matrices.values()) {
            System.out.println(m);
        }
    }
}