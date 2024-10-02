package cn.edu.engine.qvog.engine.language.cxx.factory;

import cn.edu.engine.qvog.engine.core.graph.factory.DefaultValueHandler;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandlerProvider;
import cn.edu.engine.qvog.engine.core.graph.factory.ValueFactory;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.expr.*;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.stmt.DeclarationStatementHandler;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.stmt.FunctionDefStatementHandler;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.stmt.IfStatementHandler;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.stmt.ReturnStatementHandler;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.virtual.FunctionExitHandler;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.virtual.SystemExitHandler;

public class CxxValueHandlerProvider implements IValueHandlerProvider {
    @Override
    public void registerHandlers(ValueFactory factory) {
        // expr
        factory.registerHandler("Name", new IdExpressionHandler())
                .registerHandler("Literal", new LiteralExpressionHandler())

                .registerHandler("BinaryExpr", new BinaryExpressionHandler())
                .registerHandler("UnaryExpr", new UnaryExpressionHandler())
                .registerHandler("Assign", new AssignExpressionHandler())
                .registerHandler("Call", new FunctionCallExpressionHandler())
                .registerHandler("ArraySub", new ArraySubExpressionHandler())
                .registerHandler("FieldRef", new FieldReferenceExpressionHandler())
                .registerHandler("TypeCast", new TypeCastExpressionHandler())

                .registerHandler("New", new NewExpressionHandler())
                .registerHandler("Delete", new DeleteExpressionHandler())

                .registerHandler("SysExit", new SystemExitHandler())
                .registerHandler("FunExit", new FunctionExitHandler())

                // stmt
                .registerHandler("Return", new ReturnStatementHandler())
                .registerHandler("Decl", new DeclarationStatementHandler())
                .registerHandler("FunctionDef", new FunctionDefStatementHandler())
                .registerHandler("If", new IfStatementHandler())

                // TODO switch/case/while/do-while/for/goto/label
                .registerHandler(null, new DefaultValueHandler());
    }
}
