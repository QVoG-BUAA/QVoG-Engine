package cn.edu.buaa.qvog.engine.dsl.fluent.flow;

public interface ICanSetFlowAlias {
    ICanBuildFlowPredicate as(String alias);
}
