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
import kalang.compiler.core.ClassType;
import kalang.compiler.core.ObjectType;
import kalang.compiler.core.Type;
import kalang.compiler.core.Types;
import kalang.ide.compiler.complete.Completion;
import kalang.ide.compiler.complete.MemberCompletion;
import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KasonYang
 */
public class ExtendKalangCompiler extends KalangCompiler {

    /**
     * key: operator token
     * value: complete type
     */
    public Map<Token, Completion> completeTypeMap = new HashMap<Token, Completion>();

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
                completeForTarget(token, result);
                return result;
            }

            @Override
            public ExprNode visitGetFieldExpr(KalangParser.GetFieldExprContext ctx) {
                Token opToken = ctx.refKey;
                try {
                    return complete(opToken, super.visitGetFieldExpr(ctx));
                } catch (NodeException ne) {
                    completeForTarget(opToken, visit(ctx.expression()));
                    throw ne;
                }
            }

            @Override
            public AstNode visitInvokeExpr(KalangParser.InvokeExprContext ctx) {
                Token opToken = ctx.refKey;
                try {
                    return complete(opToken, super.visitInvokeExpr(ctx));
                } catch (NodeException ex) {
                    completeForTarget(opToken, visit(ctx.target));
                    throw ex;
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

            private <T> T complete(Token opToken, T expr) {
                if (expr instanceof ObjectInvokeExpr) {
                    completeForTarget(opToken, ((ObjectInvokeExpr) expr).getInvokeTarget());
                } else if (expr instanceof StaticInvokeExpr) {
                    completeForTarget(opToken, ((StaticInvokeExpr) expr).getInvokeClass());
                } else if (expr instanceof ObjectFieldExpr) {
                    completeForTarget(opToken, ((ObjectFieldExpr) expr).getTarget());
                } else if (expr instanceof StaticFieldExpr) {
                    completeForTarget(opToken, ((StaticFieldExpr) expr).getClassReference());
                }
                return expr;
            }

            private void completeForTarget(Token opToken, Object node) {
                if (node instanceof ExprNode) {
                    Type targetType = ((ExprNode) node).getType();
                    if (!(targetType instanceof ObjectType)) {
                        return;
                    }
                    ObjectType targetObjType = (ObjectType) targetType;
                    completeTypeMap.put(opToken, new MemberCompletion(targetObjType, false));
                } else if (node instanceof ClassReference) {
                    ClassReference cr = (ClassReference) node;
                    ClassNode clsNode = cr.getReferencedClassNode();
                    ClassType clsType = Types.getClassType(clsNode);
                    completeTypeMap.put(opToken, new MemberCompletion(clsType, true));
                }
            }

        };
    }

    @Override
    public CodeGenerator createCodeGenerator(CompilationUnit compilationUnit) {
        return new Ast2JavaStub(compilationUnit);
    }

}
