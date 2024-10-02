package cn.edu.engine.qvog.engine.core.graph.values;

import cn.edu.engine.qvog.engine.core.graph.CodeNode;
import cn.edu.engine.qvog.engine.core.graph.CodeVertexProperty;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.IsUnknown;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.helper.NamingHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Value is the base class for all values in AST.
 * <p>
 * Simple value, which is not an instruction or a statement,
 * will directly inherit this class.
 */
public abstract class Value implements Serializable {
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    protected Boolean canEscapeFromBarrier = false;
    private Collection<Value> flattened;
    @JsonIgnore
    private CodeNode node;

    public static Function<Object, Object> keyMapper() {
        return o -> {
            if (o instanceof Value value) {
                return value.getNode().id();
            } else {
                throw new IllegalArgumentException("Cannot map key for " + o);
            }
        };
    }

    public CodeNode getNode() {
        return node;
    }

    /**
     * This should only be called by {@link CodeNode#CodeNode(Vertex, CodeVertexProperty, Value)}
     * after the value is set.
     *
     * @param node The node that contains this value.
     */
    public void setNode(CodeNode node) {
        if (node.value() != this) {
            throw new IllegalArgumentException("Node does not contain this value.");
        }

        this.node = node;
    }

    public Boolean getCanEscapeFromBarrier() {
        return canEscapeFromBarrier;
    }

    public void setCanEscapeFromBarrier(Boolean canEscapeFromBarrier) {
        this.canEscapeFromBarrier = canEscapeFromBarrier;
    }

    public boolean isUnknown() {
        return this instanceof IsUnknown;
    }

    /**
     * To stream with children filtered by predicate.
     *
     * @param predicate Predicate to filter the stream.
     * @return Stream of children.
     */
    public Stream<Value> toStream(IValuePredicate predicate) {
        return toStream().filter(predicate::test);
    }

    /**
     * Get all children of this value as a stream. This is actually
     * the flattened AST. Of course, itself will be included.
     * <p>
     * {@code addChildren} is a recursive function Call, we can
     * understand it by
     * {@link cn.edu.engine.qvog.engine.core.graph.values.statements.IfStatement#addChildren(Collection)}
     *
     * @return Stream of children.
     */
    public Stream<Value> toStream() {
        if (flattened == null) {
            flattened = new ArrayList<>();

            flattened.add(this);
            addChildren(flattened);
        }
        return flattened.stream();
    }

    /**
     * Add all children of this value to the stream.
     * Should be overridden by subclasses.
     *
     * @param children Stream of children.
     */
    public void addChildren(Collection<Value> children) {
    }

    @Override
    public String toString() {
        try {
            return dumps();
        } catch (JsonProcessingException e) {
            return NamingHelper.toReservedName(getClass().getSimpleName() + "$Error");
        }
    }

    public String dumps() throws JsonProcessingException {
        return this.getClass().getSimpleName() + ": " +
                mapper.writeValueAsString(this);
    }
}
