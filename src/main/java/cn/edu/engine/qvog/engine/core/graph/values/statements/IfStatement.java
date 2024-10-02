package cn.edu.engine.qvog.engine.core.graph.values.statements;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;

import java.util.Collection;

/**
 * TODO: How to make if statement know its children?
 *  Then and else statements are tend to be blocks, which contains
 *  multiple statements, so it is not feasible to make them children
 *  of if statement. However, if not, how to make if statement know
 *  its scope?
 * UPD: not only if, for every statement may have {}, such as switch\
 * for\while\do-while. Cxx can do it when build graph. —— dxj
 */
public class IfStatement extends Statement {
    private final Expression condition;
    private final Statement thenStatement;
    private final Statement elseStatement;

    public IfStatement(Expression condition) {
        this(condition, null, null);
    }

    public IfStatement(Expression condition, Statement thenStatement, Statement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void addChildren(Collection<Value> children) {
        if (condition != null) {
            children.add(condition);
            condition.addChildren(children);
        }
        if (thenStatement != null) {
            children.add(thenStatement);
            thenStatement.addChildren(children);
        }
        if (elseStatement != null) {
            children.add(elseStatement);
            elseStatement.addChildren(children);
        }
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getThenStatement() {
        return thenStatement;
    }

    public Statement getElseStatement() {
        return elseStatement;
    }
}
