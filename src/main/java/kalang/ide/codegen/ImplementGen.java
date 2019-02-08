package kalang.ide.codegen;

import java.util.LinkedList;
import java.util.List;
import javax.swing.text.JTextComponent;
import kalang.compiler.ast.ClassNode;
import kalang.compiler.core.MethodDescriptor;
import kalang.compiler.core.ObjectType;
import kalang.compiler.util.AstUtil;

/**
 *
 * @author Kason Yang
 */
public class ImplementGen extends MethodGenerator{

    public ImplementGen(JTextComponent jTextComponent) {
        super(jTextComponent,"Implement methods...");
    }

    @Override
    protected List<MethodDescriptor> getMethodNodes(ClassNode ast) {
        List<MethodDescriptor> unimplementsList = new LinkedList();
        for(ObjectType itf:ast.getInterfaces()){
            //TODO get all unimplemented methods
            unimplementsList.addAll(AstUtil.getUnimplementedMethod(ast, itf));
        }
        return unimplementsList;
    }

}
