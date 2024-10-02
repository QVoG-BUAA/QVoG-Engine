package cn.edu.engine.qvog.engine.dsl.lib.format;

import cn.edu.engine.qvog.engine.dsl.lib.engine.QueryResult;

public interface IResultFormatter {
    String format(QueryResult result);

    String formatTotalTime(long milliseconds);
}
