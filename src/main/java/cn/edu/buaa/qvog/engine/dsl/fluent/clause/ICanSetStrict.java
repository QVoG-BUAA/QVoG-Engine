package cn.edu.buaa.qvog.engine.dsl.fluent.clause;

public interface ICanSetStrict extends ICanSetCacheTag {
    default ICanSetCacheTag isStrict() {
        return isStrict(true);
    }

    ICanSetCacheTag isStrict(boolean strict);
}
