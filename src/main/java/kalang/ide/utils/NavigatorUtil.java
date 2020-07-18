package kalang.ide.utils;

import kalang.compiler.compile.CompilationUnit;
import kalang.compiler.util.TokenNavigator;
import kalang.ide.compiler.ParseTreeNavigator;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

/**
 * @author KasonYang
 */
public class NavigatorUtil {

    public static TokenNavigator createTokenNavigator(CompilationUnit compilationUnit) {
        CommonTokenStream ts = (CommonTokenStream) compilationUnit.getParser().getTokenStream();
        return new TokenNavigator(ts.getTokens().toArray(new Token[0]));
    }

    public static ParseTreeNavigator createParseTreeNavigator(CompilationUnit compilationUnit) {
        ParserRuleContext root = compilationUnit.getAstBuilder().getParseTree();
        return new ParseTreeNavigator(root);
    }

}
