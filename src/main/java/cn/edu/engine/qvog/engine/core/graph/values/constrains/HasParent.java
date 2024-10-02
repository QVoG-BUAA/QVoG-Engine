package cn.edu.engine.qvog.engine.core.graph.values.constrains;

/**
 * Marker interface for values that have a parent.
 *
 * @param <TParent> Type of the parent.
 */
public interface HasParent<TParent> {
    TParent getParent();

    void setParent(TParent parent);

    default void removeParent() {
        setParent(null);
    }
}
