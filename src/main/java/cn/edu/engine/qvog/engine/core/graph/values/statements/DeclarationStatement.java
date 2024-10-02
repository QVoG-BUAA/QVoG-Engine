package cn.edu.engine.qvog.engine.core.graph.values.statements;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;

import java.util.Collection;
import java.util.List;

/**
 * if decl has assign to init, it likes AssignExpression
 */
public class DeclarationStatement extends Statement {
    private final List<Reference> targets;
    private final List<Expression> values;

    public DeclarationStatement(List<Reference> targets, List<Expression> values) {
        //super(new TupleType(targets.stream().map(Reference::getType).toList()));

        this.targets = targets;
        this.targets.forEach(t -> t.setParent(this));
        this.values = values;
        this.values.forEach(v -> v.setParent(this));
    }

    public List<Reference> getTargets() {
        return targets;
    }

    public List<Expression> getValues() {
        return values;
    }

    public Boolean hasValue() {
        return !values.isEmpty();
    }

    public Expression getValueAt(int index) {
        return values.get(index);
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
