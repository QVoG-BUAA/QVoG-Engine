package cn.edu.engine.qvog.engine.core.graph.types;

/**
 * Represents a floating point type.
 */
public class FloatType extends NumericType {
    public static final FloatType FLOAT = new FloatType("float", 32);
    public static final FloatType DOUBLE = new FloatType("double", 64);
    public static final FloatType UNSPECIFIED = new FloatType("float", -1);

    public FloatType(String name, Origins origin, int bits) {
        super(name, origin, bits, true);
    }

    public FloatType(String name, int bits) {
        super(name, bits, true);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FloatType other) {
            return getName().equals(other.getName()) && getBits() == other.getBits();
        }
        return false;
    }
}
