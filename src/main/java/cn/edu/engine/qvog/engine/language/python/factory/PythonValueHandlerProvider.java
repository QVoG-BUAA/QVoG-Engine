package cn.edu.engine.qvog.engine.language.python.factory;

import cn.edu.engine.qvog.engine.core.graph.factory.DefaultValueHandler;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.engine.qvog.engine.core.graph.factory.ValueFactory;
import cn.edu.engine.qvog.engine.language.python.factory.handlers.*;

public class PythonValueHandlerProvider implements IValueHandlerProvider {
    @Override
    public void registerHandlers(ValueFactory factory) {
        factory.registerHandler("Name", new ReferenceHandler())
                .registerHandler("Attribute", new AttributeHandler())
                .registerHandler("Constant", new LiteralHandler())
                .registerHandler("Dict", null)
                .registerHandler("List", null)
                .registerHandler("Tuple", null)
                .registerHandler("Expr", new ExpressionHandler())
                .registerHandler("Assign", new AssignExpressionHandler())
                .registerHandler("Call", new CallHandler())
                .registerHandler("keyword", new KeywordHandler())
                .registerHandler("UnaryOp", new UnaryOperatorHandler())
                .registerHandler("BinOp", new BinaryOperatorHandler())
                .registerHandler("BoolOp", null)
                .registerHandler("Compare", new CompareHandler())
                .registerHandler("If", new IfStatementHandler())
                .registerHandler("For", new ForStatementHandler())
                .registerHandler("Return", new ReturnStatementHandler())
                .registerHandler("Try", new TryStatementHandler())
                .registerHandler("FunctionDef", new FunctionDeclarationHandler())
                .registerHandler(null, new DefaultValueHandler());
    }
}
