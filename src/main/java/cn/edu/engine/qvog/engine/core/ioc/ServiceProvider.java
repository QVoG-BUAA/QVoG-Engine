package cn.edu.engine.qvog.engine.core.ioc;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.engine.qvog.engine.language.ArkTS.factory.ArkTSValueHandlerProvider;
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
    //单例模式

    //下面是Guice框架下的套路。
    private final List<AbstractModule> modules = new ArrayList<>();
    //这是一个List,每个元素AbstractModule都是一个映射关系。
    private Injector injector;
    //injector根据上述List<AbstractModule>中的一个AbstractMoudle来初始。
    //本质上就是告诉injector要把哪两个东西绑定。

    public <T> ServiceProvider bind(Class<T> clazz, Class<? extends T> implementation) {
        configure(action -> action.add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).to(implementation);
            }
        }));
        return this;
    }
    //这个bind函数传入两个参数之后，把他们放在new AbstractModule中的congigure方法。
    //之后只要把这个AbstractModule传给初始化的injector就可以知道绑定哪两个了。

    public ServiceProvider configure(Consumer<Collection<AbstractModule>> action) {
        action.accept(modules);
        return this;
    }
    //这里的configure不是AbstractModule框架下的函数。而是一个自定义的函数。
    //configure的参数Consumer<Collection<AbstractModule>> action是一个函数式接口。
    //所谓函数式接口就是传入一个函数，一个运算关系。
    //例如Consumer<T> name中，T就是这个函数要用到的参数，name其实无所谓就是一个名字而已。

    //具体到上面这个函数，意思就是传入一个Collection<AbstractModule>类型的参数，然后对其运算。
    //做什么运算（操作）呢？就是accept()。
    //accept()是一个传入Consumer<T> name中T类型的参数，且没有返回值的函数。
    //accept(modules)意思就是消费modules，也就是modules就是我们的T类型的参数。
    //accept()的具体实现就是我们传入的那个所谓的函数所谓的运算逻辑。
    //说白了Consumer<T> name与name.accept(T类型)要对应起来。

    //在上述bind函数中，我们可以看到运算逻辑就是action -> action.add(...)
    //这里的Lambda表达式中action也不重要，换成其他也行。
    //说人话，这个函数就是把传入的一个参数（这里叫action），往里面add一个东西。

    //根据accept(modules);我们知道modules就是传入的参数，故合起来就是往moudles里add一个东西。

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

//    public void bind(Class<IValueHandlerProvider> iValueHandlerProviderClass, Class<ArkTSValueHandlerProvider> arkTSValueHandlerProviderClass) {
//    }
    //这里的Guice的用法还相对简单一点，其实还可以更复杂。但是这里用了泛型其实已经很通用了。
}
