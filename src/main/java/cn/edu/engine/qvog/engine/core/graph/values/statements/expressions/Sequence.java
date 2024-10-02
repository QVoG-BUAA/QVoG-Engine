package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.PairType;
import cn.edu.engine.qvog.engine.core.graph.types.SequenceType;
import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.values.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a sequence of expressions. e.g., list, tuple, etc.
 * <p>
 * Also, dictionary can be represented as a sequence of key-value pairs.
 */
public final class Sequence extends Expression {
    private final List<Expression> elements;

    public Sequence(Type type) {
        this(type, new ArrayList<>());
    }

    public Sequence(Type type, List<Expression> elements) {
        super(type);
        this.elements = elements;
        this.elements.forEach(e -> e.setParent(this));
    }

    public Sequence() {
        this(new ArrayList<>());
    }

    public Sequence(List<Expression> elements) {
        super();
        this.elements = elements;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * This is used to tell whether this is an associative sequence.
     * For example, a dictionary is an associative sequence.
     * <p>
     * WARNING: This method is not reliable. It only works when the type
     *  information is available.
     *
     * @return true if this is an associative sequence. Otherwise, false.
     */
    public boolean isAssociative() {
        if (getType() instanceof SequenceType type) {
            return type.getElementType() instanceof PairType;
        }
        return false;
    }

    @Override
    public void addChildren(Collection<Value> children) {
        children.addAll(elements);
        elements.forEach(e -> e.addChildren(children));
    }

    public static class Builder extends ExpressionBuilder<Sequence> {
        private final List<Expression> elements = new ArrayList<>();

        public Builder addElement(Expression element) {
            elements.add(element);
            return this;
        }

        @Override
        public Sequence build() {
            return new Sequence(type, elements);
        }
    }
}
