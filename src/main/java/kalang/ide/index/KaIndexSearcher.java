package kalang.ide.index;

import kalang.ide.index.ClassDescriptor;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import kalang.ide.elements.ClassElementHandle;
import kalang.ide.index.KalangIndexerFactory;
import kalang.ide.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.modules.csl.api.IndexSearcher;
import org.netbeans.modules.parsing.spi.indexing.support.IndexResult;
import org.netbeans.modules.parsing.spi.indexing.support.QuerySupport;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author Kason Yang
 */
public class KaIndexSearcher implements IndexSearcher{

    @Override
    public Set<? extends Descriptor> getTypes(Project project, String textForQuery, QuerySupport.Kind searchType, Helper helper) {
        Set<Descriptor> list = new HashSet();
        Logger.log("query:" + textForQuery);
        Logger.log("query type:" + searchType);
        Logger.log("helper:" + helper);
        Collection<FileObject> roots = QuerySupport.findRoots(project, null,null, null);
        try {
            QuerySupport qs = QuerySupport.forRoots(KalangIndexerFactory.INDEX_NAME, KalangIndexerFactory.VERSION,roots.toArray(new FileObject[roots.size()]));
            QuerySupport.Query.Factory qf = qs.getQueryFactory();
            //QuerySupport.Kind type = searchType;
            String fieldSuffix = "";
            String queryText = textForQuery;
            if(searchType == QuerySupport.Kind.CASE_INSENSITIVE_CAMEL_CASE || searchType == QuerySupport.Kind.CASE_INSENSITIVE_PREFIX || searchType==QuerySupport.Kind.CASE_INSENSITIVE_REGEXP){
                fieldSuffix = "_ci";
                queryText = queryText.toLowerCase();
            }            
            QuerySupport.Query q = qf.or(
                    qf.field("simple_name" + fieldSuffix, queryText, QuerySupport.Kind.PREFIX)
                    ,qf.field("full_name" + fieldSuffix, queryText, QuerySupport.Kind.PREFIX));
            Logger.log(q);
            Collection<? extends IndexResult> rs = q.execute((String[]) null);
            for(IndexResult r:rs){
                //Logger.log("value:" + r.getValue("cls"));
                //Logger.log("values:"+Arrays.toString(r.getValues("full_name")));
                String cls = r.getValue("full_name");
                ClassElementHandle clzEle = new ClassElementHandle(r.getFile(), cls);
                ClassDescriptor desc = new ClassDescriptor(clzEle,helper);
                list.add(desc);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return list;
    }

    @Override
    public Set<? extends Descriptor> getSymbols(Project project, String textForQuery, QuerySupport.Kind searchType, Helper helper) {
        Logger.log("query:" + textForQuery);
        Logger.log("query type:" + searchType);
        Logger.log("helper:" + helper);
        return Collections.emptySet();
    }

}
