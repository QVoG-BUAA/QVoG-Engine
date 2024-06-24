package cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.buaa.qvog.engine.core.graph.values.constrains.IsUnknown;
import cn.edu.buaa.qvog.engine.helper.NamingHelper;

public class UnknownReference extends Reference implements IsUnknown {
    public static final UnknownReference DEFAULT = new UnknownReference();

    public UnknownReference() {
        super(NamingHelper.toReservedName(UnknownReference.class.getSimpleName()));
    }
}
