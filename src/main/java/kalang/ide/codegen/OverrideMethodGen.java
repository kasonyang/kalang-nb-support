package kalang.ide.codegen;

import java.util.Arrays;
import java.util.List;
import javax.swing.text.JTextComponent;
import kalang.ast.ClassNode;
import kalang.ast.MethodNode;
import kalang.core.MethodDescriptor;
import kalang.core.Types;
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
    protected List<MethodDescriptor> getMethodNodes(ClassNode clazz) {
        MethodDescriptor[] mds = Types.getClassType(clazz).getMethodDescriptors(clazz, true);
        return Arrays.asList(mds);
    }

}
