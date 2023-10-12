import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;

public class Command {

    private String ASSIGNED_MATRIX_NAME;

    // Not implemented yet, currently scalars are 1x1 matrices
    // private String ASSIGNED_SCALAR_NAME;

    private final String line;
    private final String[] tokens;

    private final EnumSet<Flags> flags;

    private File inputOrOutputFile;

    private final Operation tree;

    private final HashMap<String, Matrix> matrices;

    private final HashMap<String, Fraction> scalars;

    public Command(String line, HashMap<String, Matrix> matrices, HashMap<String, Fraction> scalars) {
        this.ASSIGNED_MATRIX_NAME = null;
        // this.ASSIGNED_SCALAR_NAME = null;
        this.line = line;
        this.tokens = line.split(" ");
        flags = EnumSet.noneOf(Flags.class);
        this.matrices = matrices;
        this.scalars = scalars;
        boolean flagLine = getFlags();
        int startIndex = flagLine ? tokens.length - 1 : 0;
        if (!flagLine) {
            if (this.tokens.length > 2 && this.tokens[1].equals("=")) {
                startIndex = 2;
                /*if (this.tokens.length > 3 && this.tokens[2].equals("DET")) {
                    ASSIGNED_SCALAR_NAME = this.tokens[0];
                } else {
                }*/
                ASSIGNED_MATRIX_NAME = this.tokens[0];
            }
        }

        // For Debugging
        // this.flags.add(Flags.DISPLAY);

        this.tree = new Operation(startIndex, tokens.length);
        /*if (ASSIGNED_SCALAR_NAME != null) {
            this.tree.resultIsMatrix = false;
        }*/
    }

    /**
     * Line of file MUST be pure flags then operands.
     *
     * @return if there are flags
     */
    private boolean getFlags() {
        if (tokens.length < 2) {
            return false;
        }
        switch (tokens[0]) {
            case "DISPLAY":
                this.flags.add(Flags.DISPLAY);
                break;
            case "READ":
                this.flags.add(Flags.READ);
                inputOrOutputFile = new File(tokens[1]);
                break;
            case "WRITE":
                this.flags.add(Flags.WRITE);
                inputOrOutputFile = new File(tokens[1]);
                break;
        }

        if (!flags.isEmpty()) {
            this.ASSIGNED_MATRIX_NAME = tokens[tokens.length - 1];
        }
        return !flags.isEmpty();
    }

    public void start() {
        if (tree != null) {
            tree.start();
        }
    }

    private class Operation {


        // private Fraction resultScalar;

        private Matrix resultMatrix;

        private final String resultName;

        private String operator;

        private int startIndex, endIndex;

        private Operation left, right;

        /**
         * Parses the tokens from into subtrees with operators as middle nodes and operands as leaves.
         *
         * @param start inclusive
         * @param end   exclusive
         */
        private Operation(int start, int end) {
            if (start >= end) {
                throw new IllegalArgumentException("Start must be less than end");
            }
            if (start == end - 1) {
                // result = matrices.get(tokens[start]);
                resultName = tokens[start];
                return;
            }
            this.resultName = null;
            this.startIndex = start;
            this.endIndex = end;
            int[] indexAndSpecial = getOperator();
            int split = indexAndSpecial[0];
            if (!(indexAndSpecial[1] == 1)) {
                left = new Operation(start, split);
            }
            operator = tokens[split];
            right = new Operation(split + 1, end);
        }

        private int[] getOperator() {
            // [0] = index | [1] = special function
            int[] result = new int[2];
            result[0] = -1;

            byte priority = Byte.MAX_VALUE; // 0 is highest priority
            for (int i = startIndex; i < endIndex; i++) {
                switch (tokens[i]) {
                    case "DET":
                    case "REF":
                    case "RREF":
                    case "INV":
                        if (priority > 16) {
                            result[0] = i;
                            result[1] = 1;
                            priority = 16;
                            // Immediately return since left-to-right and highest priority
                            return result;
                        }
                        break;
                    case "*":
                        if (priority > 32) {
                            result[0] = i;
                            priority = 32;
                        }
                        break;
                    case "+":
                    case "-":
                        if (priority > 64) {
                            result[0] = i;
                            priority = 64;
                        }
                        break;
                    default:
                        if (priority == Byte.MAX_VALUE) {
                            result[0] = i;
                        }
                }
            }
            return result;
        }

        private Matrix getResult() {
            return resultMatrix;
        }

        private void start() {
            calculate();

            if (ASSIGNED_MATRIX_NAME != null) {
                matrices.put(ASSIGNED_MATRIX_NAME, resultMatrix);
                resultMatrix.setName(ASSIGNED_MATRIX_NAME);
            } /*else if (ASSIGNED_SCALAR_NAME != null) {
                scalars.put(ASSIGNED_SCALAR_NAME, resultMatrix.getMatrixValue(0, 0));
            }*/

            if (flags.contains(Flags.DISPLAY)) {
                if (ASSIGNED_MATRIX_NAME != null) {
                    System.out.println(matrices.get(ASSIGNED_MATRIX_NAME));
                }
                /*if (ASSIGNED_SCALAR_NAME != null) {
                    System.out.println(scalars.get(ASSIGNED_SCALAR_NAME));
                }*/
            }
        }

        private Matrix calculate() {
            if (resultName != null) {
                // this.resultMatrix = scalars.get(resultName).toMatrix();
                this.resultMatrix = matrices.get(resultName);
                if (this.resultMatrix == null) {
                    throw new IllegalStateException("Matrix " + resultName + " not found.");
                }
                return this.resultMatrix;

            }
            boolean isMatrix = true;

            Matrix leftResult = null, rightResult = null;
            if (left != null) {
                leftResult = left.calculate();
            }
            if (right != null) {
                rightResult = right.calculate();
            }
            switch (operator) {
                case "+":
                    this.resultMatrix = MatrixOperations.add(leftResult, rightResult);
                    break;
                case "-":
                    this.resultMatrix = MatrixOperations.subtract(leftResult, rightResult);
                    break;
                case "*":
                    this.resultMatrix = MatrixOperations.multiply(leftResult, rightResult);
                    break;
                case "=":
                    this.resultMatrix = rightResult;
                    matrices.put(ASSIGNED_MATRIX_NAME, this.resultMatrix);
                    break;
                case "REF":
                    this.resultMatrix = MatrixOperations.getREF(rightResult);
                    matrices.put(ASSIGNED_MATRIX_NAME, this.resultMatrix);
                    break;
                case "RREF":
                    this.resultMatrix = MatrixOperations.getRREF(rightResult);
                    matrices.put(ASSIGNED_MATRIX_NAME, this.resultMatrix);
                    break;
                case "INV":
                    this.resultMatrix = MatrixOperations.getInverse(rightResult);
                    matrices.put(ASSIGNED_MATRIX_NAME, this.resultMatrix);
                    break;
                case "DET":
                    isMatrix = false;
                    // this.resultScalar = MatrixOperations.getDeterminant(rightResult);
                    // scalars.put(ASSIGNED_SCALAR_NAME, this.resultScalar);

                    // Fake scalar
                    this.resultMatrix = MatrixOperations.getDeterminant(rightResult).toMatrix();
                    matrices.put(ASSIGNED_MATRIX_NAME, this.resultMatrix);
                    break;
                default:
                    System.err.println("Couldn't parse operation: " + line);
                    System.err.println("Exiting...");
                    System.exit(1);
                    break;
            }

            return this.resultMatrix;
        }

    }
}
