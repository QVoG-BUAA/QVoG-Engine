package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.TupleType;
import cn.edu.engine.qvog.engine.core.graph.values.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ArraySubExpression extends Expression {
    private final List<Expression> subList;
    private final Expression array;

    public ArraySubExpression(List<Expression> subList, Expression array) {
        this.subList = subList;
        this.array = array;

        ArrayList<Expression> references = new ArrayList<>(subList);
        references.add(array);
        this.setType(new TupleType(references.stream().map(Expression::getType).toList()));
        this.subList.forEach(t -> t.setParent(this));
        this.array.setParent(this);
    }

    @Override
    public void addChildren(Collection<Value> children) {
        children.addAll(this.subList);
        for (var reference : subList) {
            reference.addChildren(children);
        }

        children.add(this.array);
        this.array.addChildren(children);
    }
}
