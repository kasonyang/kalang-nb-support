package kalang.ide.parser;
import kalang.compiler.compile.Diagnosis;
import org.netbeans.modules.csl.api.Severity;
import org.openide.filesystems.FileObject;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class KalangError implements org.netbeans.modules.csl.api.Error.Badging{

    private String displayName;
    private String description = "<unknown error>";
    private String key;
    private FileObject file;
    private int startPosition = 0;
    private int endPosition = 1;
    private boolean isLineError = false;
    private Severity severity = Severity.ERROR;
    private Object[] parameters = new Object[0];
    private boolean showExplorerBadge = true;
    private final Diagnosis Diagnosis;

    public KalangError(Diagnosis error, FileObject file, String key,String displayName, String description) {        
        this(error,file, key, displayName, description,0,1);
    }
    
    public KalangError(Diagnosis error, FileObject file, String key,String displayName, String description,int startPosition,int endPosition){
        this(error,file, key, displayName, description, startPosition, endPosition, Severity.ERROR);
    }
    
    public KalangError(Diagnosis error,FileObject file, String key,String displayName, String description,int startPosition,int endPosition,Severity severity){
        this.displayName = displayName;
        this.description = description;
        this.key = key;
        this.file = file;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.severity = severity;
        this.Diagnosis = error;
    }
    
    
    
    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public FileObject getFile() {
        return file;
    }

    @Override
    public int getStartPosition() {
        return startPosition;
    }

    @Override
    public int getEndPosition() {
        return endPosition ;
    }

    @Override
    public boolean isLineError() {
        return isLineError;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public boolean showExplorerBadge() {
        return showExplorerBadge;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFile(FileObject file) {
        this.file = file;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public void setIsLineError(boolean isLineError) {
        this.isLineError = isLineError;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public void setShowExplorerBadge(boolean showExplorerBadge) {
        this.showExplorerBadge = showExplorerBadge;
    }

    public Diagnosis getDiagnosis() {
        return Diagnosis;
    }
    
}
