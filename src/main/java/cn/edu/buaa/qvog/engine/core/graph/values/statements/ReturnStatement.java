package cn.edu.buaa.qvog.engine.core.graph.values.statements;

import cn.edu.buaa.qvog.engine.core.graph.values.Value;

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
}