package cn.edu.buaa.qvog.engine.language.python;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.buaa.qvog.engine.core.ioc.Environment;
import cn.edu.buaa.qvog.engine.dsl.BaseQuery;
import cn.edu.buaa.qvog.engine.language.python.factory.PythonValueHandlerProvider;

public abstract class PythonQuery extends BaseQuery {
    static {
        Environment.getInstance().getServiceProvider()
                .bind(IValueHandlerProvider.class, PythonValueHandlerProvider.class);
    }
}
