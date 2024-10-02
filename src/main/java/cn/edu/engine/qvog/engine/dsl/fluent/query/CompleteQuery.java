package cn.edu.engine.qvog.engine.dsl.fluent.query;

import java.io.PrintStream;

public interface CompleteQuery {
    default IQueryDescriptor display(PrintStream output) {
        return display("console", output);
    }

    /**
     * Display the query result. This method will return the original
     * {@link IQueryDescriptor} object to chain another query.
     *
     * @param output The output stream.
     * @return {@link IQueryDescriptor}
     */
    IQueryDescriptor display(String style, PrintStream output);

    default IQueryDescriptor display() {
        return display("console", System.out);
    }
}
