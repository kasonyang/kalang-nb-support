package kalang.ide.codegen;

import java.util.Arrays;
import java.util.List;
import javax.swing.text.JTextComponent;
import kalang.ast.ClassNode;
import kalang.ast.MethodNode;
import kalang.util.AstUtil;

/**
 *
 * @author Kason Yang
 */
public class OverrideMethodGen extends MethodGenerator{

    public OverrideMethodGen(JTextComponent jTextComponent) {
        super(jTextComponent,"Override methods...");
    }

    @Override
    protected List<MethodNode> getMethodNodes(ClassNode clazz) {
        MethodNode[] mds = AstUtil.listAccessibleMethods(clazz, clazz, true);
        return Arrays.asList(mds);
    }

}
