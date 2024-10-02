package cn.edu.engine.qvog.engine.dsl.lib.flow.impl;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * FlowStream is an internal data structure to store the flow steps.
 * The outer world can only access it through the iterator.
 */
public class FlowStream {
    private final List<Pair<Value, Edge>> steps;

    FlowStream(Collection<Pair<Value, Edge>> steps) {
        this.steps = new ArrayList<>(steps);
    }

    FlowStream() {
        steps = new ArrayList<>();
    }

    void add(Pair<Value, Edge> step) {
        steps.add(step);
    }

    void addAll(Collection<Pair<Value, Edge>> steps) {
        this.steps.addAll(steps);
    }

    public Iterator<Pair<Value, Edge>> iterator() {
        return steps.iterator();
    }
}
