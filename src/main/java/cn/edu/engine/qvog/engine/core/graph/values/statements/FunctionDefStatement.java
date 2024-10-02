package cn.edu.engine.qvog.engine.core.graph.values.statements;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.CallExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;

import java.util.Collection;
import java.util.List;

/**
 * it is like {@link CallExpression}
 */
public class FunctionDefStatement extends Statement {
    private final Reference function;
    private final List<Reference> arguments;

    public FunctionDefStatement(Reference function, List<Reference> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public Reference getFunction() {
        return function;
    }

    public List<Reference> getArguments() {
        return arguments;
    }

    @Override
    public void addChildren(Collection<Value> children) {
        children.add(function);
        function.addChildren(children);

        children.addAll(arguments);
        for (Reference argument : arguments) {
            argument.addChildren(children);
        }
    }
}
