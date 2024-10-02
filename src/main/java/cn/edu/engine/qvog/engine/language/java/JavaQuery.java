package cn.edu.engine.qvog.engine.language.java;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.dsl.BaseQuery;
import cn.edu.engine.qvog.engine.language.java.factory.JavaValueHandlerProvider;

public abstract class JavaQuery extends BaseQuery {
    static {
        Environment.getInstance().getServiceProvider()
                .bind(IValueHandlerProvider.class, JavaValueHandlerProvider.class);
    }
}
