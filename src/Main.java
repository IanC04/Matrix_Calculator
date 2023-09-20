import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        InputParser input = new InputParser(args);
        input.getMatrices();
        input.runOperations();
    }
}