package cn.edu.buaa.qvog.engine.dsl.data;

import cn.edu.buaa.qvog.engine.core.graph.values.Value;

import java.util.function.Function;

/**
 * Provide index function for columns.
 */
public class IndexProvider {
    private IndexProvider() {}

    public static Function<Object, Object> getIndexFunction(IndexTypes type) {
        return switch (type) {
            case NoIndex -> null;
            case ValueIndex -> Value.keyMapper();
        };
    }
}
