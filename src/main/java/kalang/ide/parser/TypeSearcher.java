
package kalang.ide.parser;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import org.netbeans.modules.csl.api.IndexSearcher;
import org.netbeans.modules.parsing.spi.indexing.support.QuerySupport;
import org.openide.util.Exceptions;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class TypeSearcher implements IndexSearcher{

    @Override
    public Set<? extends Descriptor> getTypes(org.netbeans.api.project.Project prjct, String string, QuerySupport.Kind kind, Helper helper) {
        try {
            QuerySupport q = QuerySupport.forRoots("kalang", 1, prjct.getProjectDirectory().getChildren());
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        //TODO support needed
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<? extends Descriptor> getSymbols(org.netbeans.api.project.Project prjct, String string, QuerySupport.Kind kind, Helper helper) {
        //TODO support needed
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
