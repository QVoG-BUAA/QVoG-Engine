package cn.edu.buaa.qvog.engine.dsl.fluent.query;

import cn.edu.buaa.qvog.engine.dsl.lib.format.IFormatter;

public interface ICanConfigure {
    InitialQuery addFormatter(String name, IFormatter formatter);
}
