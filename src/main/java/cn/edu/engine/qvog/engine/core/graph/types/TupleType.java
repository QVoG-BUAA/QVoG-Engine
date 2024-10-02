package cn.edu.engine.qvog.engine.core.graph.types;

import cn.edu.engine.qvog.engine.helper.NamingHelper;

import java.util.List;

/**
 * Unlike sequence, the length of tuple is fixed, and the type of
 * each element can be different.
 */
public class TupleType extends Type {
    private final List<Type> elementTypes;

    public TupleType(List<Type> elementTypes) {
        this(NamingHelper.toReservedName(TupleType.class.getSimpleName()), elementTypes);
    }

    public TupleType(String name, List<Type> elementTypes) {
        super(name);
        this.elementTypes = elementTypes;
    }

    public List<Type> getElementTypes() {
        return elementTypes;
    }

    public int getSize() {
        return elementTypes.size();
    }

    public Type getElementType(int index) {
        return elementTypes.get(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TupleType other) {
            return elementTypes.equals(other.elementTypes);
        }
        return false;
    }
}
