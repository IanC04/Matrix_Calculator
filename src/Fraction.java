/**
 * Fraction class that implements immutable fractions
 */
public class Fraction {
    private int numerator, denominator;

    public Fraction() {
        this(0);
    }

    public Fraction(int integral) {
        this(integral, 1);
    }

    public Fraction(int num, int den) {
        numerator = num;
        denominator = den;
        simplify(numerator, denominator);
        if (numerator == 0) {
            denominator = 1;
        } else if (denominator < 0) {
            numerator = ~numerator + 1;
            denominator = ~denominator + 1;
        }
    }

    public Fraction(Fraction other) {
        this.numerator = other.numerator;
        this.denominator = other.denominator;
    }

    private void simplify(int a, int b) {
        if (b != 0) {
            simplify(b, a % b);
            return;
        }
        numerator /= a;
        denominator /= a;
    }

    public Fraction add(Fraction other) {
        int num = numerator * other.denominator + other.numerator * denominator, dem =
                denominator * other.denominator;
        return new Fraction(num, dem);
    }

    public Fraction subtract(Fraction other) {
        int num = numerator * other.denominator - other.numerator * denominator, dem =
                denominator * other.denominator;
        return new Fraction(num, dem);
    }

    public Fraction multiply(Fraction other) {
        int num = numerator * other.numerator, dem = denominator * other.denominator;
        return new Fraction(num, dem);
    }

    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            System.err.printf("%s can't be divided by %s", this, other);
            throw new ArithmeticException("Cannot divide by zero");
        }
        int num = numerator * other.denominator, dem = denominator * other.numerator;
        return new Fraction(num, dem);
    }

    private double getValDecimal() {
        return (double) numerator / denominator;
    }

    private int[] getValFraction() {
        return new int[]{numerator, denominator};
    }

    public boolean equals(Object other) {
        if (other instanceof Fraction) {
            int[] thisFrac = this.getValFraction();
            int[] otherFrac = ((Fraction) other).getValFraction();
            return thisFrac[0] == otherFrac[0] && thisFrac[1] == otherFrac[1];
        }
        return false;
    }

    public String toString() {
        if (numerator == 0 || denominator == 1) {
            return Integer.toString(numerator);
        }
        return String.format("%d/%d\n", numerator, denominator);
    }
}
