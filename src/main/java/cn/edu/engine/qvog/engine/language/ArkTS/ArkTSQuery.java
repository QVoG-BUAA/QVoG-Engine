package cn.edu.engine.qvog.engine.language.ArkTS;

import cn.edu.engine.qvog.engine.dsl.BaseQuery;
import cn.edu.engine.qvog.engine.language.ArkTS.factory.ArkTSValueHandlerProvider;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.engine.qvog.engine.core.ioc.Environment;

public abstract class ArkTSQuery extends BaseQuery {
    static {
        Environment.getInstance().getServiceProvider()
                .bind(IValueHandlerProvider.class, ArkTSValueHandlerProvider.class);
    }
}
//这个抽象类就是为了将当前运行环境中的IValueHandlerProvider于其具体的实现类绑定
//静态代码块在这个类被加载时执行
