package cn.edu.engine.qvog.engine.core.graph.types;

public class NoneType extends Type{
    public static final NoneType DEFAULT = new NoneType("none");

    public NoneType(String name) {
        super(name);
    }

    public NoneType(String name, Origins origin) {
        super(name, origin);
    }

    public NoneType() {
        super("none");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NoneType;
    }
}
