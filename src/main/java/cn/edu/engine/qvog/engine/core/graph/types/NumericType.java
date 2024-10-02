package cn.edu.engine.qvog.engine.core.graph.types;

public abstract class NumericType extends Type {
    /**
     * The number of bits of the integer type. -1 means the number
     * of bits is unspecified.
     */
    private final int bits;
    private final boolean signed;

    public NumericType(String name, Origins origin, int bits, boolean signed) {
        super(name, origin);
        this.bits = bits;
        this.signed = signed;
    }

    public NumericType(String name, int bits, boolean signed) {
        super(name);
        this.bits = bits;
        this.signed = signed;
    }

    public int getBits() {
        return bits;
    }

    public boolean isSigned() {
        return signed;
    }

}
