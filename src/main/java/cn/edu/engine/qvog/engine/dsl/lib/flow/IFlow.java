package cn.edu.engine.qvog.engine.dsl.lib.flow;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.dsl.lib.flow.impl.FlowStream;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.IVertexFlowStrategy;

import java.util.Iterator;
import java.util.Set;

public interface IFlow {
    /**
     * Open a flow from a source node. This will calculate ALL the flow steps.
     *
     * @param source The source node
     * @return Itself.
     */
    Iterator<FlowStream> open(Value source);

    /**
     * Get {@link FlowStream} iterator.
     *
     * @return The iterator.
     */
    Iterator<FlowStream> iterator();

    Set<Long> getEscapeFromBarrier();

    interface Builder {
        Builder withStrategy(IVertexFlowStrategy strategy);

        Builder withAttrOverEdge(String attrOverEdge);

        Builder withEscapeFromBarrier(Set<Long> escapeFromBarrier);

        IFlow build();
    }
}
