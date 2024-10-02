package cn.edu.engine.qvog.engine.helper.cache;

import cn.edu.engine.qvog.engine.core.graph.CodeNode;
import cn.edu.engine.qvog.engine.core.graph.Node;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.HashMap;
import java.util.Map;

/**
 * Provide cache for JSON AST to {@link CodeNode}
 * conversion to avoid duplicate object creation.
 */
public class NodeCache<TNode extends Node> implements ICache<Vertex, TNode> {
    private final Map<Object, TNode> cache = new HashMap<>();

    @Override
    public void put(Vertex vertex, TNode node) {
        cache.put(vertex.id(), node);
    }

    @Override
    public TNode get(Vertex vertex) {
        return cache.get(vertex.id());
    }
}
