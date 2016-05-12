/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kalang.ide.lexer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import kalang.antlr.KalangLexer;
import org.antlr.v4.runtime.Vocabulary;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class TokenUtil {
    public static KaTokenId[] getAllTokens(){
        int tokenSize = 200;
        KaTokenId[] tokens = new KaTokenId[tokenSize];
        Vocabulary v = KalangLexer.VOCABULARY;
        String keywords = "char,byte,int,long,double,float,void,null"
                + ",public,protected,private"
                + ",static,class,interface,extends,implements,var,as"
                + ",return,while,for,do";
        String operators = "+,-,*,/,%,=,==,!=,+=,-=,*=,/=,%=,++,--";
        HashMap<String,String> cmap = new HashMap();
        for(String k:keywords.split(",")){
            cmap.put(k.toUpperCase(), "keyword");
        }
        for(String o:operators.split(",")){
            cmap.put(o, "operator");
        }
        cmap.put("IDENTIFIER", "identifier");
        cmap.put("COMMENT","comment");
        cmap.put("LINE_COMMENT","comment");
        cmap.put("STRINGLITERAL","string");
        
        for(int i=0;i<tokenSize;i++){
            String ln = v.getLiteralName(i);
            if(ln!=null){
                ln = ln.toUpperCase();
                if(ln.startsWith("'") && ln.endsWith("'")){
                    ln = ln.substring(1,ln.length()-1);
                }
            }
            String sn = v.getSymbolicName(i);
            if(sn==null) {
                sn = ln!=null?ln:("UNKNOWN" + i);
            }
            String c = cmap.get(ln);
            if(c==null) c = cmap.get(sn);
            if(c==null) c = "default";
            KaTokenId tk = new KaTokenId(sn,c,i);
            tokens[i] = tk;
        }
        return tokens;
    }
}
