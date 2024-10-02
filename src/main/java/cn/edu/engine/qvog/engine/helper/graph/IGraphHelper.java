package cn.edu.engine.qvog.engine.helper.graph;

import cn.edu.engine.qvog.engine.core.graph.CodeNode;
import cn.edu.engine.qvog.engine.core.graph.values.Value;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public interface IGraphHelper {
    default Long getId(Vertex vertex) {
        return (Long) vertex.id();
    }

    default Long getId(Edge edge) {
        return (Long) edge.id();
    }

    default String getProperty(Edge edge, String key) {
        return edge.property(key).value().toString();
    }

    default String getProperty(Vertex vertex, String key) {
        return vertex.property(key).value().toString();
    }

    default String getBriefDescription(Vertex vertex) {
        return getBriefDescription(toCodeNode(vertex));
    }

    default String getBriefDescription(CodeNode node) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(node.filename())
                .append(":").append(node.lineNumber()).append(") ");
        String code = node.code();
        if (code.length() > 50) {
            builder.append(code, 0, 50).append("...");
        } else {
            builder.append(code);
        }
        return builder.toString();
    }

    CodeNode toCodeNode(Vertex vertex);

    default boolean isCodeVertex(Vertex vertex) {
        return vertex.label().equals("code");
    }

    default Value toValue(Vertex vertex) {
        return toCodeNode(vertex).value();
    }
}
