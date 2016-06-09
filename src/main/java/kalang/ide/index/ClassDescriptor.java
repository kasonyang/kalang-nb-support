package kalang.ide.index;

import javax.swing.Icon;
import kalang.ide.elements.ClassElementHandle;
import org.netbeans.api.project.Project;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.IndexSearcher;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Kason Yang
 */
public class ClassDescriptor extends IndexSearcher.Descriptor{

    private ClassElementHandle element;
    private IndexSearcher.Helper helper;
    
    
    ClassDescriptor(ClassElementHandle clzEle, IndexSearcher.Helper helper) {
        element = clzEle;
        this.helper = helper;
    }
    
    

    @Override
    public ElementHandle getElement() {
        return element;
    }

    @Override
    public String getSimpleName() {
        return element.getName();
    }

    @Override
    public String getOuterName() {
        return  element.getName();
    }

    @Override
    public String getTypeName() {
        return  element.getName();
    }

    @Override
    public String getContextName() {
        return  element.getName();
    }

    @Override
    public Icon getIcon() {
        return helper.getIcon(element);
    }

    @Override
    public String getProjectName() {
        return null;
    }

    @Override
    public Icon getProjectIcon() {
        return null;
    }

    @Override
    public FileObject getFileObject() {
        return element.getFileObject();
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public void open() {
        FileObject fo = getFileObject();
        if(fo!=null){
            helper.open(fo, element);
        }
    }

}
