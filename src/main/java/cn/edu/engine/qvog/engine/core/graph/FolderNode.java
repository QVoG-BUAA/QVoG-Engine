package cn.edu.engine.qvog.engine.core.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;

public class FolderNode extends Node {
    private final String name;

    public FolderNode(Vertex vertex, String name) {
        super(vertex);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
