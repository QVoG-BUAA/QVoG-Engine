package cn.edu.engine.qvog.engine.dsl.lib.flow.impl;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.dsl.lib.flow.IFlow;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.IVertexFlowStrategy;
import org.apache.tinkerpop.gremlin.structure.Edge;

import java.util.HashSet;
import java.util.Set;

/**
 * Hamilton flow implementation. This will traverse the graph using DFS,
 * and visit nodes at most once.
 */
public class HamiltonFlow extends DfsFlow {
    private final Set<Long> visited = new HashSet<>();

    public HamiltonFlow(IVertexFlowStrategy strategy) {
        super(strategy);
    }

    public static IFlow.Builder builder() {
        return new BaseFlowBuilder() {
            @Override
            public IFlow build() {
                return new HamiltonFlow(strategy);
            }
        };
    }

    @Override
    protected boolean onEnter(Value value, Edge edge) {
        if (!super.onEnter(value, edge)) {
            return false;
        }
        if (visited.contains(value.getNode().id())) {
            return false;
        }
        visited.add(value.getNode().id());
        return true;
    }
}