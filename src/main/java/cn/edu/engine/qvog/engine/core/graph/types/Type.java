package cn.edu.engine.qvog.engine.core.graph.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * Base class for all types.
 */
public abstract class Type implements Serializable {
    private final Origins origin;
    private final String name;

    /**
     * If not specified, the origin is {@link Origins#Resolved}.
     */
    public Type(String name) {
        this(name, Origins.Resolved);
    }

    public Type(String name, Origins origin) {
        this.origin = origin;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Origins getOrigin() {
        return origin;
    }

    /**
     * @return Whether the type is resolved.
     */
    public final boolean isResolved() {
        return origin == Origins.Resolved;
    }

    /**
     * @return Whether the type is inferred.
     */
    public final boolean isInferred() {
        return origin == Origins.Inferred;
    }

    /**
     * @return Whether the type is unknown.
     */
    public final boolean isUnknown() {
        return origin == Origins.Unknown;
    }

    public String dumps() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    /**
     * A utility method to cast a type to a specific type.
     *
     * @return The cast type.
     * @throws RuntimeException If the cast fails.
     */
    public <Ty> Ty as() {
        try {
            return (Ty) this;
        } catch (ClassCastException e) {
            throw new RuntimeException("Cannot cast " + this.getClass().getSimpleName() + " to " + e.getMessage());
        }
    }

    /**
     * Origins of a type.
     */
    public enum Origins {
        /**
         * The type is explicitly defined in the source code.
         */
        Resolved,

        /**
         * The type is inferred from the context.
         * <p>
         * TODO: This is not implemented yet.
         */
        Inferred,

        /**
         * The type is unknown, will only appear in {@link UnknownType}.
         */
        Unknown
    }
}
