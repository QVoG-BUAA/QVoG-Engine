package cn.edu.engine.qvog.engine.dsl.fluent.flow;

public interface ICanSetFlowAlias {
    ICanBuildFlowPredicate as(String alias);
}
