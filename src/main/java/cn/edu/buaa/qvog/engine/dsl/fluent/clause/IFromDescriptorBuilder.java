package cn.edu.buaa.qvog.engine.dsl.fluent.clause;

import cn.edu.buaa.qvog.engine.dsl.lib.predicate.IValuePredicate;

public interface IFromDescriptorBuilder {
    ICanSetStrict ofType(Class<?> clazz);

    ICanSetCacheTag ofType(IValuePredicate predicate);

    ICanSetAlias ofPredicate(IValuePredicate predicate);

    ICanSetPredictType ofLearning();
}
