package cn.edu.engine.qvog.engine.helper.graph;

import cn.edu.engine.qvog.engine.core.graph.values.CacheValue;
import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.dsl.data.IColumn;
import cn.edu.engine.qvog.engine.dsl.data.ITable;
import cn.edu.engine.qvog.engine.dsl.lib.flow.FlowPath;
import cn.edu.engine.qvog.engine.dsl.lib.flow.impl.FlowStream;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class FlowHelper {

    public static void iterateOverFlow(Value current, IColumn source, IColumn sink, IColumn barrier, ITable result,
                                       Boolean addSysExit, Boolean addEntryExit,
                                       Iterator<FlowStream> flowIter,
                                       String alias,
                                       IValuePredicate specialSinkPredicate) {
        iterateOverFlow(current, source, sink, barrier, result, true, addSysExit, addEntryExit, flowIter, alias, specialSinkPredicate);
    }

    public static void iterateOverFlow(Value current, IColumn source, IColumn sink, IColumn barrier, ITable result,
                                       Boolean flowSensitive,
                                       Boolean addSysExit, Boolean addEntryExit,
                                       Iterator<FlowStream> flowIter,
                                       String alias,
                                       IValuePredicate specialSinkPredicate) {
//        if (!flowIter.hasNext()) {
//            result.addRow(Map.of(
//                    source.name(), current,
//                    sink.name(), current,
//                    alias, new FlowPath()
//            ));
//            return;
//        }
        ArrayList<Value> hasSinkList = new ArrayList<>();
        while (flowIter.hasNext()) {
            FlowStream stream = flowIter.next();
            FlowPath path = new FlowPath();
            var it = stream.iterator();
            boolean hasThisSourceAdded = false;
            boolean isBarrier = false;
            while (it.hasNext()) {
                Pair<Value, Edge> nextP = it.next();
                Value next = nextP.getValue0();

                // cacheSpeedUp value
                if (next instanceof CacheValue cacheValue) {
                    // if there is a barrier between CacheValue lineno-range, break
                    if (barrier != null) {
                        boolean hasBarrier = false;
                        for (int i = 0; i < barrier.count(); i ++) {
                            Value barrierValue = (Value) barrier.getValue(i);
                            int barrierLine = barrierValue.getNode().lineNumber();
                            Pair<Integer, Integer> linenoBetween = cacheValue.getLinenoBetween();
                            if (barrierLine >= linenoBetween.getValue0()
                                    && barrierLine <= linenoBetween.getValue1()) {
                                hasBarrier = true;
                                break;
                            }
                        }
                        if (hasBarrier) {
                            break;
                        }
                    }
                    // if there is a sink between CacheValue lineno-range, add it to result
                    if (sink != null) {
                        boolean hasSink = false;
                        for (int i = 0; i < sink.count(); i ++) {
                            Value sinkValue = (Value) sink.getValue(i);
                            int sinkLineno = sinkValue.getNode().lineNumber();
                            Pair<Integer, Integer> linenoBetween = cacheValue.getLinenoBetween();
                            if (sinkLineno >= linenoBetween.getValue0()
                                    && sinkLineno <= linenoBetween.getValue1()) {
                                hasSink = true;
                                break;
                            }
                        }
                        if (hasSink) {
                            hasThisSourceAdded = true;
                            if (specialSinkPredicate == null) {
                                result.addRow(Map.of(
                                        source.name(), current,
                                        sink.name(), next,
                                        alias, path
                                ));
                            }
                            break;
                        }
                    }
                }

                path.add(next);
                boolean isLast = !it.hasNext();
                if (isLast && next.getNode().getVertex().id() == current.getNode().getVertex().id()) {
                    break;
                }
                if (flowSensitive) {
                    if ((sink.containsValue(next) && next != current)) {
                        if (!addSysExit && !addEntryExit) {
                            result.addRow(Map.of(
                                    source.name(), current,
                                    sink.name(), next,
                                    alias, path
                            ));
                            path = path.clone();
                        }
                        hasThisSourceAdded = true;
                    } else if (!hasThisSourceAdded &&
                            (specialSinkPredicate != null &&
                                    (specialSinkPredicate.test(next) || (addSysExit || addEntryExit) && isLast))) {
                        result.addRow(Map.of(
                                source.name(), current,
                                sink.name(), next,
                                alias, path
                        ));
                        path = path.clone();
                    }

                    if (!next.getCanEscapeFromBarrier()
                            && barrier != null && barrier.containsValue(next) && next != current) {
                        isBarrier = true;
                        break;
                    }
                } else {
                    if (sink.containsValue(next) && next != current) {
                        hasSinkList.add(current);
                        hasThisSourceAdded = true;
                    }

                    if (!next.getCanEscapeFromBarrier()
                            && barrier != null && barrier.containsValue(next) && next != current) {
                        break;
                    }
                }
            }
            // System.out.println("path: " + path);
//            if (!isBarrier && !hasThisSourceAdded && addSysExit) {
//                result.addRow(Map.of(
//                        source.name(), current,
//                        sink.name(), current,
//                        alias, new FlowPath()
//                ));
//            }
        }

        if (!flowSensitive) {
            if (!hasSinkList.contains(current)) {
                // FIXME source-source
                result.addRow(Map.of(
                        source.name(), current,
                        sink.name(), current,
                        alias, new FlowPath()
                ));
            }
        }
    }
}
