package kalang.ide.lexer;

import java.io.IOException;
import java.io.InputStream;
import kalang.antlr.KalangLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.openide.util.Exceptions;

class KaLexer implements Lexer<KaTokenId> {

    private final LexerRestartInfo<KaTokenId> info;
    private KalangLexer lexer;

    KaLexer(LexerRestartInfo<KaTokenId> lrInfo) {
        this.info = lrInfo;
        lexer = new KalangLexer(new AntlrCharStream(info.input(),"kalang"));
    }

    @Override
    public org.netbeans.api.lexer.Token<KaTokenId> nextToken() {
        Token tk = lexer.nextToken();
        System.out.println(tk.getType());
        if (info.input().readLength() < 1) {
            return null;
        }
        int type = tk.getType();
        if(type<1){
            type = 1;
        }
        KaTokenId tokenId = KaLanguageHierarchy.getToken(type);
        return info.tokenFactory().createToken(tokenId);
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }

}