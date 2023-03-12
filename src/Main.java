import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // Scanner scan = new Scanner(System.in);
        // Scanner scan = new Scanner(new File("input.dat"));
        // Matrix a = new Matrix(scan);
        // Matrix b = new Matrix(scan);
        InputParser input = new InputParser(args);
        input.getMatrices();
        input.runOperations();
    }
}