package kalang.ide.parser;

import kalang.compiler.compile.Diagnosis;
import org.netbeans.modules.csl.api.Severity;
import org.openide.filesystems.FileObject;

/**
 * @author Kason Yang
 */
public class KalangError implements org.netbeans.modules.csl.api.Error.Badging {

    private String displayName;
    private FileObject file;
    private Object[] parameters = new Object[0];
    private final Diagnosis diagnosis;

    public KalangError(Diagnosis error, FileObject file, String displayName) {
        this.displayName = displayName;
        this.file = file;
        this.diagnosis = error;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return diagnosis.getDescription();
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public FileObject getFile() {
        return file;
    }

    @Override
    public int getStartPosition() {
        return diagnosis.getOffset().startOffset;
    }

    @Override
    public int getEndPosition() {
        return diagnosis.getOffset().stopOffset;
    }

    @Override
    public boolean isLineError() {
        return false;
    }

    @Override
    public Severity getSeverity() {
        Diagnosis.Kind kind = diagnosis.getKind();
        switch (kind) {
            case NOTE:
                return Severity.INFO;
            case WARNING:
                return Severity.WARNING;
            case FATAL:
                return Severity.FATAL;
            case ERROR:
            default:
                return Severity.ERROR;
        }
    }

    @Override
    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public boolean showExplorerBadge() {
        return true;
    }

    public void setFile(FileObject file) {
        this.file = file;
    }

}
