package kalang.ide;

import kalang.ide.index.KalangIndexerFactory;
import kalang.ide.completion.KalangCompletionHandler;
import kalang.ide.lexer.KaTokenId;
import kalang.ide.index.KaIndexSearcher;
import kalang.ide.parser.KLKeystrokeHandler;
import kalang.ide.parser.KaParser;
import kalang.ide.parser.KalangDeclFinder;
import kalang.ide.parser.KalangSemanticAnalyzer;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.api.CodeCompletionHandler;
import org.netbeans.modules.csl.api.DeclarationFinder;
import org.netbeans.modules.csl.api.IndexSearcher;
import org.netbeans.modules.csl.api.KeystrokeHandler;
import org.netbeans.modules.csl.api.SemanticAnalyzer;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.netbeans.modules.parsing.spi.*;
import org.netbeans.modules.parsing.spi.indexing.EmbeddingIndexerFactory;
import org.netbeans.modules.parsing.spi.indexing.PathRecognizerRegistration;

@PathRecognizerRegistration(mimeTypes = "text/x-kalang")
@LanguageRegistration(mimeType = "text/x-kalang")
public class KaLanguage extends DefaultLanguageConfig {
    
    public final static String MIME_TYPE = "text/x-kalang";

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
    
    

    @Override
    public SemanticAnalyzer getSemanticAnalyzer() {
        return new KalangSemanticAnalyzer();
    }

    @Override
    public KeystrokeHandler getKeystrokeHandler() {
        return new KLKeystrokeHandler();
    }

    @Override
    public EmbeddingIndexerFactory getIndexerFactory() {
        return new KalangIndexerFactory();
    }

    @Override
    public IndexSearcher getIndexSearcher() {
        return new KaIndexSearcher();
    } 
    
}