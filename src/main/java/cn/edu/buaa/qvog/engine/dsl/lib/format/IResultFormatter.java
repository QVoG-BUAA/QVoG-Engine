package cn.edu.buaa.qvog.engine.dsl.lib.format;

import cn.edu.buaa.qvog.engine.dsl.lib.engine.QueryResult;

public interface IResultFormatter {
    String format(QueryResult result);

    String formatTotalTime(long milliseconds);
}
