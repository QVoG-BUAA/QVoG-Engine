package cn.edu.engine.qvog.engine.dsl.fluent.clause;

import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.dsl.data.ITable;

/**
 * From descriptor is used to describe from clause in DSL.
 * There are essentially two types of from clause:
 * 1. Fetch data from a table.
 * 2. Represent an object.
 * To make them consistent, we merge their interfaces into one.
 * This may result in some unavailable methods, leave it as technical debt. :P
 */
public interface IFromDescriptor {
    /**
     * Get the alias of the descriptor.
     *
     * @return Alias.
     */
    String getAlias();

    /**
     * Get the cache tag of the descriptor.
     * Used for from clause with data.
     *
     * @return Cache tag. null if not set.
     * @throws UnsupportedOperationException If the descriptor is not a data descriptor.
     */
    default String getCacheTag() {
        throw new UnsupportedOperationException("This method is not supported for " + this.getClass().getSimpleName());
    }

    /**
     * Fetch data from the database.
     *
     * @param dbContext Database context.
     * @return The fetched data as a table.
     * @throws UnsupportedOperationException If the descriptor is not a data descriptor.
     */
    default ITable fetchData(IDbContext dbContext) {
        throw new UnsupportedOperationException("This method is not supported for " + this.getClass().getSimpleName());
    }
}
