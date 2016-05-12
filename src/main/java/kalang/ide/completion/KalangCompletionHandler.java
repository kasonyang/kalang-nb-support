/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalang.ide.completion;

import kalang.ast.AstNode;
import java.util.*;
import java.util.concurrent.Callable;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import kalang.ast.ClassReference;
import kalang.ast.ExprNode;
import kalang.ast.UnknownFieldExpr;
import kalang.compiler.CompilationUnit;
import kalang.core.Type;
import kalang.core.Types;
import org.netbeans.modules.csl.api.CodeCompletionContext;
import org.netbeans.modules.csl.api.CodeCompletionHandler2;
import org.netbeans.modules.csl.api.CodeCompletionResult;
import org.netbeans.modules.csl.api.CompletionProposal;
import org.netbeans.modules.csl.api.Documentation;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.ParameterInfo;
import org.netbeans.modules.csl.spi.ParserResult;
import static kalang.ide.Logger.log;
import kalang.ide.parser.KaParser;
import kalang.ide.utils.AstNodeHelper;
import kalang.util.TokenNavigator;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.netbeans.modules.csl.spi.DefaultCompletionResult;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class KalangCompletionHandler implements CodeCompletionHandler2 {

    @Override
    public Documentation documentElement(ParserResult pr, ElementHandle eh, Callable<Boolean> clbl) {
        return null;
    }

    @Override
    public CodeCompletionResult complete(CodeCompletionContext ccc) {
        log("begin to complete");
        List<CompletionProposal> list = null;
        KaParser.KaParserResult parseResult = (KaParser.KaParserResult) ccc.getParserResult();
        CompletionRequest request = new CompletionRequest();
        request.compiler = parseResult.getCompiler();
        int carsetOffset = ccc.getCaretOffset();
        log("caret offset:" + carsetOffset);
        if (carsetOffset > 0) {
            list = getCompleteType(parseResult, carsetOffset);
        }
        if (list == null) {
            return CodeCompletionResult.NONE;
        }
        final List<CompletionProposal> items = list;
        DefaultCompletionResult result = new DefaultCompletionResult(items, false);
        
        return result;
    }

    private List<CompletionProposal> getCompleteType(KaParser.KaParserResult result, int caret) {
        CompilationUnit cunit = result.getCompilationUnit();
        CommonTokenStream ts = cunit.getTokenStream();
        TokenNavigator tokenNav = new TokenNavigator(ts.getTokens().toArray(new Token[0]));
        tokenNav.move(caret - 1);
        int currentTokenId = tokenNav.getCurrentToken().getTokenIndex();
        if (currentTokenId < 1) {
            return null;
        }
        //TODO skip comment channels
        Token curToken = ts.get(currentTokenId);
        log("cur token:" + curToken.getText());
        Token prevToken = ts.get(currentTokenId - 1);
        log("prev token:" + prevToken.getText());
        int exprStopCaret;
        int anchorCaret;
        if (curToken.getText().equals(".")) {
            exprStopCaret = prevToken.getStopIndex();
            anchorCaret = curToken.getStopIndex() + 1;
        } else if (prevToken.getText().equals(".")) {
            if (currentTokenId < 2) {
                return null;
            }
            Token prevPrevToken = ts.get(currentTokenId - 2);
            exprStopCaret = prevPrevToken.getStopIndex();
            anchorCaret = prevToken.getStopIndex() + 1;
        } else {
            return null;
        }
        AstNode astNode = AstNodeHelper.getAstNodeByCaretOffset(result, exprStopCaret);
        log("expr ast:" + astNode);
        if (astNode == null) {
            return null;
        }
        Type type;
        boolean inStatic;
        if (astNode instanceof ExprNode) {
            type = ((ExprNode) astNode).getType();
            inStatic = false;
        } else if (astNode instanceof ClassReference) {
            type = Types.getClassType(
                    ((ClassReference) astNode).getReferencedClassNode()
            );
            inStatic = true;
        } else {
            return null;
        }
        CompletionRequest request = new CompletionRequest();
        request.anchorOffset = anchorCaret;
        request.compiler = result.getCompiler();
        String source = result.getSnapshot().getText().toString();
        request.prefix = source.substring(anchorCaret, caret);
        log("prefix:" + request.prefix);
        return TypeCompletion.complete(request, type,inStatic);
    }

    @Override
    public String document(ParserResult pr, ElementHandle eh) {
        return null;
    }

    @Override
    public ElementHandle resolveLink(String string, ElementHandle eh) {
        return eh;
    }

    @Override
    public String getPrefix(ParserResult pr, int i, boolean bln) {
        //TODO what is prefix?
        return "";
    }

    @Override
    public QueryType getAutoQuery(JTextComponent jtc, String string) {
        return QueryType.COMPLETION;
    }

    @Override
    public String resolveTemplateVariable(String string, ParserResult pr, int i, String string1, Map map) {
        return "i";
    }

    @Override
    public Set<String> getApplicableTemplates(Document dcmnt, int i, int i1) {
        return new HashSet<String>();
    }

    @Override
    public ParameterInfo parameters(ParserResult pr, int i, CompletionProposal cp) {
        return null;
    }

}
