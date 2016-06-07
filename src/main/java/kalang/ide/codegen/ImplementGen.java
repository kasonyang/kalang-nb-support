package kalang.ide.codegen;

import java.util.LinkedList;
import java.util.List;
import javax.swing.text.JTextComponent;
import kalang.ast.ClassNode;
import kalang.ast.MethodNode;
import kalang.util.AstUtil;

/**
 *
 * @author Kason Yang
 */
public class ImplementGen extends MethodGenerator{

    public ImplementGen(JTextComponent jTextComponent) {
        super(jTextComponent,"Implement methods...");
    }

    @Override
    protected List<MethodNode> getMethodNodes(ClassNode ast) {
        List<MethodNode> unimplementsList = new LinkedList();
        for(ClassNode itf:ast.interfaces){
            //TODO get all unimplemented methods
            unimplementsList.addAll(AstUtil.getUnimplementedMethod(ast, itf));
        }
        return unimplementsList;
    }

}
