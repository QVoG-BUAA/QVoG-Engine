package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.values.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represent a function call.
 * <p>
 * FIXME:
 */
public final class CallExpression extends Expression {
    private final Reference function;
    private final List<Expression> arguments;

    public CallExpression(Type type, Reference function, List<Expression> arguments) {
        super(type);
        this.function = function;
        this.function.setParent(this);
        this.arguments = arguments;
        this.arguments.forEach(arg -> arg.setParent(this));
    }

    public CallExpression(Reference function, List<Expression> arguments) {
        super();
        this.function = function;
        this.arguments = arguments;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Reference getFunction() {
        return function;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public int getArgumentsSize() {
        return this.arguments.size();
    }

    public Expression getArgumentAt(int index) {
        return this.arguments.get(index);
    }

    @Override
    public void addChildren(Collection<Value> children) {
        children.add(function);
        children.addAll(arguments);
        function.addChildren(children);
        arguments.forEach(arg -> arg.addChildren(children));
    }

    public static class Builder extends ExpressionBuilder<CallExpression> {
        private final List<Expression> arguments = new ArrayList<>();
        private Reference function;

        public Builder function(Reference function) {
            this.function = function;
            return this;
        }

        public Builder addArgument(Expression argument) {
            this.arguments.add(argument);
            return this;
        }

        @Override
        public CallExpression build() {
            return new CallExpression(type, function, arguments);
        }
    }
}
