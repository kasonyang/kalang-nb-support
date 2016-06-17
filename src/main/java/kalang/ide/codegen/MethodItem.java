package kalang.ide.codegen;

import kalang.ast.MethodNode;
import kalang.core.MethodDescriptor;
import kalang.util.AstUtil;
import kalang.util.MethodUtil;

/**
 *
 * @author Kason Yang
 */
public class MethodItem {
    
    protected MethodDescriptor method;

    public MethodItem(MethodDescriptor method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method.toString();
    }

    public MethodDescriptor getMethod() {
        return method;
    }
    
    

}
