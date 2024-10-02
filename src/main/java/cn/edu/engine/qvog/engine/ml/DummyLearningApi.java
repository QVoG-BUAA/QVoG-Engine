package cn.edu.engine.qvog.engine.ml;

import cn.edu.engine.qvog.engine.helper.Tuple;

import java.util.List;

public class DummyLearningApi implements ILearningApi {
    DummyLearningApi() {}

    @Override
    public List<Tuple<String, PredictTypes>> predict(List<String> values) {
        throw new UnsupportedOperationException("Machine learning not enabled.");
    }

    @Override
    public boolean match(String source, String sink) {
        throw new UnsupportedOperationException("Machine learning not enabled.");
    }
}
