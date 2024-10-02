package cn.edu.engine.qvog.engine.dsl.data;

public abstract class BaseColumn implements IColumn {
    protected String name;

    @Override
    public String name() {
        return name;
    }
}
