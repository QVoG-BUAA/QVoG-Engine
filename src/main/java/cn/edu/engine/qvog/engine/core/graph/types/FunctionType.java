package cn.edu.engine.qvog.engine.core.graph.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a function type. For example, {@code int(int, long)}.
 * <p>
 * FunctionType includes the return type and the parameter types.
 * It is a composite type, thus it doesn't have a name. Instead, its
 * name comes from its subtypes.
 */
public final class FunctionType extends Type {
    private final Type returnType;
    private final List<Type> parameterTypes;

    public FunctionType(Type returnType, Type... parameterTypes) {
        super(composeName(returnType, List.of(parameterTypes)));
        this.returnType = returnType;
        this.parameterTypes = new ArrayList<>(List.of(parameterTypes));
    }

    private static String composeName(Type returnType, List<Type> parameterTypes) {
        return returnType.getName() +
                '(' +
                String.join(", ", parameterTypes.stream().map(Type::getName).toList()) +
                ')';
    }

    public FunctionType() {
        this(new VoidType());
    }

    public FunctionType(Type returnType) {
        this(returnType, new ArrayList<>());
    }

    public FunctionType(Type returnType, List<Type> parameterTypes) {
        super(composeName(returnType, parameterTypes));
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<Type> getParameterTypes() {
        return parameterTypes;
    }

    /**
     * Get the number of parameters.
     *
     * @return The number of parameters.
     */
    public int getParameterCount() {
        return parameterTypes.size();
    }

    /**
     * Get the type of the parameter at the given index.
     *
     * @param index The 0-based index of the parameter.
     * @return The type of the parameter.
     */
    public Type getParameterType(int index) {
        return parameterTypes.get(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FunctionType other) {
            if (!returnType.equals(other.returnType)) {
                return false;
            }
            return parameterTypes.equals(other.parameterTypes);
        }
        return false;
    }

    /**
     * A utility builder for {@link FunctionType}.
     */
    public static class Builder {
        private final List<Type> parameterTypes = new ArrayList<>();
        private Type returnType;

        /**
         * Prohibit direct instantiation.
         */
        private Builder() {}

        public Builder returnType(Type returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder addParameterType(Type parameterType) {
            parameterTypes.add(parameterType);
            return this;
        }

        public FunctionType build() {
            return new FunctionType(returnType, parameterTypes);
        }
    }
}
