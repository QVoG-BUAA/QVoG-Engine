package cn.edu.buaa.qvog.engine.language.java;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.buaa.qvog.engine.core.ioc.Environment;
import cn.edu.buaa.qvog.engine.dsl.BaseQuery;
import cn.edu.buaa.qvog.engine.language.java.factory.JavaValueHandlerProvider;

public abstract class JavaQuery extends BaseQuery {
    static {
        Environment.getInstance().getServiceProvider()
                .bind(IValueHandlerProvider.class, JavaValueHandlerProvider.class);
    }
}
