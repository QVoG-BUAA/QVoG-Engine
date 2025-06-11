package cn.edu.engine.qvog.engine.core.graph.values;

import org.javatuples.Pair;

/**
 * cache speed label
 * @author milo on 3/12/2025
 */
public class CacheValue extends Value {
    private Value from;
    private Value next;

    public CacheValue(Value from, Value next) {
        this.from = from;
        this.next = next;
    }

    public Pair<Integer, Integer> getLinenoBetween() {
        return Pair.with(from.getNode().lineNumber(), next.getNode().lineNumber());
    }
}
