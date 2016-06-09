package kalang.ide.index;

import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.indexing.Context;
import org.netbeans.modules.parsing.spi.indexing.EmbeddingIndexer;
import org.netbeans.modules.parsing.spi.indexing.EmbeddingIndexerFactory;
import org.netbeans.modules.parsing.spi.indexing.Indexable;

/**
 *
 * @author Kason Yang
 */
public class KalangIndexerFactory extends EmbeddingIndexerFactory {

    public static final int VERSION = 1;
    public static final String INDEX_NAME = "kalang";

    public KalangIndexerFactory() {
    }

    @Override
    public EmbeddingIndexer createIndexer(Indexable indexable, Snapshot snapshot) {
        return new KalangIndexer();
    }

    @Override
    public void filesDeleted(Iterable<? extends Indexable> deleted, Context context) {
        //TODO delete index
    }

    @Override
    public void filesDirty(Iterable<? extends Indexable> dirty, Context context) {
        //TODO delete index?
    }

    @Override
    public String getIndexerName() {
        return INDEX_NAME;
    }

    @Override
    public int getIndexVersion() {
        return VERSION;
    }

}
