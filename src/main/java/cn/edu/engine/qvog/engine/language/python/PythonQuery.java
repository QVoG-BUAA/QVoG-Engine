package cn.edu.engine.qvog.engine.language.python;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.dsl.BaseQuery;
import cn.edu.engine.qvog.engine.language.python.factory.PythonValueHandlerProvider;

public abstract class PythonQuery extends BaseQuery {
    static {
        Environment.getInstance().getServiceProvider()
                .bind(IValueHandlerProvider.class, PythonValueHandlerProvider.class);
    }
}
