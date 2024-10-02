package cn.edu.engine.qvog.engine.exception;

public class ValueHandlerMismatchException extends RuntimeException {
    public ValueHandlerMismatchException(String type, Class<?> handler, Throwable cause) {
        super("Mismatched value handler '" + handler.getSimpleName() + "' for " + type, cause);
    }

    public ValueHandlerMismatchException(String type, Class<?> handler) {
        super("Mismatched value handler '" + handler.getSimpleName() + "' for " + type);
    }
}
