/**
 * Fraction class that implements immutable fractions.
 */
public class Fraction {
    private long numerator, denominator;

    public Fraction() {
        this(0);
    }

    public Fraction(long integral) {
        this(integral, 1);
    }

    public Fraction(long num, long den) {
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

    private void simplify(long a, long b) {
        if (b != 0) {
            simplify(b, a % b);
            return;
        }
        numerator /= a;
        denominator /= a;
    }

    public Fraction add(Fraction other) {
        long num = numerator * other.denominator + other.numerator * denominator, dem =
                denominator * other.denominator;
        return new Fraction(num, dem);
    }

    public Fraction subtract(Fraction other) {
        long num = numerator * other.denominator - other.numerator * denominator, dem =
                denominator * other.denominator;
        return new Fraction(num, dem);
    }

    public Fraction multiply(Fraction other) {
        long num = numerator * other.numerator, dem = denominator * other.denominator;
        return new Fraction(num, dem);
    }

    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            System.err.printf("%s can't be divided by %s", this, other);
            throw new ArithmeticException("Cannot divide by zero");
        }
        long num = numerator * other.denominator, dem = denominator * other.numerator;
        return new Fraction(num, dem);
    }

    public void invertSign() {
        numerator = numerator * -1;
    }

    private double getValDecimal() {
        return (double) numerator / denominator;
    }

    private long[] getValFraction() {
        return new long[]{numerator, denominator};
    }

    public Matrix toMatrix() {
        return new Matrix(this);
    }

    public boolean equals(Object other) {
        if (other instanceof Fraction) {
            long[] thisFrac = this.getValFraction();
            long[] otherFrac = ((Fraction) other).getValFraction();
            return thisFrac[0] == otherFrac[0] && thisFrac[1] == otherFrac[1];
        }
        if (other instanceof Number) {
            return this.getValDecimal() == ((Number) other).doubleValue();
        }
        return false;
    }

    public String toString() {
        if (numerator == 0 || denominator == 1) {
            return Long.toString(numerator);
        }
        return String.format("%d/%d", numerator, denominator);
    }
}
