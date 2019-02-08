/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kalang.ide.test;


import kalang.compiler.antlr.KalangLexer;
import kalang.ide.lexer.KaTokenId;
import kalang.ide.lexer.TokenUtil;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class TokenUtilTest {
    
    @Test
    public void testTokens(){
        KaTokenId[] tokens = TokenUtil.getAllTokens();
        for(int i=0;i<tokens.length;i++){
            System.out.print(tokens[i]);
        }
        String c = tokens[KalangLexer.CLASS].primaryCategory();
        String op = tokens[KalangLexer.ADD].primaryCategory();
        assertEquals("keyword",c);
        assertEquals("operator",op);
    }
    
}
