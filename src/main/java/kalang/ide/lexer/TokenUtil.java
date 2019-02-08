package kalang.ide.lexer;

import java.util.HashMap;
import kalang.compiler.antlr.KalangLexer;
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
                + ",public,protected,private,synchronized"
                + ",static,class,interface,extends,implements,var,as,val"
                + ",return,while,for,do,if,else"
                + ",new,override,import,throw,throws,final,constructor"
                + ",try,catch";
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
        cmap.put("NUMBER", "number");
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
