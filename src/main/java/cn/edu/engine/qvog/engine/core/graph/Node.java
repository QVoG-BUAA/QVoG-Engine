package cn.edu.engine.qvog.engine.core.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.io.Serializable;

/**
 * Base class for all nodes in the graph, including:
 * <ul>
 *
 * </ul>
 */
public abstract class Node implements Serializable {
    protected Vertex vertex;

    public Node(Vertex vertex) {
        this.vertex = vertex;
    }

    public Vertex getVertex() {
        return vertex;
    }
}
