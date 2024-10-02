package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.types.UnknownType;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.HasParent;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.HasType;
import cn.edu.engine.qvog.engine.core.graph.values.statements.Statement;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base class for all instructions. Instructions mainly focus
 * on operations, such as operators.
 */
public abstract class Expression extends Statement implements HasType, HasParent<Statement> {
    private Type type;

    @JsonIgnore
    private Statement parent;

    public Expression() {
        this(UnknownType.DEFAULT);
    }

    public Expression(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Statement getParent() {
        return parent;
    }

    @Override
    public void setParent(Statement statement) {
        this.parent = statement;
    }

    protected abstract static class ExpressionBuilder<T extends Expression> {
        protected Type type = UnknownType.DEFAULT;

        public ExpressionBuilder<T> type(Type type) {
            this.type = type;
            return this;
        }

        public abstract T build();
    }
}
