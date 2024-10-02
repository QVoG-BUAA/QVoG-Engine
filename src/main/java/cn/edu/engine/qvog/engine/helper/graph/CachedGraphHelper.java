package cn.edu.engine.qvog.engine.helper.graph;

import cn.edu.engine.qvog.engine.core.graph.CodeNode;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.helper.cache.NodeCache;
import com.google.inject.Inject;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class CachedGraphHelper extends GraphHelper {
    private final NodeCache<CodeNode> cache = new NodeCache<>();

    @Inject
    public CachedGraphHelper(IValueFactory factory) {
        super(factory);
    }

    @Override
    public CodeNode toCodeNode(Vertex vertex) {
        CodeNode node = cache.get(vertex);
        if (node == null) {
            node = super.toCodeNode(vertex);
            cache.put(vertex, node);
        }
        return node;
    }
}
