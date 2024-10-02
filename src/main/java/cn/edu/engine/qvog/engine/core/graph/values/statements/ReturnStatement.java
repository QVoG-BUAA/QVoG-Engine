package cn.edu.engine.qvog.engine.core.graph.values.statements;

import cn.edu.engine.qvog.engine.core.graph.values.Value;

import java.util.Collection;
import java.util.List;

/**
 * Return statement.
 */
public class ReturnStatement extends Statement {
    private final List<Value> values;

    public ReturnStatement(List<Value> values) {
        this.values = values;
    }

    public ReturnStatement(Value value) {
        this.values = List.of(value);
    }

    public List<Value> getValues() {
        return values;
    }

    public Value getValue() {
        if (values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

    @Override
    public void addChildren(Collection<Value> children) {
        children.addAll(values);
        for (Value value : values) {
            value.addChildren(children);
        }
    }
}
