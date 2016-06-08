
package kalang.ide.codegen;
import javax.swing.text.JTextComponent;
import kalang.ide.utils.DocumentUtil;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.util.Lookup;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class MainMethodGen implements CodeGenerator{

    private final JTextComponent jTextComp;

    public MainMethodGen(Lookup context) {
        jTextComp = context.lookup(JTextComponent.class);
    }

    @Override
    public String getDisplayName() {
        return "Generate main()";
    }
    

    @Override
    public void invoke() {
        String indent = DocumentUtil.getIndent(jTextComp);
        String mainString = "static void main(String[] args){\n" + indent + indent +"\n" + indent +"}";
        jTextComp.replaceSelection(mainString);

    }
    
}
