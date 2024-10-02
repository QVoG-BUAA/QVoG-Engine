package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.HasBase;

import java.util.Collection;

/**
 * Represent member access expression. It is a special reference.
 * For example, in {@code a.b.c}, {@code a.b} is the base of the member
 * access expression and {@code c} is the member. {@code a.b} is
 * represented as {@link Expression}, but {@code c} is represented
 * as {@link Reference#getName()}, which is simply a {@link String}.
 */
public final class MemberExpression extends Reference implements HasBase {
    private final Expression base;

    public MemberExpression(Type type, String name, ReferenceTypes refType, Expression base) {
        super(type, name, refType);
        this.base = base;
        /*
        TODO: This may be a little wired that the base is not the parent.
         At least, it is the case in Python AST.
         I Wonder if it is the same in other languages.
         */
        this.base.setParent(this);
    }

    public MemberExpression(String name, ReferenceTypes refType, Expression base) {
        super(name, refType);
        this.base = base;
    }

    public MemberExpression(String name, Expression base) {
        super(name);
        this.base = base;
    }

    @Override
    public Expression getBase() {
        return base;
    }

    @Override
    public void addChildren(Collection<Value> children) {
        children.add(base);
        base.addChildren(children);
    }
}
