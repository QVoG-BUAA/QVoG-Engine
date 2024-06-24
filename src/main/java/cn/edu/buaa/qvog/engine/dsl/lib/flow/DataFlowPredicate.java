package cn.edu.buaa.qvog.engine.dsl.lib.flow;

import cn.edu.buaa.qvog.engine.core.graph.values.Value;
import cn.edu.buaa.qvog.engine.db.IDbContext;
import cn.edu.buaa.qvog.engine.dsl.data.IColumn;
import cn.edu.buaa.qvog.engine.dsl.data.ITable;
import cn.edu.buaa.qvog.engine.dsl.fluent.flow.AbstractFlowPredicate;
import cn.edu.buaa.qvog.engine.dsl.fluent.flow.BaseFlowDescriptor;
import cn.edu.buaa.qvog.engine.dsl.fluent.flow.IFlowDescriptor;
import cn.edu.buaa.qvog.engine.dsl.fluent.flow.IFlowPredicate;
import cn.edu.buaa.qvog.engine.dsl.lib.flow.impl.EulerFlow;
import cn.edu.buaa.qvog.engine.dsl.lib.flow.impl.FlowStream;
import cn.edu.buaa.qvog.engine.dsl.lib.flow.strategy.VertexFlowStrategies;

import java.util.Map;

public class DataFlowPredicate extends AbstractFlowPredicate {

    private DataFlowPredicate(IDbContext dbContext, String sourceTableName, String sinkTableName, String barrierTableName, String alias) {
        super(dbContext, sourceTableName, sinkTableName, barrierTableName, alias, false, false, null);
    }

    public static IFlowDescriptor builder() {
        return new BaseFlowDescriptor() {
            @Override
            public IFlowPredicate exists() {
                return new DataFlowPredicate(dbContext, sourceTableName, sinkTableName, barrierTableName, alias);
            }
        };
    }

    @Override
    protected void exists(IColumn source, IColumn sink, IColumn barrier, ITable result) {
        var it = source.iterator();
        while (it.hasNext()) {
            exists((Value) it.next(), source, sink, barrier, result);
        }
    }

    private void exists(Value current, IColumn source, IColumn sink, IColumn barrier, ITable result) {
        var flowIter = EulerFlow.builder().withStrategy(VertexFlowStrategies.DFG).build().open(current);
        while (flowIter.hasNext()) {
            FlowStream stream = flowIter.next();
            FlowPath path = new FlowPath();
            var it = stream.iterator();
            while (it.hasNext()) {
                Value next = it.next().getValue0();
                path.add(next);
                if (barrier.containsValue(next)) {
                    break;
                } else if (sink.containsValue(next)) {
                    result.addRow(Map.of(
                            source.name(), current,
                            sink.name(), next,
                            alias, path
                    ));
                    path = path.clone();
                }
            }
        }
    }
}
