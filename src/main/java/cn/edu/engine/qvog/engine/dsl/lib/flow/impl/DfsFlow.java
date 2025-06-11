package cn.edu.engine.qvog.engine.dsl.lib.flow.impl;

import cn.edu.engine.qvog.engine.core.graph.values.CacheValue;
import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.ForStatement;
import cn.edu.engine.qvog.engine.core.graph.values.statements.IfStatement;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.cxx.virtuals.CxxFunctionExit;
import cn.edu.engine.qvog.engine.dsl.lib.flow.IFlow;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.IVertexFlowStrategy;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.VertexFlowStrategies;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.javatuples.Pair;

import java.util.*;

import static cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.VertexFlowStrategies.CFG_CG;
import static cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.VertexFlowStrategies.CFG_CG_REVERSE;

/**
 * DFS flow implementation.
 */
public class DfsFlow extends BaseFlow {
    protected final Integer MAX_DEPTHS = 50;
    protected Boolean shouldTerminate = false;

    protected HashMap<Long, Boolean> visitedNodes = new HashMap<>();
    // a hashmap use node id and edge id to judge if the edge is visited
    protected HashMap<Pair<Long, Long>, Pair<Value, Integer>> cacheSpeed = new HashMap<>();

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
                if (value.getNode() != null) {
                    if (this.escapeFromBarrier.contains(value.getNode().id())) {
                        value.setCanEscapeFromBarrier(true);
                    }
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

    private boolean dfsSpeedUpCheck(Value value, Edge edge, int depths) {
        Long id = value.getNode().id();
        Boolean visited = visitedNodes.get(id);
        if (visited == null) {
            return false;
        }

        Pair<Long, Long> key = new Pair<>(id, (Long) edge.id());
        Pair<Value, Integer> res = cacheSpeed.get(key);
        if (res != null) {
            return true;
        }
        return false;
    }

    private boolean dfsSpeedUp(Value value, Edge edge, int depths) {
        // use the cacheSpeed to get to the destination node
        Long id = value.getNode().id();
        Long edgeId = (Long) edge.id();
        Pair<Long, Long> key = new Pair<>(id, edgeId);
        Pair<Value, Integer> pair = cacheSpeed.get(key);
        if (pair != null) {
            if (pair.getValue1() > MAX_DEPTHS) {
                return false;
            }
            // add a special label in stack to indicate the node using cacheSpeed
            CacheValue cacheValue = new CacheValue(value, pair.getValue0());
            cacheValue.setNode(value.getNode());
            stack.push(Pair.with(cacheValue, edge));
            boolean result = dfs(pair.getValue0(), edge, pair.getValue1());
            stack.pop();
            return result;
        }
        return false;
    }

    private boolean dfs(Value value, Edge edge, int depths) {
        if (!onEnter(value, edge)) {
            return false;
        }

        Long id = value.getNode().id();
        Boolean visited = visitedNodes.get(id);
        if (visited == null) {
            visitedNodes.put(id, Boolean.TRUE);

            boolean cfgCg = this.strategy.equals(CFG_CG);
            if (cfgCg) {
                // intra-control-flow cache? no, one node may have multiple control flows
                // should remember the edge-id

                // 1. if node, it has two outcome edge
                // if the node is `if`, all the nodes have one cfg edge in path can cache to it except category2

                // traverse to non-branch node, skill the between nodes.
                if (value instanceof IfStatement) {
                    // traverse the stack, if cacheSpeed is None, set it the current node
                    // the stack traverse should use the reverse order to get the diff of depths
                    for (int i = stack.size() - 2; i >= 1; i--) {
                        Pair<Value, Edge> pair = stack.get(i);
                        Long nodeId = pair.getValue0().getNode().id();
                        Long edgeId = (Long) pair.getValue1().id();
                        Pair<Long, Long> key = new Pair<>(nodeId, edgeId);

                        Value node = pair.getValue0();
                        long nodeID = node.getNode().id();
                        // using gremlin to check if the node has one cfg edge
                        var neighbors = CFG_CG.getNeighbors(dbContext.getGremlinDb().g(), nodeID);
                        if (cacheSpeed.get(key) == null &&
                                !(pair.getValue0() instanceof IfStatement) &&
                                neighbors.size() <= 1) {
                            cacheSpeed.put(key, Pair.with(value, depths + (stack.size() - i)));
                        } else {
                            break;
                        }
                    }
                }

                // 2. phi node, it has two income edge
                // we can do it by judge if the next node has two income cfg-cg edge
                // check the neighbors of this node, to see if some has two edge
                var neighbors = CFG_CG.getNeighbors(dbContext.getGremlinDb().g(), id);
                if (neighbors.size() == 1) {
                    for (Pair<Vertex, Edge> neighbor : neighbors) {
                        Long neighborId = (Long) neighbor.getValue0().id();
                        boolean flag = false;
                        var neighborNeighbors = CFG_CG_REVERSE.getNeighbors(dbContext.getGremlinDb().g(), neighborId);
                        if (neighborNeighbors.size() >= 2) {
                            flag = true;
                        }
                        if (flag) {
                            for (int i = stack.size() - 2; i >= 1; i--) {
                                Pair<Value, Edge> pair = stack.get(i);
                                Long nodeId = pair.getValue0().getNode().id();
                                Long edgeId = (Long) pair.getValue1().id();
                                Pair<Long, Long> key = new Pair<>(nodeId, edgeId);

                                Value node = pair.getValue0();
                                long nodeID = node.getNode().id();
                                // using gremlin to check if the node has one cfg edge
                                var neighbors2 = CFG_CG.getNeighbors(dbContext.getGremlinDb().g(), nodeID);
                                if (cacheSpeed.get(key) == null &&
                                        !(pair.getValue0() instanceof IfStatement) &&
                                        neighbors2.size() <= 1) {
                                    cacheSpeed.put(key, Pair.with(value, depths + (stack.size() - i)));
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // data-flow cache
            boolean isDfg = this.strategy.equals(VertexFlowStrategies.DFG);
            if (isDfg) {
                return false;
            }

            // we can only use cfg cache in cfg traverse
        }

        if (depths > MAX_DEPTHS) {
            shouldTerminate = true;
            streams.add(new FlowStream(stack));
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

            if ("cg".endsWith(label)) {
                cgList.add(neighbor);
            } else {
                otherList.add(neighbor);
            }
        }

        boolean isDfg = this.strategy.equals(VertexFlowStrategies.DFG);
        if (isDfg && !checkEdgeLeft(otherList) || !isDfg && !checkEdgeLeft(neighbors)) {
            streams.add(new FlowStream(stack));
            onExit(value, edge);
            return true;
        }

        if (shouldTerminate) {
            streams.add(new FlowStream(stack));
            return false;
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

        if (shouldTerminate) {
            streams.add(new FlowStream(stack));
            return false;
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
                    lastCgCaller.push(new Pair<>(value.getNode().id(), this.visited.size()));
                    stack.push(Pair.with(neighborValue, neighborEdge));
                    pathWorkWell |= dfs(neighborValue, neighborEdge, depths + 1);
                    stack.pop();
                    lastCgCaller.pop();
                    if (shouldTerminate) {
                        streams.add(new FlowStream(stack));
                        return false;
                    }
                }
            }
            onExit(value, edge);
            return pathWorkWell;
        }
        if (shouldTerminate) {
            streams.add(new FlowStream(stack));
            return false;
        }

        // 1. not dfg -> cfg + cg, 此时 isCgEquals 无用
        // 2. dfg with no cg, no use
        if (!(value instanceof CxxFunctionExit) && isCgEquals) {
            for (var neighbor : cgList) {
                var neighborValue = helper.toValue(neighbor.getValue0());
                var neighborEdge = neighbor.getValue1();
                if (this.visited.contains(neighborEdge.id())) {
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
                lastCgCaller.push(new Pair<>(value.getNode().id(), this.visited.size()));
                stack.push(Pair.with(neighborValue, neighborEdge));
                pathWorkWell |= dfs(neighborValue, neighborEdge, depths + 1);
                stack.pop();
                lastCgCaller.pop();
                if (shouldTerminate) {
                    streams.add(new FlowStream(stack));
                    return false;
                }
            }
        } else {
            // 1. just dfg 且不是 functionExit -> 空
            // dfg 和 cg equals，表示必是跨函数 dfg
            // 即 dfg 在某些情况下表征了 cg 的某些能力
            // 2. functionExit 的 cfg + cg
            if (isCgEquals) {
                pathWorkWell = handleCrossFunction(pathWorkWell, cgList, depths + 1);
            }
            if (shouldTerminate) {
                streams.add(new FlowStream(stack));
                return false;
            }
        }

        // it means no cg works well
        // so we should use dfg(inner function) cfg directly
        if (!pathWorkWell) {
            for (var neighbor : otherList) {
                var neighborValue = helper.toValue(neighbor.getValue0());
                var neighborEdge = neighbor.getValue1();

                // dfg may with cg, should not use
                if (!isDfg) {
                    // use cacheSpeed to speed up
                    if (dfsSpeedUpCheck(neighborValue, neighborEdge, depths)) {
                        pathWorkWell |= dfsSpeedUp(neighborValue, neighborEdge, depths);
                        continue;
                    }
                }

                stack.push(Pair.with(neighborValue, neighborEdge));
                pathWorkWell |= dfs(neighborValue, neighborEdge, depths + 1);
                stack.pop();
                if (shouldTerminate) {
                    streams.add(new FlowStream(stack));
                    return false;
                }
            }
        }
        if (shouldTerminate) {
            streams.add(new FlowStream(stack));
            return false;
        }
        onExit(value, edge);
        // it means the path is work well
        return pathWorkWell;
    }

    private boolean handleCrossFunction(boolean pathWorkWell, ArrayList<Pair<Vertex, Edge>> cgList, int depth) {
        if (depth > MAX_DEPTHS) {
            shouldTerminate = true;
            streams.add(new FlowStream(stack));
            return false;
        }
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
                if (shouldTerminate) {
                    streams.add(new FlowStream(stack));
                    return false;
                }
            }
            if (shouldTerminate) {
                streams.add(new FlowStream(stack));
                return false;
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
                    if (shouldTerminate) {
                        streams.add(new FlowStream(stack));
                        return false;
                    }
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
                if (shouldTerminate) {
                    streams.add(new FlowStream(stack));
                    return false;
                }
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