package cn.edu.engine.qvog.engine.core.graph.values.declarations;

/**
 * Declaration of a function.
 * TODO: This class is not completed yet.
 */
public class FunctionDeclaration extends Declaration {
    private String name;

    public FunctionDeclaration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
