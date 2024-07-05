package cn.edu.buaa.qvog.engine.ml;

import cn.edu.buaa.qvog.engine.helper.Tuple;

import java.util.List;

public interface ILearningApi {
    List<Tuple<String, PredictTypes>> predict(List<String> values);

    boolean match(String source, String sink);
}