package kalang.ide;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.modules.editor.indent.api.IndentUtils;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.IndentTask;

public class KaIndentTask implements IndentTask {

    private Context context;

    KaIndentTask(Context context) {
        this.context = context;
    }

    @Override
    public void reindent() throws BadLocationException {
        Document doc = context.document();
        int offset = context.caretOffset();
        int lineStartOffset = IndentUtils.lineStartOffset(doc, offset-1);
        int indentSize = IndentUtils.lineIndent(doc, lineStartOffset);
        String indent = IndentUtils.createIndentString(doc, indentSize);
        doc.insertString(offset,indent, null);
    }

    @Override
    public ExtraLock indentLock() {
        return null;
    }
    
}