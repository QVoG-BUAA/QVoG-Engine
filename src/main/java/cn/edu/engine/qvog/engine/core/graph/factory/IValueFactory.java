package cn.edu.engine.qvog.engine.core.graph.factory;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Factory for creating Value from JSON format AST.
 * <p>
 * However, the factory itself does not care how to create the Value,
 * it will delegate the creation to the registered handlers.
 */
public interface IValueFactory {
    /**
     * Register a handler for a specific AST type.
     *
     * @param astType AST type.
     * @param handler Handler.
     * @return Itself.
     */
    IValueFactory registerHandler(String astType, IValueHandler handler);

    /**
     * Register all handlers provided by the given provider.
     *
     * @param provider Handler provider.
     * @return Itself.
     */
    IValueFactory registerHandlers(IValueHandlerProvider provider);

    default <TBase extends Value, TDefault extends TBase> TBase build(JSONObject json) {
        return build(json, null);
    }

    /**
     * Build a Value from the given JSON object.
     *
     * @param json         JSON object.
     * @param defaultValue Default value to return if the handler is not found.
     * @param <TBase>      Base type of the value.
     * @param <TDefault>   Default type of the value.
     * @return Value.
     */
    <TBase extends Value, TDefault extends TBase> TBase build(JSONObject json, TDefault defaultValue);

    default <TBase extends Value, TDefault extends TBase> TBase build(String json, TDefault defaultValue) {
        JSONParser parser = new JSONParser();
        try {
            return build((JSONObject) parser.parse(json), defaultValue);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
