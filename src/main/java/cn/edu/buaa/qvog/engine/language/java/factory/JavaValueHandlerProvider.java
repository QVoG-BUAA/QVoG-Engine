package cn.edu.buaa.qvog.engine.language.java.factory;

import cn.edu.buaa.qvog.engine.core.graph.factory.DefaultValueHandler;
import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.buaa.qvog.engine.core.graph.factory.ValueFactory;
import cn.edu.buaa.qvog.engine.language.java.factory.handlers.*;

public class JavaValueHandlerProvider implements IValueHandlerProvider {
    @Override
    public void registerHandlers(ValueFactory factory) {
        factory.registerHandler("Assign", new AssignExpressionHandler())
                .registerHandler("BinOp", new BinaryOperatorHandler())
                .registerHandler("Compare", new CompareHandler())
                .registerHandler("Call", new CallHandler())
                .registerHandler("Name", new ReferenceHandler())
                .registerHandler("Expr", new ExpressionHandler())
                .registerHandler("If", new IfStatementHandler())
                .registerHandler("Decl", new DeclStatementHandler())
                .registerHandler("Constant", new LiteralValueHandler())
                .registerHandler(null, new DefaultValueHandler());
    }
}
