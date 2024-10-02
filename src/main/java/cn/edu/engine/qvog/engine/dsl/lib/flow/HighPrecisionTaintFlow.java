package cn.edu.engine.qvog.engine.dsl.lib.flow;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.DeclarationStatement;
import cn.edu.engine.qvog.engine.core.graph.values.statements.FunctionDefStatement;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.AssignExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
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
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.IVertexFlowStrategy;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.VertexFlowStrategies;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.MatchNone;
import cn.edu.engine.qvog.engine.helper.graph.FlowHelper;

import java.util.Set;

/**
 * use dfg-Attr to promote the precision of taintFlow
 */
public class HighPrecisionTaintFlow extends AbstractFlowPredicate {
    private HighPrecisionTaintFlow(IDbContext dbContext, String sourceTableName, String sinkTableName,
                                   String barrierTableName, String alias,
                                   Boolean addSysExit, Boolean addEntryExit, IValuePredicate specialSinkPredicate) {
        super(dbContext, sourceTableName, sinkTableName, barrierTableName, alias,
                addSysExit, addEntryExit, specialSinkPredicate);
    }

    public static IFlowDescriptor with() {
        return new BaseFlowDescriptor() {
            @Override
            public IFlowPredicate exists() {
                return new HighPrecisionTaintFlow(dbContext, sourceTableName, sinkTableName, barrierTableName, alias,
                        addSysExit, addEntryExit, specialSinkPredicate);
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
        IColumn dataFlowResult = DataColumn.builder().withName(sink.name()).withIndex(IndexTypes.ValueIndex).build();
        if (dataFlowResult.count() == 0 && specialSinkPredicate instanceof MatchNone) {
            return;
        }
        Set<Long> escapeFromBarriers = existsDataFlow(current, sink, dataFlowResult);
        if (barrier != null) {
            IColumn barrierDataFlow = DataColumn.builder().withName(barrier.name()).withIndex(IndexTypes.ValueIndex).build();
            existsDataFlow(current, barrier, barrierDataFlow);
            existsControlOrCgFlow(current, source, dataFlowResult, barrierDataFlow, result, escapeFromBarriers);
        } else {
            existsControlOrCgFlow(current, source, dataFlowResult, barrier, result, escapeFromBarriers);
        }
    }

    private Set<Long> existsDataFlow(Value current, IColumn sink, IColumn result) {
        IVertexFlowStrategy strategy = VertexFlowStrategies.DFG_WITH_ATTR;
        String attr = null;
        if (current instanceof AssignExpression assignExpression) {
            Expression target = assignExpression.getTarget();
            if (target instanceof Reference reference) {
                attr = reference.getName();
            }
        } else if (current instanceof DeclarationStatement declarationStatement) {
            attr = declarationStatement.getTargets().get(0).getName();
        } else if (current instanceof FunctionDefStatement functionDefStatement) {
            attr = VertexFlowStrategies.DECL2USAGE;
            if ("main".equals(functionDefStatement.getFunction().getName())) {
                if (functionDefStatement.getArguments().size() == 2) {
                    attr = functionDefStatement.getArguments().get(1).getName();
                }
            }
        }

        if (attr == null) {
            attr = "";
            strategy = VertexFlowStrategies.DFG;
        }

        IFlow dataFlow = EulerFlow.builder().withStrategy(strategy)
                .withAttrOverEdge(attr)
                .build();
        var flowIter = dataFlow.open(current);
        while (flowIter.hasNext()) {
            FlowStream stream = flowIter.next();
            var it = stream.iterator();
            while (it.hasNext()) {
                Value next = it.next().getValue0();
                // node failed
                if (sink.containsValue(next)) {
                    result.addValue(next);
                }
            }
        }
        return dataFlow.getEscapeFromBarrier();
    }

    private void existsControlOrCgFlow(Value current, IColumn source, IColumn sink, IColumn barrier, ITable result,
                                       Set<Long> escapeFromBarrier) {
        var flowIter = EulerFlow.builder().withStrategy(VertexFlowStrategies.CFG_CG)
                .withEscapeFromBarrier(escapeFromBarrier)
                .build().open(current);
        FlowHelper.iterateOverFlow(current, source, sink, barrier, result, this.addSysExit, this.addEntryExit,
                flowIter, alias, specialSinkPredicate);
    }
}
