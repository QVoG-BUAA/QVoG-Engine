package cn.edu.engine.qvog.engine.dsl.lib.flow;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.dsl.data.IColumn;
import cn.edu.engine.qvog.engine.dsl.data.ITable;
import cn.edu.engine.qvog.engine.dsl.fluent.flow.AbstractFlowPredicate;
import cn.edu.engine.qvog.engine.dsl.fluent.flow.BaseFlowDescriptor;
import cn.edu.engine.qvog.engine.dsl.fluent.flow.IFlowDescriptor;
import cn.edu.engine.qvog.engine.dsl.fluent.flow.IFlowPredicate;
import cn.edu.engine.qvog.engine.dsl.lib.flow.impl.EulerFlow;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.VertexFlowStrategies;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.MatchNone;
import cn.edu.engine.qvog.engine.helper.graph.FlowHelper;

public class InverseControlAndCgFlowPredicate extends AbstractFlowPredicate {
    private InverseControlAndCgFlowPredicate(IDbContext dbContext, String sourceTableName, String sinkTableName,
                                             String barrierTableName, String alias,
                                             Boolean flowSensitive, Boolean addSysExit, Boolean addEntryExit,
                                             IValuePredicate specialSinkPredicate) {
        super(dbContext, sourceTableName, sinkTableName, barrierTableName, alias, flowSensitive,
                addSysExit, addEntryExit, specialSinkPredicate);
    }

    public static IFlowDescriptor builder() {
        return new BaseFlowDescriptor() {
            @Override
            public IFlowPredicate exists() {
                return new InverseControlAndCgFlowPredicate(dbContext, sourceTableName, sinkTableName, barrierTableName, alias,
                        flowSensitive, addSysExit, addEntryExit, specialSinkPredicate);
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
        if (sink.count() == 0 && specialSinkPredicate instanceof MatchNone) {
            return;
        }
        var flowIter = EulerFlow.builder().withStrategy(VertexFlowStrategies.CFG_CG_REVERSE).build().open(current);
        FlowHelper.iterateOverFlow(current, source, sink, barrier, result,
                this.flowSensitive, this.addSysExit, this.addEntryExit, flowIter, alias, specialSinkPredicate);
    }
}
