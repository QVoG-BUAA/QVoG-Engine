package cn.edu.engine.qvog.engine.core.ioc;


import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.helper.graph.IGraphHelper;
import cn.edu.engine.qvog.engine.ml.ILearningApi;

/**
 * The global environment. This is a singleton class.
 * Using global variables is not a good practice, but there has to be
 * something that holds all the global state.
 */
public class Environment {
    private static final Environment instance = new Environment();
    private final ServiceProvider serviceProvider = new ServiceProvider();

    private Environment() {}

    public static Environment getInstance() {
        return instance;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public IGraphHelper getGraphHelper() {
        return get(IGraphHelper.class);
    }

    /**
     * Native method to get something in the environment.
     *
     * @param clazz The interface class.
     * @param <T>   Whatever.
     * @return The thing.
     */
    public <T> T get(Class<T> clazz) {
        return serviceProvider.get(clazz);
    }

    public IDbContext getDbContext() {
        return get(IDbContext.class);
    }

    public ILearningApi getLearningApi() {
        return get(ILearningApi.class);
    }
}
