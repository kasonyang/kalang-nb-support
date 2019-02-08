package kalang.ide.codegen;

import java.util.Arrays;
import java.util.List;
import javax.swing.text.JTextComponent;
import kalang.compiler.ast.ClassNode;
import kalang.compiler.core.MethodDescriptor;
import kalang.compiler.core.Types;


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
        MethodDescriptor[] mds = Types.getClassType(clazz).getMethodDescriptors(clazz, true,true);
        return Arrays.asList(mds);
    }

}
