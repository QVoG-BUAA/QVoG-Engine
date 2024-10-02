package cn.edu.engine.qvog.engine.core.graph.factory;

/**
 * Provider of {@link IValueHandler} instances. Each language can have its own
 * implementation of this interface to provide the language-specific
 * AST nodes.
 * Well, I guess it can be declared as a functional interface?
 */
@FunctionalInterface
public interface IValueHandlerProvider {
    /**
     * Register handlers to the given factory.
     *
     * @param factory The factory to register handlers to.
     */
    void registerHandlers(ValueFactory factory);
}
