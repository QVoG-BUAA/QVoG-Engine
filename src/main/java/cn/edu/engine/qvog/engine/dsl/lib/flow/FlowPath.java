package cn.edu.engine.qvog.engine.dsl.lib.flow;

import cn.edu.engine.qvog.engine.core.graph.values.Value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FlowPath implements Cloneable {
    private final List<Value> path;

    public FlowPath() {
        path = new ArrayList<>();
    }

    private FlowPath(List<Value> path) {
        this.path = path;
    }

    public void add(Value value) {
        path.add(value);
    }

    public List<Value> getPath() {
        return path;
    }

    @Override
    public FlowPath clone() {
        return new FlowPath(new ArrayList<>(path));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        var it = iterator();
        if (it.hasNext()) {
            builder.append(it.next().getNode().lineNumber());
        }
        while (it.hasNext()) {
            builder.append(" -> ");
            builder.append(it.next().getNode().lineNumber());
        }
        return builder.toString();
    }

    public Iterator<Value> iterator() {
        return path.iterator();
    }
}
