package cn.edu.buaa.qvog.engine.core.graph.values;

import cn.edu.buaa.qvog.engine.core.graph.values.constrains.IsUnknown;


/**
 * Use this when the value is not recognized.
 */
public final class UnknownValue extends Value implements IsUnknown {
    public static final UnknownValue DEFAULT = new UnknownValue();
}
