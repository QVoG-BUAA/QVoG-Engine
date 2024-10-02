package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.Type;

public class Literal extends Expression {
    Object value;

    public Literal(Type type, Object value) {
        super(type);
        this.value = value;
    }

    public Literal(Object value) {
        this.value = value;
    }

    /**
     * Get the value of the literal.
     * WARNING: May cause ClassCastException if the type of the value
     *  is not the same as the type of the literal.
     *
     * @param <T> Specify the type of the value
     * @return The value of the literal
     */
    public <T> T getTypedValue() {
        return (T) value;
    }

    public Object getValue() {
        return value;
    }

    public boolean isNull() {
        return value == null;
    }
}
