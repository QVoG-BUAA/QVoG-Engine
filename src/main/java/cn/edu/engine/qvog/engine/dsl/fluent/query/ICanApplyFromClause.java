package cn.edu.engine.qvog.engine.dsl.fluent.query;

import cn.edu.engine.qvog.engine.dsl.fluent.clause.ICanBuildFromDescriptor;
import cn.edu.engine.qvog.engine.dsl.fluent.clause.IFromDescriptorBuilder;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.ml.PredictTypes;

import java.util.function.Function;

public interface ICanApplyFromClause {
    /**
     * From clause that creates data table.
     * This is a shortcut for {@link #from(String, Class, boolean, String)}, with
     * strict match, and auto cache tag.
     *
     * @param alias The from alias.
     * @param clazz THe class of the value.
     * @return {@link SimpleQuery}
     */
    default SimpleQuery from(String alias, Class<?> clazz) {
        return from(alias, clazz, true, null);
    }

    /**
     * From clause that creates data table, storing the result into a cached table.
     *
     * @param alias    The from alias.
     * @param clazz    The class of the value.
     * @param strict   Whether strict match the class or not.
     * @param cacheTag Specify cache key. If null, will assign automatically.
     * @return {@link SimpleQuery}
     */
    default SimpleQuery from(String alias, Class<?> clazz, boolean strict, String cacheTag) {
        return from(builder -> builder.ofType(clazz).isStrict(strict).withCacheTag(cacheTag).as(alias));
    }

    /**
     * Apply additional from clause to the query.
     *
     * @param clause The from clause.
     * @return {@link SimpleQuery}
     */
    SimpleQuery from(Function<IFromDescriptorBuilder, ICanBuildFromDescriptor> clause);

    /**
     * From clause that creates a data table.
     * It is a shortcut for {@link #from(String, IValuePredicate, String)}, with auto
     * cache tag.
     *
     * @param alias     The from alias.
     * @param predicate The user defined value predicate.
     * @return {@link SimpleQuery}
     */
    default SimpleQuery from(String alias, IValuePredicate predicate) {
        return from(alias, predicate, null);
    }

    /**
     * From clause that creates data table. It will use a user specified predicate to
     * fetch the values.
     *
     * @param alias     The from alias.
     * @param predicate User defined value predicate.
     * @param cacheTag  Optional cache tag.
     * @return {@link SimpleQuery}
     */
    default SimpleQuery from(String alias, IValuePredicate predicate, String cacheTag) {
        return from(builder -> builder.ofType(predicate).withCacheTag(cacheTag).as(alias));
    }

    /**
     * From clause that create a Machine Learning enabled data table.
     *
     * @param alias The from alias.
     * @param type  The predict type.
     * @return {@link SimpleQuery}
     */
    default SimpleQuery from(String alias, PredictTypes type) {
        return from(builder -> builder.ofLearning().is(type).withCacheTag(null).as(alias));
    }

    default SimpleQuery from(String alias, PredictTypes type, String cacheTag) {
        return from(builder -> builder.ofLearning().is(type).withCacheTag(cacheTag).as(alias));
    }

    /**
     * This from will create a predicate table, witch stores no data to reduce memory cost.
     * It uses the predicate to tell whether it has the value or not.
     *
     * @param alias     The from alias.
     * @param predicate The predicate for value.
     * @return {@link SimpleQuery}
     */
    default SimpleQuery fromP(String alias, IValuePredicate predicate) {
        return from(builder -> builder.ofPredicate(predicate).as(alias));
    }
}
