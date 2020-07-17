package kalang.ide.type.hooks;

import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;

import javax.swing.text.Document;

/**
 * @author KasonYang
 */
public class AbstractBracketInterceptor {

    private final static char[] COMPLETE_KEYS = "()[]".toCharArray();

    protected boolean isCompletedChar(char ch) {
        for (int i = 1; i < COMPLETE_KEYS.length; i += 2) {
            if (ch == COMPLETE_KEYS[i]) {
                return true;
            }
        }
        return false;
    }

    protected String getCompleteText(char ch) {
        for (int i = 0; i < COMPLETE_KEYS.length; i += 2) {
            if (COMPLETE_KEYS[i] == ch) {
                return new String(COMPLETE_KEYS, i+1, 1);
            }
        }
        return "";
    }

    protected boolean isTokenText(Document doc, int caret, String text) {
        TokenHierarchy<Document> th = TokenHierarchy.get(doc);
        TokenSequence ts = th.tokenSequence();
        ts.move(caret);
        if (!ts.moveNext()) {
            return false;
        }
        CharSequence tokenText = ts.token().text();
        return text.length() == tokenText.length() && text.equals(tokenText.toString());
    }


}
