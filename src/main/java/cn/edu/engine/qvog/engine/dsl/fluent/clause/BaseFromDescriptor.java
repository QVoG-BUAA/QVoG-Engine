package cn.edu.engine.qvog.engine.dsl.fluent.clause;

public abstract class BaseFromDescriptor implements IFromDescriptor {
    protected final String alias;

    protected BaseFromDescriptor(String alias) {
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return alias;
    }
}
