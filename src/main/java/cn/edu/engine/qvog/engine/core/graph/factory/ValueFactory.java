package cn.edu.engine.qvog.engine.core.graph.factory;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.exception.InvalidAstException;
import cn.edu.engine.qvog.engine.exception.ValueHandlerMismatchException;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating AST nodes. It is only a "shell", as all
 * the actual work is done by the language-specific handlers {@link IValueHandler}.
 * And the handlers are provided by {@link IValueHandlerProvider}.
 */
@Singleton
public class ValueFactory implements IValueFactory {
    private final Map<String, IValueHandler> handlers = new HashMap<>();
    IValueHandler defaultHandler;

    @Inject
    public ValueFactory(IValueHandlerProvider provider) {
        provider.registerHandlers(this);
    }

    /**
     * Register a handler for the given AST type. If astType is null,
     * then register default handler.
     *
     * @param astType AST type.
     * @param handler Handler.
     * @return Itself.
     */
    @Override
    public ValueFactory registerHandler(String astType, IValueHandler handler) {
        if (astType == null) {
            defaultHandler = handler;
        } else {
            handlers.put(astType, handler);
        }
        return this;
    }

    /**
     * Register all handlers provided by the given provider.
     *
     * @param provider Handler provider.
     * @return Itself.
     */
    @Override
    public ValueFactory registerHandlers(IValueHandlerProvider provider) {
        provider.registerHandlers(this);
        return this;
    }


    /**
     * This is the root method for creating AST nodes. All exceptions
     * will be caught here and wrapped into {@link InvalidAstException}.
     *
     * @param json JSON object for the AST node.
     * @return AST node.
     */
    @Override
    public <TBase extends Value, TDefault extends TBase> TBase build(JSONObject json, TDefault defaultValue) {
        TBase value;
        var type = JsonHelper.getValue(json, "_type");
        var handler = handlers.get(type);
        if (handler == null) {
            // TODO, should be better to fix
            // value = (TBase) new UnknownValue();
            value = defaultValue;
        } else {
            try {
                value = (TBase) handler.build(json, this);
            } catch (ClassCastException e) {
                throw new ValueHandlerMismatchException(type, handler.getClass(), e);
            } catch (Exception e) {
                throw new InvalidAstException(json, e);
            }
        }

        if (value == null) {
            throw new IllegalStateException("No value get from " + json.toJSONString());
        }

        return value;
    }
}
