package cn.edu.engine.qvog.engine.dsl.fluent.flow;

import cn.edu.engine.qvog.engine.dsl.data.ITable;
import cn.edu.engine.qvog.engine.dsl.data.ITableSet;

import java.util.Collection;

public interface IFlowPredicate {
    Collection<String> getAffectedTableNames();

    ITable exists(ITableSet tables);
}
