package cn.edu.engine.qvog.engine.language.ArkTS.lib;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

import java.util.ArrayList;

public class LineNumberMatch implements IValuePredicate {

    private final ArrayList<Integer> lineNumberArr = new ArrayList<>();

    public LineNumberMatch(int lineNumber) {
        this.lineNumberArr.add(lineNumber);
    }

    public LineNumberMatch(int[] lineNumberArr) {
        for (int lineNumber : lineNumberArr) {
            this.lineNumberArr.add(lineNumber);
        }
    }

    @Override
    public boolean test(Value value) {
        if (value.getNode() != null) {
            if (value.getNode().property() != null) {
                return lineNumberArr.contains(value.getNode().lineNumber());
            }
        }
        return false;
    }
}
