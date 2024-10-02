package cn.edu.engine.qvog.engine.core.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * The IoC container for services.
 */
public class ServiceProvider {
    public static final ServiceProvider DEFAULT = new ServiceProvider();

    private final List<AbstractModule> modules = new ArrayList<>();
    private Injector injector;

    public <T> ServiceProvider bind(Class<T> clazz, Class<? extends T> implementation) {
        configure(action -> action.add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).to(implementation);
            }
        }));
        return this;
    }

    public ServiceProvider configure(Consumer<Collection<AbstractModule>> action) {
        action.accept(modules);
        return this;
    }

    public <T> ServiceProvider bind(Class<T> clazz, T instance) {
        configure(action -> action.add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).toInstance(instance);
            }
        }));
        return this;
    }


    /**
     * Build the service provider. It can be called multiple times.
     * Will reset the injector.
     */
    public void build() {
        injector = Guice.createInjector(modules);
    }

    public <T> T get(Class<T> type) {
        if (injector == null) {
            throw new IllegalStateException("The service provider is not built yet.");
        }
        return injector.getInstance(type);
    }
}
