package cn.edu.engine.qvog.engine.dsl.lib.format;

import cn.edu.engine.qvog.engine.dsl.lib.engine.QueryResult;
import cn.edu.engine.qvog.engine.helper.AnsiColors;

public class DefaultResultFormatter implements IResultFormatter {
    @Override
    public String format(QueryResult result) {
        return AnsiColors.ANSI_CYAN + "===== Executing " + result.name() + AnsiColors.ANSI_RESET
                + "\n" + result.result() + "\n"
                + AnsiColors.ANSI_CYAN + "===== Query executed in " + result.milliseconds() + "ms (" + result.milliseconds() * 1.0 / 1000 + "s)\n" + AnsiColors.ANSI_RESET;
    }

    @Override
    public String formatTotalTime(long milliseconds) {
        return AnsiColors.ANSI_GREEN + "===== Total execution time: " + milliseconds + "ms (" + milliseconds * 1.0 / 1000 + "s)" + AnsiColors.ANSI_RESET;
    }
}
