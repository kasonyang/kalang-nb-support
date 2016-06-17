
package kalang.ide.index;
import java.io.*;
import java.util.Collection;
import kalang.compiler.CompilationUnit;
import kalang.ide.Logger;
import kalang.ide.parser.KaParser;
import kalang.util.AstUtil;
import kalang.util.NameUtil;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.indexing.Context;
import org.netbeans.modules.parsing.spi.indexing.EmbeddingIndexer;
import org.netbeans.modules.parsing.spi.indexing.Indexable;
import org.netbeans.modules.parsing.spi.indexing.support.IndexDocument;
import org.netbeans.modules.parsing.spi.indexing.support.IndexResult;
import org.netbeans.modules.parsing.spi.indexing.support.IndexingSupport;
import org.netbeans.modules.parsing.spi.indexing.support.QuerySupport;
import org.openide.util.Exceptions;
/**
 *
 * @author Kason Yang
 */
public class KalangIndexer extends EmbeddingIndexer{

    @Override
    protected void index(Indexable indxbl, Parser.Result result, Context cntxt) {
        IndexingSupport is = null;
        try {    
            is = IndexingSupport.getInstance(cntxt);
        } catch (IOException ex) {
            Logger.log(ex.toString());
            return;
        }
        KaParser.KaParserResult rs = (KaParser.KaParserResult) result;
        IndexDocument id = is.createDocument(indxbl);
        Logger.log("indexing " + rs.getSnapshot().getSource().getFileObject());
        CompilationUnit cu = rs.getCompilationUnit();
        String clsName = rs.getClassName();
        String simpleName = NameUtil.getClassNameWithoutPackage(clsName);
        //String pkgName = AstUtil.getPackageName(clsName);
        id.addPair("simple_name",simpleName , true, true);
        id.addPair("simple_name_ci", simpleName.toLowerCase(), true, true);
        id.addPair("full_name", clsName, true, true);
        id.addPair("full_name_ci", clsName.toLowerCase(), true, true);
        id.addPair("offset",String.valueOf(cu.getAst().offset.startOffset), false, true);
        //TODO get construcors
        //id.addPair("constructors", "test()", true, true);
        is.addDocument(id);
    }

}
