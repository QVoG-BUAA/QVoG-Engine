package cn.edu.engine.qvog.engine.dsl.lib.flow.strategy;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;

public class VertexFlowStrategies {
    public static final String DECL2USAGE = "decl2usage";

    public static IVertexFlowStrategy DECL = new DeclFlow();
    public static IVertexFlowStrategy DFG = new DfgFlow();
    public static IVertexFlowStrategy DFG_WITH_ATTR = new DfgWithAttrFlow();
    public static IVertexFlowStrategy DFG_REVERSE = new DfgReverseFlow();
    public static IVertexFlowStrategy CFG = new CfgFlow();
    public static IVertexFlowStrategy CFG_REVERSE = new CfgReverseFlow();

    public static IVertexFlowStrategy CFG_CG = new CfgCgFlow();
    public static IVertexFlowStrategy CFG_CG_REVERSE = new CfgCgReverseFlow();

    private VertexFlowStrategies() {
    }

    private static class DeclFlow implements IVertexFlowStrategy {
        @Override
        public List<Pair<Vertex, Edge>> getNeighbors(GraphTraversalSource g, Long id) {
            return g.V(id).outE("dfg").has("description", DECL2USAGE).dedup()
                    .project("e", "v")
                    .by().by(inV()).toStream().map(m -> new Pair<>(
                            (Vertex) m.get("v"), (Edge) m.get("e"))).toList();
        }

        @Override
        public List<Pair<Vertex, Edge>> getNeighborsByAttr(GraphTraversalSource g, Long id, List<String> attrs) {
            return getNeighbors(g, id);
        }
    }

    private static class DfgFlow implements IVertexFlowStrategy {
        @Override
        public List<Pair<Vertex, Edge>> getNeighbors(GraphTraversalSource g, Long id) {
            List<Pair<Vertex, Edge>> dfgList = g.V(id).inE("dfg").dedup()
                    .project("e", "v")
                    .by().by(outV()).toStream().map(m -> new Pair<>(
                            (Vertex) m.get("v"), (Edge) m.get("e"))).toList();
            List<Pair<Vertex, Edge>> cgList = g.V(id).outE("cg").dedup()
                    .project("e", "v")
                    .by().by(inV()).toStream().map(m -> new Pair<>(
                            (Vertex) m.get("v"), (Edge) m.get("e"))).toList();
            ArrayList<Pair<Vertex, Edge>> ans = new ArrayList<>(dfgList);
            ans.addAll(cgList);
            return ans;
        }

        @Override
        public List<Pair<Vertex, Edge>> getNeighborsByAttr(GraphTraversalSource g, Long id, List<String> attrs) {
            return getNeighbors(g, id);
        }
    }

    private static class DfgWithAttrFlow implements IVertexFlowStrategy {
        @Override
        public List<Pair<Vertex, Edge>> getNeighbors(GraphTraversalSource g, Long id) {
            return null;
        }

        @Override
        public List<Pair<Vertex, Edge>> getNeighborsByAttr(GraphTraversalSource g, Long id, List<String> attrs) {
            return g.V(id).inE("dfg")
                    .or(
                            has("description", DECL2USAGE),
                            has("description", P.within(attrs)))
                    .dedup()
                    .project("e", "v")
                    .by().by(outV()).toStream().map(m -> new Pair<>(
                            (Vertex) m.get("v"), (Edge) m.get("e"))).toList();
        }
    }

    private static class DfgReverseFlow implements IVertexFlowStrategy {
        @Override
        public List<Pair<Vertex, Edge>> getNeighbors(GraphTraversalSource g, Long id) {
            return g.V(id).outE("dfg").dedup()
                    .project("e", "v")
                    .by().by(inV()).toStream().map(m -> new Pair<>(
                            (Vertex) m.get("v"), (Edge) m.get("e"))).toList();
        }

        @Override
        public List<Pair<Vertex, Edge>> getNeighborsByAttr(GraphTraversalSource g, Long id, List<String> attrs) {
            return getNeighbors(g, id);
        }
    }

    private static class CfgFlow implements IVertexFlowStrategy {
        @Override
        public List<Pair<Vertex, Edge>> getNeighbors(GraphTraversalSource g, Long id) {
            return g.V(id).outE("cfg").dedup()
                    .project("e", "v")
                    .by().by(inV()).toStream().map(m -> new Pair<>(
                            (Vertex) m.get("v"), (Edge) m.get("e"))).toList();
        }

        @Override
        public List<Pair<Vertex, Edge>> getNeighborsByAttr(GraphTraversalSource g, Long id, List<String> attrs) {
            return getNeighbors(g, id);
        }
    }

    private static class CfgCgFlow implements IVertexFlowStrategy {
        @Override
        public List<Pair<Vertex, Edge>> getNeighbors(GraphTraversalSource g, Long id) {
            return g.V(id).outE("cfg", "cg").dedup()
                    .project("e", "v")
                    .by().by(inV()).toStream().map(m -> new Pair<>(
                            (Vertex) m.get("v"), (Edge) m.get("e"))).toList();
        }

        @Override
        public List<Pair<Vertex, Edge>> getNeighborsByAttr(GraphTraversalSource g, Long id, List<String> attrs) {
            return getNeighbors(g, id);
        }
    }

    private static class CfgCgReverseFlow implements IVertexFlowStrategy {
        @Override
        public List<Pair<Vertex, Edge>> getNeighbors(GraphTraversalSource g, Long id) {
            return g.V(id).inE("cfg", "cg").dedup()
                    .project("e", "v")
                    .by().by(outV()).toStream().map(m -> new Pair<>(
                            (Vertex) m.get("v"), (Edge) m.get("e"))).toList();
        }

        @Override
        public List<Pair<Vertex, Edge>> getNeighborsByAttr(GraphTraversalSource g, Long id, List<String> attrs) {
            return getNeighbors(g, id);
        }
    }

    private static class CfgReverseFlow implements IVertexFlowStrategy {
        @Override
        public List<Pair<Vertex, Edge>> getNeighbors(GraphTraversalSource g, Long id) {
            return g.V(id).inE("cfg").dedup()
                    .project("e", "v")
                    .by().by(outV()).toStream().map(m -> new Pair<>(
                            (Vertex) m.get("v"), (Edge) m.get("e"))).toList();
        }

        @Override
        public List<Pair<Vertex, Edge>> getNeighborsByAttr(GraphTraversalSource g, Long id, List<String> attrs) {
            return getNeighbors(g, id);
        }
    }
}