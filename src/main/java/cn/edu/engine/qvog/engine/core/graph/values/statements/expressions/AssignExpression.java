package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.TupleType;
import cn.edu.engine.qvog.engine.core.graph.values.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents an assignment expression.
 * <p>
 * Although ordinary assignment only involves one target and one value,
 * some languages, like Python, allow multiple targets and/or multiple
 * values.
 * <p>
 * The type of assignment should be a tuple type.
 */
public final class AssignExpression extends Expression {
    private final List<Expression> targets;
    private final List<Expression> values;

    public AssignExpression(Expression target, Expression value) {
        this(new ArrayList<>(List.of(target)), new ArrayList<>(List.of(value)));
    }

    public AssignExpression(List<Expression> targets, List<Expression> values) {
        super(new TupleType(targets.stream().map(Expression::getType).toList()));
        // For now, we only support targets and values of the
        // same size.
        if (targets.size() != values.size()) {
            throw new IllegalArgumentException("The number of targets and values should be the same.");
        }

        if (targets.isEmpty()) {
            throw new IllegalArgumentException("The number of targets and values should be greater than 0.");
        }

        this.targets = targets;
        this.targets.forEach(t -> t.setParent(this));
        this.values = values;
        this.values.forEach(v -> v.setParent(this));
    }

    public int getSize() {
        return targets.size();
    }

    public List<Expression> getTargets() {
        return targets;
    }

    public Expression getTarget(int index) {
        return targets.get(index);
    }

    public Expression getTarget() {
        return targets.get(0);
    }

    public List<Expression> getValues() {
        return values;
    }

    public Expression getValue(int index) {
        return values.get(index);
    }

    public Expression getValue() {
        return values.get(0);
    }

    @Override
    public void addChildren(Collection<Value> children) {
        children.addAll(targets);
        children.addAll(values);
        for (var target : targets) {
            target.addChildren(children);
        }
        for (var value : values) {
            value.addChildren(children);
        }
    }
}
