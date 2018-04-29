package kalang.ide.type.hooks;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import kalang.ide.KaLanguage;
import kalang.ide.Logger;
import kalang.ide.utils.DocumentUtil;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.typinghooks.TypedBreakInterceptor;

/**
 *
 * @author Kason Yang
 */
public class BreakHandler implements TypedBreakInterceptor {

    @Override
    public boolean beforeInsert(Context context) throws BadLocationException {
        return false;
    }

    @Override
    public void insert(MutableContext context) throws BadLocationException {
        int caretOffset = context.getCaretOffset();
        Logger.log("caret offset:" + caretOffset);
        int preCaretOffset = caretOffset - 1;
        if (preCaretOffset<0){
            return;
        }
        Document doc = context.getDocument();
        String preChar = doc.getText(preCaretOffset, 1);
        Logger.log("prechar=" + preChar);
        if ("{".equals(preChar)){
            String ident = DocumentUtil.getIndent(doc, preCaretOffset);
            String text = "\n" + ident + "\n" + ident + "}";
            context.setText(text, 0, 1+ident.length());
        }
    }

    @Override
    public void afterInsert(Context context) throws BadLocationException {
    }

    @Override
    public void cancelled(Context context) {
    }
    
    @MimeRegistration(mimeType = KaLanguage.MIME_TYPE, service = TypedBreakInterceptor.Factory.class)
    public static class Factory implements TypedBreakInterceptor.Factory {

        @Override
        public TypedBreakInterceptor createTypedBreakInterceptor(MimePath mimePath) {
            return new BreakHandler();
        }


        
    }
    
}
