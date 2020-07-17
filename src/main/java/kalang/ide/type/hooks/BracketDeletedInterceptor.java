package kalang.ide.type.hooks;

import kalang.ide.KaLanguage;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.typinghooks.DeletedTextInterceptor;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * @author KasonYang
 */
public class BracketDeletedInterceptor extends AbstractBracketInterceptor implements DeletedTextInterceptor {
    @Override
    public boolean beforeRemove(Context context) throws BadLocationException {
        if (!context.isBackwardDelete()) {
            return false;
        }
        String deleteText = context.getText();
        if (deleteText.length() != 1) {
            return false;
        }
        char deleteChar = deleteText.charAt(0);
        String completeText = getCompleteText(deleteChar);
        if (completeText.isEmpty()) {
            return false;
        }
        Document doc = context.getDocument();
        int caret = context.getOffset();
        if (!isTokenText(doc, caret - 1, deleteText) || !isTokenText(doc, caret, completeText)) {
            return  false;
        }
        doc.remove(caret, completeText.length());
        return false;
    }

    @Override
    public void remove(Context context) throws BadLocationException {

    }

    @Override
    public void afterRemove(Context context) throws BadLocationException {

    }

    @Override
    public void cancelled(Context context) {

    }

    @MimeRegistration(mimeType = KaLanguage.MIME_TYPE, service = DeletedTextInterceptor.Factory.class)
    public static class Factory implements DeletedTextInterceptor.Factory {

        @Override
        public DeletedTextInterceptor createDeletedTextInterceptor(MimePath mimePath) {
            return new BracketDeletedInterceptor();
        }

    }
}
