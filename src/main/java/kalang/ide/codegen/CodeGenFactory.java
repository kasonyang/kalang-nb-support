
package kalang.ide.codegen;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.util.Lookup;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
@MimeRegistration(mimeType = "text/x-kalang", service = CodeGenerator.Factory.class)
public class CodeGenFactory implements CodeGenerator.Factory {

    @Override
    public List<? extends CodeGenerator> create(Lookup context) {
        List<CodeGenerator> list = new LinkedList<CodeGenerator>();
        list.add(new MainMethodGen(context));
        list.add(new OverrideGen(context));
        return list;
    }

}
