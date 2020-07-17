package kalang.ide.type.hooks;

import kalang.ide.KaLanguage;
import kalang.ide.Logger;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.typinghooks.TypedTextInterceptor;

import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;

/**
 *
 * @author Kason Yang
 */
public class BracketTypedInterceptor extends AbstractBracketInterceptor implements TypedTextInterceptor {

    @Override
    public boolean beforeInsert(Context context) throws BadLocationException {
        return false;
    }

    @Override
    public void insert(MutableContext context) throws BadLocationException {

    }

    @Override
    public void afterInsert(Context context) throws BadLocationException {
        Caret caret = context.getComponent().getCaret();
        Document doc = context.getDocument();
        int offset = context.getOffset();
        String text = context.getText();
        if (text.length() != 1) {
            return;
        }
        char ch = text.charAt(0);
        String completeText = getCompleteText(ch);
        if (completeText.isEmpty() && !isCompletedChar(ch)) {
            return;
        }
        if (!isTokenText(doc, offset, text)) {
            return;
        }
        if (!completeText.isEmpty()) {
            doc.insertString(offset + 1, completeText, null);
            caret.setDot(offset + 1);
        } else {
            char currentChar = context.getDocument().getText(offset, 1).charAt(0);
            if (currentChar == ch) {
                doc.remove(offset, 1);
                caret.setDot(offset + 1);
            }
        }
    }

    @Override
    public void cancelled(Context context) {
    }

    
    @MimeRegistration(mimeType = KaLanguage.MIME_TYPE, service = TypedTextInterceptor.Factory.class)
    public static class Factory implements TypedTextInterceptor.Factory {

        @Override
        public TypedTextInterceptor createTypedTextInterceptor(MimePath mimePath) {
            Logger.log("creating bracket completer");
            return new BracketTypedInterceptor();
        }
        
    }

}
