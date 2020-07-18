package kalang.ide.completion;

import kalang.compiler.antlr.KalangLexer;
import kalang.compiler.compile.CompilationUnit;
import kalang.compiler.util.TokenNavigator;
import kalang.ide.compiler.complete.Completion;
import kalang.ide.compiler.complete.KalangCompleter;
import kalang.ide.compiler.complete.MemberCompletion;
import kalang.ide.parser.KaParser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.netbeans.modules.csl.api.*;
import org.netbeans.modules.csl.spi.DefaultCompletionResult;
import org.netbeans.modules.csl.spi.ParserResult;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static kalang.ide.Logger.log;

/**
 *
 * @author Kason Yang
 */
public class KalangCompletionHandler implements CodeCompletionHandler2 {

    @Override
    public Documentation documentElement(ParserResult pr, ElementHandle eh, Callable<Boolean> clbl) {
        return null;
    }

    @Override
    public CodeCompletionResult complete(CodeCompletionContext ccc) {
        log("begin to complete");
        String prefix = ccc.getPrefix();
        log("request prefix:" + prefix);
        List<CompletionProposal> list = null;
        KaParser.KaParserResult parseResult = (KaParser.KaParserResult) ccc.getParserResult();
        CompletionRequest request = new CompletionRequest();
        request.compiler = parseResult.getCompiler();
        int caretOffset = ccc.getCaretOffset();
        log("caret offset:" + caretOffset);
        if (caretOffset > 0) {
            list = getCompleteType(parseResult, caretOffset, ccc.getPrefix());
        }
        if (list == null) {
            return CodeCompletionResult.NONE;
        }
        return new DefaultCompletionResult(list, false);
    }

    private List<CompletionProposal> getCompleteType(KaParser.KaParserResult result, int caret, String prefix) {
        CompilationUnit cunit = result.getCompilationUnit();
        if (caret <= 0) {
            return null;
        }
        KalangCompleter completer = new KalangCompleter(result.getCompiler().parseTreeAstNodeMap, result.getCompilationUnit());
        Completion completeType = completer.complete(caret);
        if (completeType == null) {
            return null;
        }
        if (completeType instanceof MemberCompletion) {
            MemberCompletion mc = (MemberCompletion) completeType;
            CompletionRequest request = new CompletionRequest();
            request.anchorOffset = mc.getAnchorOffset();
            request.compiler = result.getCompiler();
            request.compilationUnit = cunit;
            request.prefix = prefix;
            log("prefix:" + request.prefix);
            return TypeCompletion.complete(request, mc.getOwnerType(), mc.getStaticMember());
        }
        return null;
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
    public String getPrefix(ParserResult pr, int caret, boolean upToOffset) {
        KaParser.KaParserResult result = (KaParser.KaParserResult) pr;
        CompilationUnit cunit = result.getCompilationUnit();
        if (caret <= 0) {
            return "";
        }
        CommonTokenStream ts = (CommonTokenStream) cunit.getParser().getTokenStream();
        TokenNavigator tokenNav = new TokenNavigator(ts.getTokens().toArray(new Token[0]));
        tokenNav.move(caret - 1);
        Token prevToken = tokenNav.getCurrentToken();
        if (prevToken.getType() != KalangLexer.Identifier) {
            return "";
        }
        int start = prevToken.getStartIndex();
        String source = result.getSnapshot().getText().toString();
        return source.substring(start, caret);
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
