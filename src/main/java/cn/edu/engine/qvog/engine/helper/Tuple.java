package cn.edu.engine.qvog.engine.helper;

import java.util.Objects;

public class Tuple<T, U> {
    private final T first;
    private final U second;

    public Tuple(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public static <T, U> Tuple<T, U> of(T first, U second) {
        return new Tuple<>(first, second);
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Tuple {\n" +
                "  first = " + first + "\n" +
                " second = " + second + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Tuple<?, ?> tuple = (Tuple<?, ?>) object;
        return Objects.equals(first, tuple.first) && Objects.equals(second, tuple.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
