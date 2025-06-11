package cn.edu.engine.qvog.engine.language.python.lib.predicate;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.ml.Location;
import cn.edu.engine.qvog.engine.ml.PredictTypes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LLMMatch implements IValuePredicate {

    private final ArrayList<Location> locations = new ArrayList<>();
    private final PredictTypes type;

    public LLMMatch(ArrayList<Location> locations, PredictTypes type) {
        this.locations.addAll(locations);
        this.type = type;
    }

    @Override
    public boolean test(Value value) {
        if (value.getNode() != null) {
            if (value.getNode().property() != null) {
                int lineno = value.getNode().lineNumber();
                String file = value.getNode().filename();
                for (Location location : locations) {
                    boolean lineMatch = location.getLineno() == lineno;
                    boolean typeMatch = location.getType() == type;

                    Path path1 = Paths.get(file);
                    Path path2 = Paths.get(location.getFile());
                    Path normalizedPath1 = path1.normalize();
                    Path normalizedPath2 = path2.normalize();
                    boolean fileMatch = normalizedPath1.equals(normalizedPath2);
//                    System.out.printf("location lineno = %s, node lineno = %s\n", location.getLineno(), lineno);
//                    System.out.printf("location type = %s, node type = %s\n", location.getType(), type);
//                    System.out.printf("lineMatch = %s, typeMatch = %s, fileMatch = %s\n\n", lineMatch, typeMatch, fileMatch);
                    if (lineMatch && typeMatch && fileMatch) {return true;}
                }
            }
        }
        return false;
    }
}
