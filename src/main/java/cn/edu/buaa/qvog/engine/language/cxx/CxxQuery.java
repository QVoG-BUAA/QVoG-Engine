package cn.edu.buaa.qvog.engine.language.cxx;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.buaa.qvog.engine.core.ioc.Environment;
import cn.edu.buaa.qvog.engine.dsl.BaseQuery;
import cn.edu.buaa.qvog.engine.language.cxx.factory.CxxValueHandlerProvider;

public abstract class CxxQuery extends BaseQuery {
    static {
        Environment.getInstance().getServiceProvider()
                .bind(IValueHandlerProvider.class, CxxValueHandlerProvider.class);
    }
}
