package cn.edu.engine.qvog.engine.dsl.lib.flow.strategy;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.javatuples.Pair;

import java.util.List;

public interface IVertexFlowStrategy {
    /**
     * Get the neighbors of a vertex.
     *
     * @param g  The graph traversal source.
     * @param id The id of the vertex.
     * @return The neighbors of the vertex.
     */
    List<Pair<Vertex, Edge>> getNeighbors(GraphTraversalSource g, Long id);

    List<Pair<Vertex, Edge>> getNeighborsByAttr(GraphTraversalSource g, Long id, List<String> attrs);
}
