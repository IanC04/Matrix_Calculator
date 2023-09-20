public class Cell {

    Fraction value;

    /**
     * Immutable class so safe from client changes
     */
    private static class Fraction {
        int numerator, denominator;

        private Fraction() {
            numerator = 0;
            denominator = 1;
        }

        private Fraction(String num) {
            if (num.contains("/")) {
                numerator = Integer.parseInt(num.substring(0, num.indexOf('/')));
                denominator = Integer.parseInt(num.substring(num.indexOf('/') + 1));
            }
            if (num.contains(".")) {
                numerator = Integer.parseInt(num.substring(0, num.indexOf('.')));
                num = num.substring(num.indexOf('.'));
                do {
                    denominator *= 10;
                    numerator *= 10;
                    num = num.substring(1);
                } while (num.length() > 0 && num.charAt(0) == '0');
                numerator += denominator * Integer.parseInt(num.substring(denominator));
            }
        }

        private Fraction(int integral) {
            numerator = integral;
            denominator = 1;
        }

        private Fraction(int num, int den) {
            numerator = num;
            denominator = den;
            simplify(numerator, denominator);
            if (denominator < 0) {
                numerator = ~numerator + 1;
                denominator = ~denominator + 1;
            }
            if (denominator < 0) {
                throw new IllegalStateException("Denominator is: " + denominator);
            }
        }

        private void simplify(int a, int b) {
            if (b != 0) {
                simplify(b, a % b);
            } else {
                numerator %= a;
                denominator %= a;
            }
        }

        private Fraction add(Fraction other) {
            int num = numerator * other.denominator + other.numerator * denominator, dem =
                    denominator * other.denominator;
            return new Fraction(num, dem);
        }

        private Fraction subtract(Fraction other) {
            int num = numerator * other.denominator + -other.numerator * denominator, dem =
                    denominator * other.denominator;
            return new Fraction(num, dem);
        }

        private double getValDec() {
            return (double) numerator / denominator;
        }

        private String getValFrac() {
            return toString();
        }

        public boolean equals(Object other) {
            return getValFrac().equals(other.toString());
        }

        public String toString() {
            if (numerator == 0 || denominator == 1) {
                return Integer.toString(numerator);
            }
            return numerator + "/" + denominator;
        }
    }

    public Cell() {
        this(0);
    }

    public Cell(int val) {
        value = new Fraction(val);
    }

    public Cell(int numerator, int denominator) {
        value = new Fraction(numerator, denominator);
    }

    public Cell(Fraction val1, Fraction val2) {
        value = val1.add(val2);
    }

    public Cell(String val) {
        value = new Fraction(val);
    }

    public Cell add(int r, int c, Cell A, Cell B) {
        //return new Cell(r, c, A.value, B.value);
        return null;
    }

    public boolean equals(Object other) {
        return value.equals(other);
    }

    public String toString() {
        return value.getValFrac();
    }
}
