package cn.edu.buaa.qvog.engine.dsl.fluent.clause;

import cn.edu.buaa.qvog.engine.ml.PredictTypes;

public interface ICanSetPredictType {
    ICanSetCacheTag is(PredictTypes type);
}
