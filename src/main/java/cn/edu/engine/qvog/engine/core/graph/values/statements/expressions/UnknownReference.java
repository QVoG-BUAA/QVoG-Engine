package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.values.constrains.IsUnknown;
import cn.edu.engine.qvog.engine.helper.NamingHelper;

public class UnknownReference extends Reference implements IsUnknown {
    public static final UnknownReference DEFAULT = new UnknownReference();

    public UnknownReference() {
        super(NamingHelper.toReservedName(UnknownReference.class.getSimpleName()));
    }
}
