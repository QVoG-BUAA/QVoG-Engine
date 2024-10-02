package cn.edu.engine.qvog.engine.dsl.lib.flow.impl;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.DeclarationStatement;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.ArraySubExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.AssignExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.dsl.lib.flow.IFlow;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.IVertexFlowStrategy;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Euler flow implementation. This will traverse the graph using DFS,
 * and visit edges at most once.
 */
public class EulerFlow extends DfsFlow {
    private EulerFlow(IVertexFlowStrategy strategy, List<String> attr) {
        super(strategy, attr);
    }

    private EulerFlow(IVertexFlowStrategy strategy, List<String> attr, Set<Long> escapeFromBarrier) {
        super(strategy, attr, escapeFromBarrier);
    }

    public static IFlow.Builder builder() {
        return new BaseFlowBuilder() {
            @Override
            public IFlow build() {
                ArrayList<String> objects = new ArrayList<>();
                objects.add(attrOverEdge);
                return new EulerFlow(strategy, objects, escapeFromBarrier);
            }
        };
    }

    @Override
    protected boolean onEnter(Value value, Edge edge) {
        if (!super.onEnter(value, edge)) {
            return false;
        }
        if (edge != null) {
            if (visited.contains(edge.id())) {
                return false;
            }
            visited.add(edge.id());

            // TODO seems useless
            boolean hasAssignExpr = value.toStream().anyMatch(v -> v instanceof AssignExpression assignExpression
                    && assignExpression.getValue() instanceof Reference);
            boolean hasDeclAssignExpr = value.toStream().anyMatch(v -> v instanceof DeclarationStatement declarationStatement
                    && declarationStatement.getValues().size() == 1
                    && (declarationStatement.getValueAt(0) instanceof Reference ||
                    declarationStatement.getValueAt(0) instanceof ArraySubExpression
            ));
            Property<Object> defineOperationLike = edge.property("defineOperationLike");
            if (defineOperationLike instanceof EmptyProperty<Object>) {
                return true;
            }
            if ((hasAssignExpr || hasDeclAssignExpr)
                    && defineOperationLike.value().toString().equals("none")) {
                this.escapeFromBarrier.add(value.getNode().id());
                if (hasAssignExpr) {
                    Stream<Value> valueStream = value.toStream().filter(v -> v instanceof AssignExpression);
                    valueStream.forEach(v -> {
                        AssignExpression assignExpression = (AssignExpression) v;
                        if (assignExpression.getTarget(0) instanceof Reference reference) {
                            this.attrOverEdge.add(reference.getName());
                        }
                    });
                } else {
                    value.toStream().filter(v -> v instanceof DeclarationStatement)
                            .forEach(v -> this.attrOverEdge.add(((DeclarationStatement) v).getTargets().get(0).getName()));
                }
            }
        }
        return true;
    }

    @Override
    protected boolean onExit(Value value, Edge edge) {
        if (edge != null) {
            visited.remove(edge.id());
        }
        return true;
    }

    @Override
    protected boolean onExitFunctionCall(int idx) {
        while (visited.size() > idx) {
            visited.pop();
        }
        return true;
    }
}
