package cn.edu.engine.qvog.engine.dsl.lib.flow.strategy;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

/**
 * This is actually a visitor pattern.
 */
public interface IVertexFlowEvent {
    /**
     * Triggered when a vertex is visited.
     *
     * @param vertex The vertex to be visited.
     * @return Whether to continue visiting its neighbors.
     */
    default boolean onEnter(Vertex vertex) {
        return true;
    }

    /**
     * Triggered when visiting a vertex's neighbor.
     *
     * @param from The vertex being visited.
     * @param to   The vertex's neighbor to be visited.
     * @param edge
     * @return Whether to visit this neighbor.
     */
    default boolean onFlow(Vertex from, Vertex to, Edge edge) {
        return true;
    }


    /**
     * Triggered when a vertex's neighbors are all visited.
     *
     * @param vertex The vertex visited.
     * @return Whether to continue visiting its neighbors.
     */
    default boolean onExit(Vertex vertex) {
        return true;
    }
}
