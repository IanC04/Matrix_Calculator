public enum Flags {
    DISPLAY(1), WRITE(2), READ(4);

    private final int val;

    Flags(int v) {
        val = v;
    }

    int getVal() {
        return val;
    }
}
