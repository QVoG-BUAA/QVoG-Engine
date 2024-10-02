package cn.edu.engine.qvog.engine.core.graph.types;

import cn.edu.engine.qvog.engine.helper.NamingHelper;

/**
 * Indicates that a type is unknown. For example, Python has no type.
 * <p>
 * However, types can be inferred from the context, so this is not always
 * the case. For example, we can infer the type of literals in Python,
 * instead of leaving it as unknown when we can get a more specific type.
 */
public final class UnknownType extends Type {
    public static final UnknownType DEFAULT = new UnknownType();

    public UnknownType() {
        this(NamingHelper.toReservedName(UnknownType.class.getSimpleName()));
    }

    public UnknownType(String name) {
        super(name, Origins.Unknown);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UnknownType;
    }
}
