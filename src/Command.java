import java.util.EnumSet;
import java.util.HashMap;

public class Command {

    private String ASSIGNED_MATRIX_NAME;
    private String ASSIGNED_SCALAR_NAME;

    private final String line;
    private final String[] tokens;

    private final EnumSet<Flags> flags;

    private final Operation tree;

    private final HashMap<String, Matrix> matrices;

    private final HashMap<String, Fraction> scalars;

    public Command(String line, HashMap<String, Matrix> matrices, HashMap<String, Fraction> scalars) {
        this.ASSIGNED_MATRIX_NAME = null;
        this.ASSIGNED_SCALAR_NAME = null;
        this.line = line;
        this.tokens = line.split(" ");
        flags = EnumSet.noneOf(Flags.class);
        if (line.contains("DISPLAY")) {
            flags.add(Flags.DISPLAY);
        }
        if (line.contains("WRITE")) {
            flags.add(Flags.WRITE);
        }
        if (line.contains("READ")) {
            flags.add(Flags.READ);
        }
        this.matrices = matrices;
        this.scalars = scalars;
        int startIndex = 0;
        for (int i = 0; i < this.tokens.length; i++) {
            if (this.tokens[i].equals("=")) {
                ASSIGNED_MATRIX_NAME = this.tokens[i - 1];
                startIndex = i + 1;
                break;
            }
        }

        // For Debugging
        this.flags.add(Flags.DISPLAY);

        this.tree = new Operation(startIndex, tokens.length);
    }

    public Command(String line) {
        this(line, null, null);
    }

    private class Operation {

        private Matrix result;

        private String resultName;

        private String operator;

        private int startIndex, endIndex;

        private Operation left, right;

        /**
         * Parses the tokens from start inclusive to end exclusive.
         *
         * @param start
         * @param end
         */
        private Operation(int start, int end) {
            if (start >= end) {
                throw new IllegalArgumentException("Start must be less than end");
            }
            if (start == end - 1) {
                result = matrices.get(tokens[start]);
                resultName = tokens[start];
                return;
            }
            this.resultName = null;
            this.startIndex = start;
            this.endIndex = end;
            int split = getOperator();
            operator = tokens[split];
            left = new Operation(start, split);
            right = new Operation(split + 1, end);
        }

        private int getOperator() {
            int index = -1;
            byte priority = Byte.MAX_VALUE; // 0 is highest priority
            for (int i = startIndex; i < endIndex; i++) {
                switch (tokens[i]) {
                    case "*":
                        if (priority > 2) {
                            index = i;
                            priority = 2;
                        }
                        break;
                    case "+":
                    case "-":
                        if (priority > 3) {
                            index = i;
                            priority = 3;
                        }
                        break;
                    default:
                        if (priority > 4) {
                            index = i;
                            priority = 4;
                        }
                }
            }
            return index;
        }

        private Matrix getResult() {
            return result;
        }

        private void start() {
            if (left == null || right == null) {
                throw new IllegalArgumentException("Must have at least one operation.");
            }
            calculate();

            if (ASSIGNED_MATRIX_NAME != null) {
                matrices.put(ASSIGNED_MATRIX_NAME, result);
            } else if (ASSIGNED_SCALAR_NAME != null) {
                scalars.put(ASSIGNED_SCALAR_NAME, result.getMatrixValue(0, 0));
            }

            if (flags.contains(Flags.DISPLAY)) {
                if (matrices.containsKey(ASSIGNED_MATRIX_NAME)) {
                    System.out.println(matrices.get(ASSIGNED_MATRIX_NAME));
                } else if (scalars.containsKey(ASSIGNED_SCALAR_NAME)) {
                    System.out.println(scalars.get(ASSIGNED_SCALAR_NAME));
                }
            }
        }

        private Matrix calculate() {
            if (this.result == null) {

                if(resultName != null) {
                    this.result = matrices.get(resultName);
                    if(this.result == null) {
                        throw new IllegalStateException("Matrix " + resultName + " not found.");
                    }
                    return this.result;
                }


                Matrix leftResult = left.calculate();
                Matrix rightResult = right.calculate();
                switch (operator) {
                    case "+":
                        this.result = MatrixOperations.add(leftResult, rightResult);
                        break;
                    case "-":
                        this.result = MatrixOperations.subtract(leftResult, rightResult);
                        break;
                    case "*":
                        this.result = MatrixOperations.multiply(leftResult, rightResult);
                        break;
                    case "=":
                        this.result = rightResult;
                        matrices.put(ASSIGNED_MATRIX_NAME, this.result);
                        break;
                    default:
                        System.err.println("Couldn't parse operation: " + line);
                        System.err.println("Exiting...");
                        System.exit(1);
                        break;
                }
            }

            return this.result;
        }
    }

    public void start() {
        tree.start();
    }
}
