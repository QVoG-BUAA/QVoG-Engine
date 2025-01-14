package cn.edu.engine.qvog.engine.dsl.lib.flow.impl;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.cxx.virtuals.CxxFunctionExit;
import cn.edu.engine.qvog.engine.dsl.lib.flow.IFlow;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.IVertexFlowStrategy;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.VertexFlowStrategies;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.javatuples.Pair;

import java.util.*;

/**
 * DFS flow implementation.
 */
public class DfsFlow extends BaseFlow {
    protected final Integer MAX_DEPTHS = 300;
    protected final Stack<Object> visited = new Stack<>();
    private final Stack<Pair<Value, Edge>> stack = new Stack<>();
    protected Stack<Pair<Long, Integer>> lastCgCaller = new Stack<>();

    DfsFlow(IVertexFlowStrategy strategy) {
        super(strategy);
    }

    DfsFlow(IVertexFlowStrategy strategy, List<String> attrs) {
        super(strategy, attrs);
    }

    DfsFlow(IVertexFlowStrategy strategy, List<String> attrs, Set<Long> escapeFromBarrier) {
        super(strategy, attrs, escapeFromBarrier);
    }

    public static IFlow.Builder builder() {
        return new BaseFlowBuilder() {
            @Override
            public IFlow build() {
                return new DfsFlow(strategy);
            }
        };
    }

    @Override
    public Iterator<FlowStream> open(Value source) {
        stack.push(Pair.with(source, null));
        dfs(source, null, 0);
        stack.pop();

        for (FlowStream stream : streams) {
            Iterator<Pair<Value, Edge>> iterator = stream.iterator();
            while (iterator.hasNext()) {
                Pair<Value, Edge> next = iterator.next();
                Value value = next.getValue0();
                if (this.escapeFromBarrier.contains(value.getNode().id())) {
                    value.setCanEscapeFromBarrier(true);
                }
            }
        }
        return iterator();
    }

    protected boolean onEnter(Value value, Edge edge) {
        // FIXME: This is suspicious to let an unknown value pass
        // return !value.isUnknown();
        return true;
    }

    private boolean checkEdgeLeft(List<Pair<Vertex, Edge>> neighbors) {
        for (Pair<Vertex, Edge> neighbor : neighbors) {
            if (!visited.contains(neighbor.getValue1().id())) {
                return true;
            }
        }
        return false;
    }

