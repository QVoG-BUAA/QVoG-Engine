package cn.edu.engine.qvog.engine.core.graph.types;

/**
 * Represents a boolean type.
 */
public final class BoolType extends Type {
    public static final BoolType DEFAULT = new BoolType("bool");

    public BoolType(String name, Origins origin) {
        super(name, origin);
    }

    public BoolType(String name) {
        super(name);
    }

    public BoolType() {
        super("bool");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BoolType;
    }
}
