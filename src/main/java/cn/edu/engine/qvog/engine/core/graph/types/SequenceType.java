package cn.edu.engine.qvog.engine.core.graph.types;

import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Sequence;
import cn.edu.engine.qvog.engine.helper.NamingHelper;

/**
 * Represents a sequence type. For example, array, list, etc.
 * <p>
 * For now, only support sequence of a single type.
 */
public final class SequenceType extends Type {
    private final Type elementType;

    /**
     * The size of the sequence. If the size is unknown or dynamic, the value is -1.
     */
    private final int size;

    public SequenceType(String name) {
        this(name, UnknownType.DEFAULT);
    }

    public SequenceType(String name, Type elementType) {
        this(name, elementType, -1);
    }

    public SequenceType(String name, Type elementType, int size) {
        super(name);
        this.elementType = elementType;
        this.size = size;
    }

    public SequenceType(Type elementType) {
        this(elementType, -1);
    }

    public SequenceType(Type elementType, int size) {
        this(NamingHelper.toReservedName(Sequence.class.getSimpleName()), elementType, size);
    }

    public Type getElementType() {
        return elementType;
    }

    /**
     * WARNING: May return -1 if the size is unknown or dynamic.
     *
     * @return The size of the sequence.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return Whether the sequence has a fixed size.
     */
    public boolean isFixedSize() {
        return size >= 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SequenceType other) {
            return elementType.equals(other.elementType);
        }
        return false;
    }
}
