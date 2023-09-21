import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        InputParser input = new InputParser(args);
        HashMap<String, Matrix> matrices = input.getMatrices();
        matrices.values().forEach(System.out::println);
        input.start();
    }
}