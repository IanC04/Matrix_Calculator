public enum Flags {
    DISPLAY(1), READ(2), WRITE(4);

    private final int val;

    Flags(int v) {
        val = v;
    }

    int getVal() {
        return val;
    }
}
