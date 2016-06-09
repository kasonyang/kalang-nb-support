package kalang.ide.elements;

import java.util.Collections;
import java.util.Set;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.Modifier;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.spi.ParserResult;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Kason Yang
 */
public class KalangElementHandle implements ElementHandle{
    
    protected FileObject fo;
    private String name;
    private ElementKind kind;

    public KalangElementHandle(FileObject fo, String name, ElementKind kind) {
        this.fo = fo;
        this.name = name;
        this.kind = kind;
    }

    @Override
    public FileObject getFileObject() {
        return fo;
    }

    @Override
    public String getMimeType() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIn() {
        return null;
    }

    @Override
    public ElementKind getKind() {
        return kind;
    }

    @Override
    public Set<Modifier> getModifiers() {
        //TODO set modifier
        return Collections.singleton(Modifier.PUBLIC);
    }

    @Override
    public boolean signatureEquals(ElementHandle handle) {
        //TODO impl signEquals
        return equals(handle);
    }

    @Override
    public OffsetRange getOffsetRange(ParserResult result) {
        //TODO impl getOffsetRange
        return OffsetRange.NONE;
    }

}
