package kalang.ide.compiler.complete;

import kalang.compiler.antlr.KalangLexer;
import kalang.compiler.antlr.KalangParser.ExpressionContext;
import kalang.compiler.ast.AstNode;
import kalang.compiler.ast.ClassNode;
import kalang.compiler.ast.ClassReference;
import kalang.compiler.ast.ExprNode;
import kalang.compiler.compile.CompilationUnit;
import kalang.compiler.core.ClassType;
import kalang.compiler.core.ObjectType;
import kalang.compiler.core.Type;
import kalang.compiler.core.Types;
import kalang.compiler.util.TokenNavigator;
import kalang.ide.compiler.ParseTreeNavigator;
import kalang.ide.utils.NavigatorUtil;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Map;

/**
 * @author KasonYang
 */
public class KalangCompleter {

    //TODO move to compilation unit
    private Map<ParseTree, AstNode> parseTreeAstNodeMap;
    private CompilationUnit compilationUnit;

    public KalangCompleter(Map<ParseTree, AstNode> parseTreeAstNodeMap, CompilationUnit compilationUnit) {
        this.parseTreeAstNodeMap = parseTreeAstNodeMap;
        this.compilationUnit = compilationUnit;
    }

    public Completion complete(int caret) {
        TokenNavigator tokenNav = NavigatorUtil.createTokenNavigator(compilationUnit);
        try {
            tokenNav.move(caret - 1);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
        Token currentToken = tokenNav.getCurrentToken();
        if (!tokenNav.hasPrevious()) {
            return null;
        }
        tokenNav.previous(0);
        Token prevToken = tokenNav.getCurrentToken();
        if (isDotToken(currentToken)) {
            return completeMember(prevToken, caret);
        } else if (isIdentifier(currentToken) && isDotToken(prevToken)) {
            if (!tokenNav.hasPrevious()) {
                return null;
            }
            tokenNav.previous(0);
            Token prevPrevToken = tokenNav.getCurrentToken();
            return completeMember(prevPrevToken, currentToken.getStartIndex());
        }
        return null;
    }


    private MemberCompletion completeMember(Token targetExprToken, int anchorOffset) {
        ParseTreeNavigator parseTreeNav = NavigatorUtil.createParseTreeNavigator(compilationUnit);
        ExpressionContext prevCtx = parseTreeNav.move(targetExprToken.getStopIndex(), ExpressionContext.class);
        AstNode node = parseTreeAstNodeMap.get(prevCtx);
        if (node instanceof ExprNode) {
            Type targetType = ((ExprNode) node).getType();
            if (!(targetType instanceof ObjectType)) {
                return null;
            }
            ObjectType targetObjType = (ObjectType) targetType;
            return new MemberCompletion(anchorOffset, targetObjType, false);
        } else if (node instanceof ClassReference) {
            ClassReference cr = (ClassReference) node;
            ClassNode clsNode = cr.getReferencedClassNode();
            ClassType clsType = Types.getClassType(clsNode);
            return new MemberCompletion(anchorOffset, clsType, true);
        }
        return null;
    }

    private boolean isDotToken(Token token) {
        return token.getType() == KalangLexer.DOT;
    }

    private boolean isIdentifier(Token token) {
        return token.getType() == KalangLexer.Identifier;
    }

}
