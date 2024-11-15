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
        dfs(source, null);
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
        //只要输入有一个点不在已访问过的里面就返回真
        for (Pair<Vertex, Edge> neighbor : neighbors) {
            if (!visited.contains(neighbor.getValue1().id())) {
                return true;
            }
        }
        return false;
    }

    private boolean dfs(Value value, Edge edge) {
        if (!onEnter(value, edge)) {
            return false;
        }
        //这个可能是之后有继承这个类的会重写
        var neighbors = strategy.getNeighborsByAttr(dbContext.getGremlinDb().g(),
                value.getNode().id(), this.attrOverEdge);
        //可以发现这个函数有8个实现，在VertexFlowStrategies中
        //大致应该就是获得这个节点相邻的节点


        boolean pathWorkWell = false;
        //pathWorkWell表示到目前为止是否找到有效路径
        boolean isCgEquals = true;
        //isCgEquals代表cgList是否为otherList的子集
        ArrayList<Pair<Vertex, Edge>> cgList = new ArrayList<>();
        ArrayList<Pair<Vertex, Edge>> otherList = new ArrayList<>();
        for (Pair<Vertex, Edge> neighbor : neighbors) {
            Edge neighborEdge = neighbor.getValue1();
            //getValue1()就是获取Pair后一个，也就是Edge
            String label = neighborEdge.label();

            //分成cg和其他类型
            if ("cg".endsWith(label)) {
                cgList.add(neighbor);
            } else {
                otherList.add(neighbor);
            }
        }

        boolean isDfg = this.strategy.equals(VertexFlowStrategies.DFG);
        //判断是否是DFG策略
        //-----提出问题：什么是DFG策略？？
        //dfg+otherList全部访问过
        //不是dfg+neighbors全访问过
        if (isDfg && !checkEdgeLeft(otherList) || !isDfg && !checkEdgeLeft(neighbors)) {
            streams.add(new FlowStream(stack));
            //-----这个stream和stack分别代表什么？？？？
            onExit(value, edge);
            //-----这个可能是继承了dfsFlow的类会重写。这个具体也不太清楚是什么意思？？？
            return true;
            //返回true代表path work well，能走通
        }
        //条件1：是dfg
        //条件2：otherList中有未被访问过的节点
        if (isDfg) {
            for (Pair<Vertex, Edge> cgPair : cgList) {

                Object cgId = cgPair.getValue0().id();
                boolean flag = false;
                for (Pair<Vertex, Edge> otherPair : otherList) {
                    Object otherId = otherPair.getValue0().id();
                    if (otherId.equals(cgId)) {
                        flag = true;
                    }
                }
                //以上这段代码的作用就是对于cgList也就是CG边的某一个点，看看是否也在otherList中
                //要注意：我们上面将neighbors分成cgList和otherList，依据是边是否为cg。
                //但是也有可能从A到B有两条边，一条是cg，一条不是，所以cgList可能会与otherList中有重合
                if (!flag) {
                    //意思就是只要flag=false，有一个cgList中的点无法在otherList中找到
                    //那么就不是Equals
                    isCgEquals = false;
                    break;
                }
            }
        }
        //条件1：dfg
        // 条件2：cgList不为空，因为到目前为止没有对cgList有任何操作，
        // 所以实际上就是neighbors中至少有一,jij条边为cg
        //要进这个if，isCgList要为true，
        // 由上面可知isCgList=true一定不能走那个if中的break，只能循环全部完成后自然跳出循环。
        //条件三：cgList中每个的点都能在otherList中的找到
        if (isDfg && !cgList.isEmpty() && isCgEquals) {
            if (value instanceof CxxFunctionExit) {
                //这里的value就是这个函数一开始传入的点
                pathWorkWell = handleCrossFunction(pathWorkWell, otherList);
                //-----暂时不清楚这个函数作用
            } else {
                //------这块对应value不是CxxFunctionExit,那还能代表什么？？
                for (var neighbor : otherList) {
                    var neighborValue = helper.toValue(neighbor.getValue0());
                    var neighborEdge = neighbor.getValue1();
                    lastCgCaller.push(new Pair<>(value.getNode().id(), visited.size()));
                    //lastCgCaller用于记录最近访问的函数调用者
                    stack.push(Pair.with(neighborValue, neighborEdge));
                    //stack用于记录当前DFS遍历的路径上的节点和边
                    //lastCgCaller与stack的区别：
                    // lastCgCaller中存放的是传入这个函数的参数value的id是Long类型和visited.size
                    //stack则是dfs过程中，otherList走到了那个节点，哪个边
                    pathWorkWell |= dfs(neighborValue, neighborEdge);
                    stack.pop();
                    lastCgCaller.pop();
                    //不太清楚这个地方lastCgCaller和stack代表什么？
                }
            }
            onExit(value, edge);
            //-----也不清楚onExit函数的作用
            return pathWorkWell;
        }

        // 1. not dfg
        // 2. dfg with no cg
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
                pathWorkWell |= dfs(neighborValue, neighborEdge);
                stack.pop();
                lastCgCaller.pop();
                //这里就是递归遍历cgList
                //如果出现了lastCgCaller中有某个节点是当前dfs的输入节点，
                // 就会再对当前节点进行递归，因为这样会导致死循环Base
            }
        } else {
            if (isCgEquals) {
                pathWorkWell = handleCrossFunction(pathWorkWell, cgList);
                //-----还是得看一下这个handleCrossFunction
            }
        }

        // it means no cg works well
        if (!pathWorkWell) {
            for (var neighbor : otherList) {
                var neighborValue = helper.toValue(neighbor.getValue0());
                var neighborEdge = neighbor.getValue1();
                stack.push(Pair.with(neighborValue, neighborEdge));
                pathWorkWell |= dfs(neighborValue, neighborEdge);
                stack.pop();
            }
        }
        onExit(value, edge);
        // it means the path is work well
        return pathWorkWell;
    }

    private boolean handleCrossFunction(boolean pathWorkWell, ArrayList<Pair<Vertex, Edge>> cgList) {
        //lastCgCaller不为空.
        if (!lastCgCaller.isEmpty()) {
            boolean flag = false;
            for (var neighbor : cgList) {
                var neighborValue = helper.toValue(neighbor.getValue0());
                var neighborEdge = neighbor.getValue1();
                //取cgList中某个节点的value和edge
                Pair<Long, Integer> peek = lastCgCaller.peek();
                //查看栈顶元素
                if (peek.getValue0() == neighborValue.getNode().id()) {
                    //cgList中这个节点就是lastCgCaller的栈顶节点
                    stack.push(Pair.with(neighborValue, neighborEdge));
                    //也就是说当前的dfs传入的value在找到其neighbors后分成cgList和otherList
                    //然后这个cgList中某个节点就是lastCgCaller的栈顶节点
                    //lastCgCaller的栈顶节点意味着就是上一次dfs传入的value（dfs是递归嵌套的）

                    // cg call should not be clear
                    Stack<Object> objects = new Stack<>();
                    for (Object object : this.visited) {
                        objects.push(object);
                    }
                    //赋值一份已经遍历过的
                    onExitFunctionCall(peek.getValue1() + 1);
                    //-----这个可能也是之后会被继承然后重写的
                    pathWorkWell |= dfs(neighborValue, neighborEdge);
                    //这个递归之前为什么不往lastCgCaller中存
                    //因为此时开始的节点就已经是lastCgCaller栈顶的节点了
                    //继续递归dfs
                    this.visited.clear();
                    for (Object object : objects) {
                        this.visited.push(object);
                    }
                    //因为在dfs递归时visited可能会被改变，所以需要先复制一份然后再放回去
                    stack.pop();

                    flag = true;
                    break;
                }
            }
            //cgList中没有任何一个节点是lastCgCaller的栈顶节点
            if (!flag) {
                for (var neighbor : cgList) {
                    //遍历cgList
                    var neighborValue = helper.toValue(neighbor.getValue0());
                    var neighborEdge = neighbor.getValue1();

                    // source inside the callee-function, sink outside
                    lastCgCaller.push(new Pair<>(neighborValue.getNode().id(), visited.size()));
                    stack.push(Pair.with(neighborValue, neighborEdge));
                    pathWorkWell |= dfs(neighborValue, neighborEdge);
                    stack.pop();
                    lastCgCaller.pop();
                }
            }
        }
        //lastCgCaller为空
        else {
            for (var neighbor : cgList) {
                var neighborValue = helper.toValue(neighbor.getValue0());
                var neighborEdge = neighbor.getValue1();

                // source inside the callee-function, sink outside
                lastCgCaller.push(new Pair<>(neighborValue.getNode().id(), visited.size()));
                stack.push(Pair.with(neighborValue, neighborEdge));
                pathWorkWell |= dfs(neighborValue, neighborEdge);
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