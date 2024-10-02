package cn.edu.engine.qvog.engine.exception;

import org.json.simple.JSONObject;

public class InvalidAstException extends RuntimeException {
    public InvalidAstException(String message) {
        super(message);
    }

    public InvalidAstException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAstException(Throwable cause) {
        super(cause);
    }

    public InvalidAstException(JSONObject json) {
        super("Invalid AST node: " + json.toJSONString());
    }

    public InvalidAstException(JSONObject json, Throwable cause) {
        super("Invalid AST node: " + json.toJSONString(), cause);
    }
}
