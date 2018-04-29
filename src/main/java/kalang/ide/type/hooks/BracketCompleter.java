package kalang.ide.type.hooks;

import javax.swing.text.BadLocationException;
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

    @Override
    public boolean beforeInsert(Context context) throws BadLocationException {
        return false;
    }

    @Override
    public void insert(MutableContext context) throws BadLocationException {
        int offset = context.getOffset();
        String text = context.getText();
        Logger.log("insert:typed=" + text);
        Logger.log("insert:offset=" + offset);
        if (this.isCompleteKeyword(text)) {
            String completeText = this.getCompleteText(text);
            Logger.log("insert:completeText=" + completeText);
            if (!completeText.isEmpty()) {
                String newText = text + completeText;
                context.setText(newText, text.length());
            }
        }
    }

    @Override
    public void afterInsert(Context context) throws BadLocationException {

    }

    @Override
    public void cancelled(Context context) {
    }

    private boolean isCompleteKeyword(String input) {
        String[] keywords = new String[]{"[", "("};
        for (String k : keywords) {
            if (k.equals(input)) {
                return true;
            }
        }
        return false;
    }

    private String getCompleteText(String input) {
        if ("[".equals(input)) {
            return "]";
        } else if ("(".equals(input)) {
            return ")";
        } else {
            return "";
        }
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
