package kalang.ide.elements;

import kalang.ast.ClassNode;
import org.netbeans.modules.csl.api.ElementKind;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Kason Yang
 */
public class ClassElementHandle extends KalangElementHandle{

    public ClassElementHandle(FileObject fo,String clazz) {
        super(fo,clazz,ElementKind.CLASS);
    }

}
