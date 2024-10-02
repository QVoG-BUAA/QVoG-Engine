package cn.edu.engine.qvog.engine.core.graph.types;

import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.KeyValuePair;
import cn.edu.engine.qvog.engine.helper.NamingHelper;

/**
 * Represents a key-value pair type. Mainly used for
 * {@link KeyValuePair}.
 */
public class PairType extends Type {
    private final Type keyType;
    private final Type valueType;

    public PairType(String name, Origins origin, Type keyType, Type valueType) {
        super(name, origin);
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public PairType(Type keyType, Type valueType) {
        super(NamingHelper.toReservedName(PairType.class.getSimpleName()));
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public Type getKeyType() {
        return keyType;
    }

    public Type getValueType() {
        return valueType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PairType other) {
            return keyType.equals(other.keyType) && valueType.equals(other.valueType);
        }
        return false;
    }
}
