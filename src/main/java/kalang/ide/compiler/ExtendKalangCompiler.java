package kalang.ide.compiler;

import kalang.compiler.antlr.KalangParser;
import kalang.compiler.ast.*;
import kalang.compiler.compile.CodeGenerator;
import kalang.compiler.compile.CompilationUnit;
import kalang.compiler.compile.Configuration;
import kalang.compiler.compile.KalangCompiler;
import kalang.compiler.compile.codegen.Ast2JavaStub;
import kalang.compiler.compile.semantic.AstBuilder;
import kalang.compiler.compile.semantic.NodeException;
import kalang.compiler.util.TokenNavigator;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KasonYang
 */
public class ExtendKalangCompiler extends KalangCompiler {

    @Nullable
    public CompleteReq completeReq;

    public Map<Token, Object> completeInfoMap = new HashMap<Token, Object>();

    @Nullable
    public CompleteContext completeContext;

    public ExtendKalangCompiler() {
    }

    public ExtendKalangCompiler(Configuration configuration) {
        super(configuration);
    }

    @Override
    public AstBuilder createAstBuilder(CompilationUnit compilationUnit, KalangParser parser) {
        return new AstBuilder(compilationUnit, parser) {

            @Override
            public Object visitErrorousMemberExpr(KalangParser.ErrorousMemberExprContext emec) {
                Token token = emec.stop;
                Object result = super.visit(emec.expression());
                completeInfoMap.put(token, result);
                return result;
            }

            @Override
            public ExprNode visitGetFieldExpr(KalangParser.GetFieldExprContext ctx) {
                Token opToken = ctx.refKey;
                try {
                    ExprNode expr = super.visitGetFieldExpr(ctx);
                    completeNormal(opToken, expr);
                    return expr;
                } catch (NodeException ne) {
                    Object expr = visit(ctx.expression());
                    completeTarget(opToken, expr);
                    throw ne;
                }
            }

            @Override
            public Object visitErrorousStat(KalangParser.ErrorousStatContext esc) {
                super.visitErrorousStat(esc);
                Object ast = visit(esc.expression());
                if (ast instanceof AstNode) {
                    return new ExprStmt(new ErrorousExpr((AstNode) ast));
                } else {
                    return null;
                }
            }

            private boolean isDotToken(Token token) {
                return token != null && token.getText().equals(".");
            }

            @Nullable
            private Token getToken(int tokenOffset) {
                if (completeReq == null || completeReq.caret <= 0) {
                    return null;
                }
                CommonTokenStream ts = (CommonTokenStream) getParser().getTokenStream();
                TokenNavigator tokenNav = new TokenNavigator(ts.getTokens().toArray(new Token[0]));
                tokenNav.move(completeReq.caret);
                int currentTokenId = tokenNav.getCurrentToken().getTokenIndex();
                if (currentTokenId < 1) {
                    return null;
                }
                //TODO skip comment channels
                return ts.get(currentTokenId + tokenOffset);
            }

            private void completeNormal(Token opToken, Object node) {
                if (node instanceof ObjectInvokeExpr) {
                    ObjectInvokeExpr oie = (ObjectInvokeExpr) node;
                    completeTarget(opToken, oie.getInvokeTarget());
                }
            }

            private void completeTarget(Token opToken, Object target) {
                completeInfoMap.put(opToken, target);
            }

        };
    }

    @Override
    public CodeGenerator createCodeGenerator(CompilationUnit compilationUnit) {
        return new Ast2JavaStub(compilationUnit);
    }

    private Object doComplete(Object node, int anchorCaret) {
        completeContext = new CompleteContext();
        if (node instanceof AstNode) {
            completeContext.completeNode = (AstNode) node;
            completeContext.anchorCaret = anchorCaret;
        }
        return node;
    }

}
