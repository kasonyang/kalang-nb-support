package kalang.ide.codegen;

import kalang.ast.MethodNode;
import kalang.util.AstUtil;

/**
 *
 * @author Kason Yang
 */
public class MethodItem {
    
    protected MethodNode method;

    public MethodItem(MethodNode method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return AstUtil.getMethodDescription(method);
    }

    public MethodNode getMethod() {
        return method;
    }
    
    

}
