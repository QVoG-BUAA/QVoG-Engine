package cn.edu.engine.qvog.engine.core.graph;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Objects;

/**
 * Node represents a "code" node in the final graph.
 * <p>
 * It is converted from a {@link Vertex}
 * from the original graph.
 */
public final class CodeNode extends Node {
    private final CodeVertexProperty property;
    private final Value value;

    public CodeNode(Vertex vertex) {
        this(vertex, null, null);
    }

    public CodeNode(Vertex vertex, CodeVertexProperty property, Value value) {
        super(vertex);
        this.property = property;
        this.value = value;
        this.value.setNode(this);
    }

    public long id() {
        return property.id();
    }

    public String code() {
        return property.code();
    }

    public int lineNumber() {
        return property.lineNumber();
    }

    public String filename() {
        return property.filename();
    }

    public String functionDefName() {
        return property.functionDefName();
    }

    public String json() {
        return property.json();
    }

    public CodeVertexProperty property() {return property;}

    public Value value() {return value;}

    @Override
    public int hashCode() {
        return Objects.hash(vertex, property, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (CodeNode) obj;
        return Objects.equals(this.vertex, that.vertex) &&
                Objects.equals(this.property, that.property) &&
                Objects.equals(this.value, that.value);
    }
}
