package kalang.ide.type.hooks;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import kalang.ide.KaLanguage;
import kalang.ide.Logger;
import kalang.ide.lexer.KaTokenId;
import kalang.ide.utils.DocumentUtil;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
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
        Logger.log("pre char=" + preChar);
        if ("{".equals(preChar) && isMissingRightBracket(doc)){
            String indent = DocumentUtil.getIndent(doc, preCaretOffset);
            String text = "\n" + indent + "\n" + indent + "}";
            context.setText(text, 0, 1+indent.length());
        }
    }

    @Override
    public void afterInsert(Context context) throws BadLocationException {
    }

    @Override
    public void cancelled(Context context) {
    }

    private boolean isMissingRightBracket(Document doc) {
        TokenHierarchy<Document> th = TokenHierarchy.get(doc);
        TokenSequence<KaTokenId> ts = (TokenSequence<KaTokenId>) th.tokenSequence();
        int leftCount = 0;
        int rightCount = 0;
        ts.moveStart();
        while (ts.moveNext()) {
            Token<KaTokenId> token = ts.token();
            if (token.text().length() != 1) {
                continue;
            }
            String tokenText = token.text().toString();
            if ("{".equals(tokenText)) {
                leftCount ++;
            } else if ("}".equals(tokenText)) {
                rightCount ++;
            }
        }
        return leftCount > rightCount;
    }
    
    @MimeRegistration(mimeType = KaLanguage.MIME_TYPE, service = TypedBreakInterceptor.Factory.class)
    public static class Factory implements TypedBreakInterceptor.Factory {

        @Override
        public TypedBreakInterceptor createTypedBreakInterceptor(MimePath mimePath) {
            return new BreakHandler();
        }


        
    }
    
}
