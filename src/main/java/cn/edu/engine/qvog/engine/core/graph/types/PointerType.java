package cn.edu.engine.qvog.engine.core.graph.types;

/**
 * Represents a pointer type. For example, {@code int*}.
 * <p>
 * A pointer type is a composite type, thus it doesn't have a name.
 * <p>
 * FIXME: Can PointerType represent a reference type?
 */
public final class PointerType extends Type {
    private final Type elementType;

    public PointerType(String name) {
        super(name);
        this.elementType = null;
    }

    public PointerType(Type type) {
        super(type.getName() + '*');
        this.elementType = type;
    }

    public Type getElementType() {
        return elementType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointerType other) {
            return elementType.equals(other.elementType);
        }
        return false;
    }
}
