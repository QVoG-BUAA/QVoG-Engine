package cn.edu.engine.qvog.engine.dsl.lib.flow;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.dsl.lib.flow.impl.FlowStream;
import cn.edu.engine.qvog.engine.dsl.lib.flow.impl.HamiltonFlow;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.VertexFlowStrategies;

public class FlowUtil {
    public static Value getDeclAncestor(Value current) {
        var declFlowIter = HamiltonFlow.builder().withStrategy(VertexFlowStrategies.DECL).build().open(current);
        while (declFlowIter.hasNext()) {
            FlowStream stream = declFlowIter.next();
            boolean isFirst = true;
            var it = stream.iterator();
            while (it.hasNext()) {
                if (!isFirst) {
                    return it.next().getValue0();
                } else {
                    it.next();
                    isFirst = false;
                }
            }
        }
        return null;
    }
}
