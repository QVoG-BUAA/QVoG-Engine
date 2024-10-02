package cn.edu.engine.qvog.engine.dsl.fluent.clause;

import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.ml.PredictTypes;

public class FromDescriptorBuilder implements
        IFromDescriptorBuilder,
        ICanSetStrict,
        ICanSetPredictType,
        ICanSetCacheTag,
        ICanSetAlias,
        ICanBuildFromDescriptor {

    /**
     * Record the type we are going to build.
     * 0: DataDescriptor with
     */
    private int choice;

    private String alias;
    private Class<?> clazz;
    private boolean strict = false;
    private IValuePredicate predicate;
    private String cacheTag;

    private PredictTypes type;

    public FromDescriptorBuilder() {
    }

    public static IFromDescriptorBuilder create() {
        return new FromDescriptorBuilder();
    }

    @Override
    public IFromDescriptor build() {
        return switch (choice) {
            case 0 -> new DataFromDescriptor(alias, clazz, strict, cacheTag);
            case 1 -> new DataFromDescriptor(alias, predicate, cacheTag);
            case 2 -> new PredicateFromDescriptor(alias, predicate);
            case 3 -> new LearningFromDescriptor(alias, type, cacheTag);
            default -> throw new IllegalStateException("Unexpected choice: " + choice);
        };
    }

    @Override
    public ICanSetStrict ofType(Class<?> clazz) {
        choice = 0;
        this.clazz = clazz;
        return this;
    }

    @Override
    public ICanSetCacheTag ofType(IValuePredicate predicate) {
        choice = 1;
        this.predicate = predicate;
        return this;
    }

    @Override
    public ICanSetAlias ofPredicate(IValuePredicate predicate) {
        choice = 2;
        this.predicate = predicate;
        return this;
    }

    @Override
    public ICanSetPredictType ofLearning() {
        choice = 3;
        return this;
    }

    @Override
    public ICanSetCacheTag isStrict(boolean strict) {
        this.strict = strict;
        return this;
    }

    @Override
    public ICanSetAlias withCacheTag(String cacheTag) {
        this.cacheTag = cacheTag;
        return this;
    }

    @Override
    public ICanBuildFromDescriptor as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public ICanSetCacheTag is(PredictTypes type) {
        this.type = type;
        return this;
    }
}
