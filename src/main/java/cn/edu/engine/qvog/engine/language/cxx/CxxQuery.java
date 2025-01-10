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
    //静态块只会被加载一次，哪怕有多个实例。
    //而且静态块的加载是在其他静态变量之前，比如static main
}
