package kalang.ide.type.hooks;

import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;

import kalang.ide.KaLanguage;
import kalang.ide.Logger;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.typinghooks.TypedTextInterceptor;

/**
 *
 * @author Kason Yang
 */
public class BracketCompleter implements TypedTextInterceptor {

    private final static char[] COMPLETE_KEYS = "()[]".toCharArray();

    @Override
    public boolean beforeInsert(Context context) throws BadLocationException {
        return false;
    }

    @Override
    public void insert(MutableContext context) throws BadLocationException {
        String text = context.getText();
        char[] chars = text.toCharArray();
        if (chars.length != 1) {
            return;
        }
        char ch = chars[0];
        String completeText = getCompleteText(ch);
        if (!completeText.isEmpty()) {
            String newText = text + completeText;
            context.setText(newText, text.length());
        }
    }

    @Override
    public void afterInsert(Context context) throws BadLocationException {
        Caret caret = context.getComponent().getCaret();
        Document doc = context.getDocument();
        int offset = context.getOffset();
        char[] chars = context.getText().toCharArray();
        if (chars.length != 1) {
            return;
        }
        char ch = chars[0];
        if (!isCompletedChar(ch)) {
            return;
        }
        char currentChar = context.getDocument().getText(offset, 1).charAt(0);
        if (currentChar != ch) {
            return;
        }
        doc.remove(offset, 1);
        caret.setDot(offset + 1);
    }

    @Override
    public void cancelled(Context context) {
    }

    private boolean isCompletedChar(char ch) {
        for (int i = 1; i < COMPLETE_KEYS.length; i += 2) {
            if (ch == COMPLETE_KEYS[i]) {
                return true;
            }
        }
        return false;
    }

    private String getCompleteText(char ch) {
        for (int i = 0; i < COMPLETE_KEYS.length; i += 2) {
            if (COMPLETE_KEYS[i] == ch) {
                return new String(COMPLETE_KEYS, i+1, 1);
            }
        }
        return "";
    }

    
    @MimeRegistration(mimeType = KaLanguage.MIME_TYPE, service = TypedTextInterceptor.Factory.class)
    public static class Factory implements TypedTextInterceptor.Factory {

        @Override
        public TypedTextInterceptor createTypedTextInterceptor(MimePath mimePath) {
            Logger.log("creating bracket completer");
            return new BracketCompleter();
        }
        
    }

}
