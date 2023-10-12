import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        InputParser input = new InputParser(args);
        HashMap<String, Matrix> matrices = input.getMatrices();
        // matrices.values().forEach(System.out::println);
        // Scanner s = new Scanner(System.in);
        // System.out.println("Type y to start.");
        // s.next();
        input.start();
    }
}