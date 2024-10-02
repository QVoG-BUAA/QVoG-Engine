package cn.edu.engine.qvog.engine.exception;

public class TraversalException extends RuntimeException {
    public TraversalException(String message) {
        super(message);
    }

    public TraversalException(String message, Throwable cause) {
        super(message, cause);
    }

    public TraversalException(Throwable cause) {
        super(cause);
    }
}
