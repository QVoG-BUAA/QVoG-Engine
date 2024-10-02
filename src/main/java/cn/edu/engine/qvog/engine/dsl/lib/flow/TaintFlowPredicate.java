package cn.edu.engine.qvog.engine.dsl.lib.flow;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.dsl.data.DataColumn;
import cn.edu.engine.qvog.engine.dsl.data.IColumn;
import cn.edu.engine.qvog.engine.dsl.data.ITable;
import cn.edu.engine.qvog.engine.dsl.data.IndexTypes;
import cn.edu.engine.qvog.engine.dsl.fluent.flow.AbstractFlowPredicate;
import cn.edu.engine.qvog.engine.dsl.fluent.flow.BaseFlowDescriptor;
import cn.edu.engine.qvog.engine.dsl.fluent.flow.IFlowDescriptor;
import cn.edu.engine.qvog.engine.dsl.fluent.flow.IFlowPredicate;
import cn.edu.engine.qvog.engine.dsl.lib.flow.impl.EulerFlow;
import cn.edu.engine.qvog.engine.dsl.lib.flow.impl.FlowStream;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.VertexFlowStrategies;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.MatchNone;
import cn.edu.engine.qvog.engine.helper.Tuple;
import cn.edu.engine.qvog.engine.helper.graph.FlowHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * Taint flow implementation. The taint flow is a flow that is used to track the
 * taint propagation in the graph. The implementation is as follows:
 * <ul>
 * <li>1. Track the data flow from the source to the sink.</li>
 * <li>2. With the result of 1., track the control flow from the source to the sink.</li>
 * <li>3. In 2., if the source and sink are connected without barriers, add the flow
 * to the result.</li>
 * </ul>
 */
public class TaintFlowPredicate extends AbstractFlowPredicate {
    private Set<Tuple<Value, Value>> added = new HashSet<>();

    private TaintFlowPredicate(IDbContext dbContext, String sourceTableName, String sinkTableName,
                               String barrierTableName, String alias,
                               Boolean flowSensitive, Boolean addSysExit, Boolean addEntryExit,
                               IValuePredicate specialSinkPredicate) {
        super(dbContext, sourceTableName, sinkTableName, barrierTableName, alias, flowSensitive,
                addSysExit, addEntryExit, specialSinkPredicate);
    }

    public static IFlowDescriptor with() {
        return new BaseFlowDescriptor() {
            @Override
            public IFlowPredicate exists() {
                return new TaintFlowPredicate(dbContext, sourceTableName, sinkTableName, barrierTableName, alias,
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
        if (sink.count() == 0 && sink instanceof DataColumn && specialSinkPredicate instanceof MatchNone) {
            return;
        }
        IColumn dataFlowResult = DataColumn.builder().withName(sink.name()).withIndex(IndexTypes.ValueIndex).build();
        existsDataFlow(current, sink, dataFlowResult, false);
        if (dataFlowResult.count() == 0 && specialSinkPredicate instanceof MatchNone) {
            return;
        }
        if (barrier != null) {
            IColumn barrierDataResult = DataColumn.builder().withName(barrier.name()).withIndex(IndexTypes.ValueIndex).build();
            existsDataFlow(current, barrier, barrierDataResult, true);
            existsControlOrCgFlow(current, source, dataFlowResult, barrierDataResult, result, specialSinkPredicate);
        } else {
            existsControlOrCgFlow(current, source, dataFlowResult, barrier, result, specialSinkPredicate);
        }
    }

    private void existsDataFlow(Value current, IColumn sink, IColumn result, Boolean isBarrier) {
        Value sourceAncestor = cn.edu.engine.qvog.engine.dsl.lib.flow.FlowUtil.getDeclAncestor(current);
        var flowIter = EulerFlow.builder().withStrategy(VertexFlowStrategies.DFG).build().open(current);
        while (flowIter.hasNext()) {
            FlowStream stream = flowIter.next();
            var it = stream.iterator();

            cn.edu.engine.qvog.engine.dsl.lib.flow.FlowPath path = new cn.edu.engine.qvog.engine.dsl.lib.flow.FlowPath();
            while (it.hasNext()) {
                Value next = it.next().getValue0();
                path.add(next);
                if (sink.containsValue(next)) {
                    result.addValue(next);
                }
            }
        }

        if (sink instanceof DataColumn) {
            if (isBarrier) {
                sink.iterator().forEachRemaining(sinkNode -> {
                    Value declAncestor = cn.edu.engine.qvog.engine.dsl.lib.flow.FlowUtil.getDeclAncestor((Value) sinkNode);
                    if (sourceAncestor != null && declAncestor != null
                            && sourceAncestor.getNode().id() == declAncestor.getNode().id()) {
                        result.addValue(sinkNode);
                    }
                });
            }
        }
    }

    private void existsControlOrCgFlow(Value current, IColumn source, IColumn sink, IColumn barrier,
                                       ITable result, IValuePredicate specialSinkPredicate) {
        var flowIter = EulerFlow.builder().withStrategy(VertexFlowStrategies.CFG_CG).build().open(current);
        FlowHelper.iterateOverFlow(current, source, sink, barrier, result,
                this.flowSensitive, this.addSysExit, this.addEntryExit, flowIter, alias, specialSinkPredicate);
    }
}