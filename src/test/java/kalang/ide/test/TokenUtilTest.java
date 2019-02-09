package kalang.ide.test;


import kalang.compiler.antlr.KalangLexer;
import kalang.ide.lexer.KaTokenId;
import kalang.ide.lexer.TokenUtil;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Kason Yang
 */
public class TokenUtilTest {
    
    @Test
    public void testTokens(){
        KaTokenId[] tokens = TokenUtil.getAllTokens();
        for(int i=0;i<tokens.length;i++){
            System.out.println(tokens[i]);
        }
        String c = tokens[KalangLexer.CLASS].primaryCategory();
        String op = tokens[KalangLexer.ADD].primaryCategory();
        assertEquals("keyword",c);
        assertEquals("operator",op);
    }
    
}
