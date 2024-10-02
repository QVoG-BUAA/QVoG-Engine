package cn.edu.engine.qvog.engine.core.graph.factory;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import org.json.simple.JSONObject;

/**
 * Handler for creating AST nodes of a specific type.
 * Since AST node may nest, the handler may need to call
 * the factory to create child nodes.
 */
public interface IValueHandler<TValue extends Value> {
    /**
     * Create an AST node of a specific type.
     *
     * @param json    The JSON object to create the AST node from.
     * @param factory The factory to create child nodes.
     * @return The AST node.
     */
    TValue build(JSONObject json, IValueFactory factory);
}
