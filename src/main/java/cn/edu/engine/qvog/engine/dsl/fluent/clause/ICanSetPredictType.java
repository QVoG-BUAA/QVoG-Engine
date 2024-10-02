package cn.edu.engine.qvog.engine.dsl.fluent.clause;

import cn.edu.engine.qvog.engine.ml.PredictTypes;

public interface ICanSetPredictType {
    ICanSetCacheTag is(PredictTypes type);
}
