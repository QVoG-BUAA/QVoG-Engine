package cn.edu.engine.qvog.engine.core.graph.types;


/**
 * The void type.
 */
public final class VoidType extends Type {
    public VoidType() {
        this("void");
    }

    public VoidType(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VoidType;
    }
}
