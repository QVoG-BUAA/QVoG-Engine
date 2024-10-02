package cn.edu.engine.qvog.engine.helper.graph;

import cn.edu.engine.qvog.engine.core.graph.CodeNode;
import cn.edu.engine.qvog.engine.core.graph.CodeVertexProperty;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.values.UnknownValue;
import cn.edu.engine.qvog.engine.core.graph.values.Value;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.tinkerpop.gremlin.structure.Vertex;

@Singleton
public class GraphHelper implements IGraphHelper {
    private final IValueFactory factory;

    @Inject
    public GraphHelper(IValueFactory factory) {
        this.factory = factory;
    }

    @Override
    public CodeNode toCodeNode(Vertex vertex) {
        try {
            String json = vertex.property("json").value().toString();

            CodeVertexProperty property;
            property = new CodeVertexProperty(
                    getId(vertex),
                    vertex.property("code").value().toString(),
                    vertex.<Integer>property("lineno").value(),
                    vertex.property("file").value().toString(),
                    "", // TODO: functionDefName
                    json
            );
            Value value = factory.build(json, UnknownValue.DEFAULT);
            return new CodeNode(vertex, property, value);
        } catch (Exception e) {
            // FIXME for debug
//            throw e;
            return new CodeNode(vertex, null, UnknownValue.DEFAULT);
        }
    }
}
