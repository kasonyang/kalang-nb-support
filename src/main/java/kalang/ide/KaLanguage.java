package kalang.ide;

import kalang.ide.completion.KalangCompletionHandler;
import kalang.ide.lexer.KaTokenId;
import kalang.ide.parser.KaParser;
import kalang.ide.parser.KalangDeclFinder;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.api.CodeCompletionHandler;
import org.netbeans.modules.csl.api.DeclarationFinder;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.netbeans.modules.parsing.spi.*;

@LanguageRegistration(mimeType = "text/x-kalang")
public class KaLanguage extends DefaultLanguageConfig {

    @Override
    public Language getLexerLanguage() {
        return KaTokenId.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "kl";
    }

    @Override
    public Parser getParser() {
        return new KaParser();
    }

    @Override
    public CodeCompletionHandler getCompletionHandler() {
        //Logger.log("on getCompletionHandler");
        return new KalangCompletionHandler();
    }

    @Override
    public DeclarationFinder getDeclarationFinder() {
        return new KalangDeclFinder();
    }

//    @Override
//    public SemanticAnalyzer getSemanticAnalyzer() {
//        return new KalangSemanticAnalyzer();
//    }

//    @Override
//    public KeystrokeHandler getKeystrokeHandler() {
//        return new KLKeystrokeHandler();
//    }
    
    
}