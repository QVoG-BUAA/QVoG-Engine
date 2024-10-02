package cn.edu.engine.qvog.engine.dsl.fluent.flow;

import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

public interface ICanSetFlowEntry extends ICanSetFlowAlias {
    ICanSetFlowEntry source(String tableName);

    ICanSetFlowEntry sink(String tableName);

    ICanSetFlowEntry barrier(String tableName);

    ICanSetFlowEntry addSysExitAsSink(Boolean addSysExit);

    ICanSetFlowEntry setFlowSensitive(Boolean flowSensitive);

    ICanSetFlowEntry addFunctionEntryAsSink(Boolean addEntryExit);

    ICanSetFlowEntry specialSink(IValuePredicate predicate);
}
