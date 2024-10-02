package cn.edu.engine.qvog.engine.db.gremlin;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

/**
 * This interface is used to provide a wrapper for Gremlin database operations.
 */
public interface IGremlinDb {
    /**
     * Get the graph traversal source.
     *
     * @return The graph traversal source.
     */
    GraphTraversalSource g();

    /**
     * Get the graph traversal V().
     *
     * @return The graph traversal V().
     */
    GraphTraversal<Vertex, Vertex> V();

    /**
     * Get the graph traversal E().
     *
     * @return The graph traversal E().
     */
    GraphTraversal<Edge, Edge> E();

    /**
     * Close the context.
     *
     * @throws Exception The exception.
     */
    void close() throws Exception;
}
