package cn.edu.engine.qvog.engine.core.graph.types;

public class StringType extends Type {
    public static final StringType DEFAULT = new StringType("string");

    public StringType(String name, Origins origin) {
        super(name, origin);
    }

    public StringType(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringType;
    }
}
