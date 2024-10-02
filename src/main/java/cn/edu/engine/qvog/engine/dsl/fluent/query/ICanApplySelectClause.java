package cn.edu.engine.qvog.engine.dsl.fluent.query;

public interface ICanApplySelectClause {
    /**
     * Select columns from the table. The query will then complete.
     *
     * @param columns Column names.
     * @return {@link CompleteQuery}
     */
    CompleteQuery select(String... names);
}
