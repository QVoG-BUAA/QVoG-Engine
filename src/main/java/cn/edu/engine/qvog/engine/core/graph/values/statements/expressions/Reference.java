package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.HasName;

/**
 * A reference in code, it can be a variable, a function, a class, etc.
 * <p>
 * It can be a left value or a right value, or unspecified.
 * <p>
 * TODO: Can reference get the declaration of the thing it refers to?
 */
public class Reference extends Expression implements HasName {
    private final String name;
    private final ReferenceTypes refType;

    public Reference(Type type, String name) {
        this(type, name, ReferenceTypes.Unspecified);
    }

    public Reference(Type type, String name, ReferenceTypes refType) {
        super(type);
        this.name = name;
        this.refType = refType;
    }

    public Reference(String name) {
        this(name, ReferenceTypes.Unspecified);
    }

    public Reference(String name, ReferenceTypes refType) {
        super();
        this.name = name;
        this.refType = refType;
    }

    public ReferenceTypes getRefType() {
        return refType;
    }

    public boolean isLeftValue() {
        return refType == ReferenceTypes.Left || refType == ReferenceTypes.Both;
    }

    public boolean isRightValue() {
        return refType == ReferenceTypes.Right || refType == ReferenceTypes.Both;
    }

    public boolean isBoth() {
        return refType == ReferenceTypes.Both;
    }

    public boolean isUnspecified() {
        return refType == ReferenceTypes.Unspecified;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * The type of the reference.
     */
    public enum ReferenceTypes {
        Left,
        Right,

        // The reference is both a left value and a right value.
        Both,

        // The reference is unspecified.
        Unspecified
    }
}
