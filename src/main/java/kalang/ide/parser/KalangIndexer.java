
package kalang.ide.parser;
import java.io.*;
import kalang.ide.Logger;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.indexing.Context;
import org.netbeans.modules.parsing.spi.indexing.EmbeddingIndexer;
import org.netbeans.modules.parsing.spi.indexing.Indexable;
import org.netbeans.modules.parsing.spi.indexing.support.IndexDocument;
import org.netbeans.modules.parsing.spi.indexing.support.IndexingSupport;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
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
        IndexDocument id = is.createDocument(indxbl);
        id.addPair("class", "t", true, true);
        //TODO support needed
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
