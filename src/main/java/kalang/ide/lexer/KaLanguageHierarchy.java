package kalang.ide.lexer;

import java.util.*;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

public class KaLanguageHierarchy extends LanguageHierarchy<KaTokenId> {

    private static List<KaTokenId> tokens;
    
    private static void init() {
        tokens =Arrays.asList(TokenUtil.getAllTokens());
    }

    static synchronized KaTokenId getToken(int id) {
        if (tokens == null) {
            init();
        }
        KaTokenId tk = tokens.get(id);
        if(tk==null) tk = tokens.get(0);
        return tk;
    }

    @Override
    protected synchronized Collection<KaTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<KaTokenId> createLexer(LexerRestartInfo<KaTokenId> info) {
        return new KaLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-kalang";
    }
}