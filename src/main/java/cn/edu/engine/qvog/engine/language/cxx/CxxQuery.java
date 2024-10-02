package cn.edu.engine.qvog.engine.language.cxx;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.dsl.BaseQuery;
import cn.edu.engine.qvog.engine.language.cxx.factory.CxxValueHandlerProvider;

public abstract class CxxQuery extends BaseQuery {
    static {
        Environment.getInstance().getServiceProvider()
                .bind(IValueHandlerProvider.class, CxxValueHandlerProvider.class);
    }
}
