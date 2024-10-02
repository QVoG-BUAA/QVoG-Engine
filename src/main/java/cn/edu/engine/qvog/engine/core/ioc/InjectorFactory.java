package cn.edu.engine.qvog.engine.core.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory to create {@link Injector} instances.
 * This enables the flexibility to add custom modules to the injector.
 * It seems that we won't need this feature, but it's here anyway.
 */
@Deprecated
public class InjectorFactory {
    private final List<AbstractModule> modules = new ArrayList<>();

    /**
     * Default constructor.
     * Adds the {@link CoreModule} to the injector.
     */
    public InjectorFactory() {
    }

    /**
     * @param module The module to add to the injector.
     * @return The factory instance.
     */
    public InjectorFactory withModule(AbstractModule module) {
        modules.add(module);
        return this;
    }

    /**
     * @return The injector instance.
     */
    public Injector build() {
        return Guice.createInjector(modules);
    }
}
