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
import cn.edu.engine.qvog.engine.dsl.lib.flow.impl.HamiltonFlow;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.VertexFlowStrategies;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.MatchNone;
import cn.edu.engine.qvog.engine.helper.graph.FlowHelper;
import cn.edu.engine.qvog.engine.helper.graph.GraphFilter;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * use DataFlow to find the ancestor of source and sink
 * use ControlFlow to judge the reachability (barrier works here)
 */
public class CrossFunctionTaintFlowPredicate extends AbstractFlowPredicate {
    public CrossFunctionTaintFlowPredicate(IDbContext dbContext, String sourceTableName, String sinkTableName,
                                           String barrierTableName, String alias,
                                           Boolean addSysExit, Boolean addEntryExit, IValuePredicate specialSinkPredicate) {
        super(dbContext, sourceTableName, sinkTableName, barrierTableName, alias,
                addSysExit, addEntryExit, specialSinkPredicate);
    }

    public static IFlowDescriptor with() {
        return new BaseFlowDescriptor() {
            @Override
            public IFlowPredicate exists() {
                return new CrossFunctionTaintFlowPredicate(dbContext, sourceTableName, sinkTableName, barrierTableName, alias,
                        addSysExit, addEntryExit, specialSinkPredicate);
            }
        };
    }

    @Override
    protected void exists(IColumn source, IColumn sink, IColumn barrier, ITable result) {
        Iterator<Object> sourceIt = source.iterator();
        Iterator<Object> sinkIt = sink.iterator();

        ArrayList<Pair<Value, ArrayList<Value>>> sourceListList = getValuesDecls(sourceIt);
        ArrayList<Pair<Value, ArrayList<Value>>> sinkListList = getValuesDecls(sinkIt);

        ArrayList<Pair<Value, Value>> sourceSinkPair = new ArrayList<>();
        for (Pair<Value, ArrayList<Value>> sourcePair : sourceListList) {
            HashMap<Long, Integer> counter = new HashMap<>();
            for (Value value : sourcePair.getValue1()) {
                counter.put(value.getNode().id(), 1);
            }
            for (Pair<Value, ArrayList<Value>> sinkPair : sinkListList) {
                for (Value value : sinkPair.getValue1()) {
                    Integer count = counter.get(value.getNode().id());
                    if (count != null) {
                        sourceSinkPair.add(new Pair<>(sourcePair.getValue0(), sinkPair.getValue0()));
                    }
                }
            }
        }

        ArrayList<Value> values = new ArrayList<>();
        for (Pair<Value, Value> objects : sourceSinkPair) {
            IColumn sourceC = DataColumn.builder().withValues(List.of(objects.getValue0())).withName("source").build();
            IColumn sinkC = DataColumn.builder().withValues(List.of(objects.getValue1())).withName("sink").build();

            int backup = result.getColumn("path").count();
            if (barrier == null) {
                existsControlAndCgFlow(objects.getValue0(), sourceC, sinkC, barrier, result);
                if (result.getColumn("path").count() == backup) {
                    values.add(objects.getValue0());
                }
                continue;
            }

            IColumn barrierDataResult = DataColumn.builder().withName(barrier.name()).withIndex(IndexTypes.ValueIndex).build();
            existsDataFlow(objects.getValue0(), barrier, barrierDataResult);
            existsControlAndCgFlow(objects.getValue0(), sourceC, sinkC, barrierDataResult, result);
            if (result.getColumn("path").count() == backup) {
                values.add(objects.getValue0());
            }
        }

        // if sourceSinkPair is empty, then we need to check all the source
        // if sourceSinkPair is not empty, then we need to check all the values
        if ((sourceSinkPair.isEmpty() || !values.isEmpty()) && this.specialSinkPredicate instanceof MatchNone) {
            this.addSysExit = false;

            var gremlinDb = dbContext.getGremlinDb();
            String specialAlias = "specialSink";
            var table = GraphFilter.open().withDb(gremlinDb)
                    .withPredicate(this.specialSinkPredicate).filter(specialAlias);
            IColumn specialSinkColumn = table.getColumn(specialAlias);


            if (sourceSinkPair.isEmpty()) {
                sourceIt = source.iterator();
                while (sourceIt.hasNext()) {
                    Object aSource = sourceIt.next();
                    existsControlAndCgFlow(((Value) aSource), source, specialSinkColumn, barrier, result);
                }
            } else {
                for (Value value : values) {
                    existsControlAndCgFlow(value, source, specialSinkColumn, barrier, result);
                }
            }
        }
    }

    private ArrayList<Pair<Value, ArrayList<Value>>> getValuesDecls(Iterator<Object> it) {
        ArrayList<Pair<Value, ArrayList<Value>>> res = new ArrayList<>();
        while (it.hasNext()) {
            Value next = (Value) it.next();
            Pair<Value, ArrayList<Value>> objects = new Pair<>(next, getValueDeclValue(next));
            res.add(objects);
        }
        return res;
    }

    private void existsControlAndCgFlow(Value current, IColumn source, IColumn sink, IColumn barrier, ITable result) {
        var flowIter = EulerFlow.builder().withStrategy(VertexFlowStrategies.CFG_CG).build().open(current);
        FlowHelper.iterateOverFlow(current, source, sink, barrier, result, this.addSysExit, this.addEntryExit,
                flowIter, alias, specialSinkPredicate);
    }

    private void existsDataFlow(Value current, IColumn barriers, IColumn result) {
        Value sourceAncestor = FlowUtil.getDeclAncestor(current);
        var barrierIterator = barriers.iterator();
        while (barrierIterator.hasNext()) {
            Value barrier = (Value) barrierIterator.next();
            Value declAncestor = FlowUtil.getDeclAncestor(barrier);
            if (sourceAncestor != null && declAncestor != null
                    && sourceAncestor.getNode().id() == declAncestor.getNode().id()) {
                result.addValue(barrier);
            }
        }
    }

    private ArrayList<Value> getValueDeclValue(Value current) {
        ArrayList<Value> res = new ArrayList<>();
        var flowIter = HamiltonFlow.builder().withStrategy(VertexFlowStrategies.DECL).build().open(current);
        while (flowIter.hasNext()) {
            FlowStream stream = flowIter.next();
            boolean isFirst = true;
            var it = stream.iterator();
            while (it.hasNext()) {
                if (!isFirst) {
                    Value next = it.next().getValue0();
                    res.add(next);
                } else {
                    it.next();
                    isFirst = false;
                }
            }
        }
        return res;
    }
}
