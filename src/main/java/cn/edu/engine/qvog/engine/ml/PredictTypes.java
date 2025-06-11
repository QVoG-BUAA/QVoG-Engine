package cn.edu.engine.qvog.engine.ml;

public enum PredictTypes {
    Source,
    Sink,
    None,
    Barrier;

    public static PredictTypes fromString(String type) throws IllegalArgumentException {
        try {
            return valueOf(Character.toUpperCase(type.charAt(0)) + type.substring(1));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid color name: " + type);
        }
    }
}
