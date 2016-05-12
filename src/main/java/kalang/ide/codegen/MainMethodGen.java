
package kalang.ide.codegen;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import kalang.ide.utils.FileObjectUtil;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class MainMethodGen implements CodeGenerator{
    
    private static String mainString = "static void main(String[] args){}";

    private final JTextComponent jTextComp;

    public MainMethodGen(Lookup context) {
        jTextComp = context.lookup(JTextComponent.class);
    }

    @Override
    public String getDisplayName() {
        return "main method";
    }
    

    @Override
    public void invoke() {
        jTextComp.replaceSelection(mainString);
//        Document doc = jTextComp.getDocument();
//        String source = jTextComp.getText();
//        int caret = jTextComp.getCaretPosition();
//        String newSource = StringUtil.insert(source,caret,mainString);
//        jTextComp.setText(newSource);
//        jTextComp.setCaretPosition(caret+mainString.length()-1);
    }
    

}
