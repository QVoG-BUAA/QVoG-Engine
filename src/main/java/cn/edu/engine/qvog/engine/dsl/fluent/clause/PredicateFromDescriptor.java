package cn.edu.engine.qvog.engine.dsl.fluent.clause;

import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.dsl.data.DataTable;
import cn.edu.engine.qvog.engine.dsl.data.ITable;
import cn.edu.engine.qvog.engine.dsl.data.PredicateColumn;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class PredicateFromDescriptor extends BaseFromDescriptor {
    private final IValuePredicate predicate;

    PredicateFromDescriptor(String alias, IValuePredicate predicate) {
        super(alias);
        this.predicate = predicate;
    }

    /**
     * By default, {@link DataTable} will add {@link cn.edu.engine.qvog.engine.dsl.data.DataColumn},
     * so, here we must manually construct a predicate column for it.
     */
    @Override
    public ITable fetchData(IDbContext dbContext) {
        return DataTable.builder().withName(alias)
                .withColumn(PredicateColumn.builder().withName(alias).withPredicate(predicate).build())
                .build();
    }
}