    private boolean dfs(Value value, Edge edge, int depths) {
        if (!onEnter(value, edge)) {
            return false;
        }
        if (depths > MAX_DEPTHS) {
            return false;
        }
        var neighbors = strategy.getNeighborsByAttr(dbContext.getGremlinDb().g(),
                value.getNode().id(), this.attrOverEdge);

        boolean pathWorkWell = false;
        boolean isCgEquals = true;
        ArrayList<Pair<Vertex, Edge>> cgList = new ArrayList<>();
        // dfg or cfg
        ArrayList<Pair<Vertex, Edge>> otherList = new ArrayList<>();
        for (Pair<Vertex, Edge> neighbor : neighbors) {
            Edge neighborEdge = neighbor.getValue1();
            String label = neighborEdge.label();

//            if ("cg".endsWith(label)) {
//                cgList.add(neighbor);
//            } else {
//                otherList.add(neighbor);
//            }
            otherList.add(neighbor);
        }

        boolean isDfg = this.strategy.equals(VertexFlowStrategies.DFG);
        if (isDfg && !checkEdgeLeft(otherList) || !isDfg && !checkEdgeLeft(neighbors)) {
            streams.add(new FlowStream(stack));
            onExit(value, edge);
            return true;
        }

        if (isDfg) {
            for (Pair<Vertex, Edge> cgPair : cgList) {
                Object cgId = cgPair.getValue0().id();
                boolean flag = false;
                // if there is node that do need dfg=cg
                for (Pair<Vertex, Edge> otherPair : otherList) {
                    Object otherId = otherPair.getValue0().id();
                    if (otherId.equals(cgId)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    isCgEquals = false;
                    break;
                }
            }
        }

        // cross function
        if (isDfg && !cgList.isEmpty() && isCgEquals) {
            // return
            if (value instanceof CxxFunctionExit) {
                pathWorkWell = handleCrossFunction(pathWorkWell, otherList, depths);
            } else {
                // call
                for (var neighbor : otherList) {
                    var neighborValue = helper.toValue(neighbor.getValue0());
                    var neighborEdge = neighbor.getValue1();
                    lastCgCaller.push(new Pair<>(value.getNode().id(), visited.size()));
                    stack.push(Pair.with(neighborValue, neighborEdge));
                    pathWorkWell |= dfs(neighborValue, neighborEdge, depths + 1);
                    stack.pop();
                    lastCgCaller.pop();
                }
            }
            onExit(value, edge);
            return pathWorkWell;
        }

        // 1. not dfg -> cfg + cg, 此时 isCgEquals 无用
        // 2. dfg with no cg, no use
        if (!(value instanceof CxxFunctionExit) && isCgEquals) {
            for (var neighbor : cgList) {
                var neighborValue = helper.toValue(neighbor.getValue0());
                var neighborEdge = neighbor.getValue1();
                if (visited.contains(neighborEdge.id())) {
                    continue;
                }
                // for recursive call, we need to judge
                boolean flag = true;
                for (Pair<Long, Integer> objects : lastCgCaller) {
                    if (objects.getValue0().equals(value.getNode().id())) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    continue;
                }
                lastCgCaller.push(new Pair<>(value.getNode().id(), visited.size()));
                stack.push(Pair.with(neighborValue, neighborEdge));
                pathWorkWell |= dfs(neighborValue, neighborEdge, depths + 1);
                stack.pop();
                lastCgCaller.pop();
            }
        } else {
            // 1. just dfg 且不是 functionExit -> 空
            // dfg 和 cg equals，表示必是跨函数 dfg
            // 即 dfg 在某些情况下表征了 cg 的某些能力
            // 2. functionExit 的 cfg + cg
            if (isCgEquals) {
                pathWorkWell = handleCrossFunction(pathWorkWell, cgList, depths + 1);
            }
        }

        // it means no cg works well
        // so we should use dfg(inner function) cfg directly
        if (!pathWorkWell) {
            for (var neighbor : otherList) {
                var neighborValue = helper.toValue(neighbor.getValue0());
                var neighborEdge = neighbor.getValue1();
                stack.push(Pair.with(neighborValue, neighborEdge));
                pathWorkWell |= dfs(neighborValue, neighborEdge, depths + 1);
                stack.pop();
            }
        }
        onExit(value, edge);
        // it means the path is work well
        return pathWorkWell;
    }

    private boolean handleCrossFunction(boolean pathWorkWell, ArrayList<Pair<Vertex, Edge>> cgList, int depth) {
        if (!lastCgCaller.isEmpty()) {
            boolean flag = false;
            for (var neighbor : cgList) {
                var neighborValue = helper.toValue(neighbor.getValue0());
                var neighborEdge = neighbor.getValue1();
                Pair<Long, Integer> peek = lastCgCaller.peek();
                // the same node, call-in + call-out
                if (peek.getValue0() == neighborValue.getNode().id()) {
                    stack.push(Pair.with(neighborValue, neighborEdge));

                    // cg call should not be clear
                    Stack<Object> objects = new Stack<>();
                    for (Object object : this.visited) {
                        objects.push(object);
                    }
                    onExitFunctionCall(peek.getValue1() + 1);
                    pathWorkWell |= dfs(neighborValue, neighborEdge, depth + 1);
                    this.visited.clear();
                    for (Object object : objects) {
                        this.visited.push(object);
                    }
                    stack.pop();

                    flag = true;
                    break;
                }
            }
            if (!flag) {
                for (var neighbor : cgList) {
                    var neighborValue = helper.toValue(neighbor.getValue0());
                    var neighborEdge = neighbor.getValue1();

                    // source inside the callee-function, sink outside
                    lastCgCaller.push(new Pair<>(neighborValue.getNode().id(), visited.size()));
                    stack.push(Pair.with(neighborValue, neighborEdge));
                    pathWorkWell |= dfs(neighborValue, neighborEdge, depth + 1);
                    stack.pop();
                    lastCgCaller.pop();
                }
            }
        } else {
            for (var neighbor : cgList) {
                var neighborValue = helper.toValue(neighbor.getValue0());
                var neighborEdge = neighbor.getValue1();

                // source inside the callee-function, sink outside
                lastCgCaller.push(new Pair<>(neighborValue.getNode().id(), visited.size()));
                stack.push(Pair.with(neighborValue, neighborEdge));
                pathWorkWell |= dfs(neighborValue, neighborEdge, depth + 1);
                stack.pop();
                lastCgCaller.pop();
            }
        }
        return pathWorkWell;
    }

    protected boolean onExit(Value value, Edge edge) {
        return true;
    }

    protected boolean onExitFunctionCall(int idx) {
        return true;
    }
}