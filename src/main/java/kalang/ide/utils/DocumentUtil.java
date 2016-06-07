package kalang.ide.utils;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.modules.editor.indent.api.IndentUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author Kason Yang
 */
public class DocumentUtil {
    
    public static String getIndent(JTextComponent textComponent){
        Document doc = textComponent.getDocument();
        int offset = textComponent.getCaret().getDot();
        try {
            int lineStartOffset = IndentUtils.lineStartOffset(doc, offset-1);
            int indentSize = IndentUtils.lineIndent(doc, lineStartOffset);
            String indent = IndentUtils.createIndentString(doc, indentSize);
            return indent;
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
            return "";
        }
    }

}
