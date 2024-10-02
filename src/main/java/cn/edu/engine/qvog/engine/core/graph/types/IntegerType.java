package cn.edu.engine.qvog.engine.core.graph.types;

/**
 * Represents an integer type.
 * <p>
 * The integer type has two main attributes, bit width and signedness.
 */
public final class IntegerType extends NumericType {
    public static final IntegerType INT8 = new IntegerType("int8", 8);
    public static final IntegerType INT16 = new IntegerType("int16", 16);
    public static final IntegerType INT32 = new IntegerType("int32", 32);
    public static final IntegerType INT64 = new IntegerType("int64", 64);
    public static final IntegerType INT128 = new IntegerType("int128", 128);
    public static final IntegerType UINT8 = new IntegerType("uint8", 8, false);
    public static final IntegerType UINT16 = new IntegerType("uint16", 16, false);
    public static final IntegerType UINT32 = new IntegerType("uint32", 32, false);
    public static final IntegerType UINT64 = new IntegerType("uint64", 64, false);
    public static final IntegerType UINT128 = new IntegerType("uint128", 128, false);
    public static final IntegerType UNSPECIFIED = new IntegerType("int", -1);

    public IntegerType(String name, Origins origin, int bits, boolean signed) {
        super(name, origin, bits, signed);
    }

    public IntegerType(String name) {
        this(name, -1);
    }

    public IntegerType(String name, int bits) {
        this(name, bits, true);
    }

    public IntegerType(String name, int bits, boolean signed) {
        super(name, bits, signed);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntegerType other) {
            return getName().equals(other.getName()) &&
                    getBits() == other.getBits() &&
                    isSigned() == other.isSigned();
        }
        return false;
    }
}
