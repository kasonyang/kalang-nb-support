package kalang.ide.lexer;

import java.util.HashMap;
import kalang.compiler.antlr.KalangLexer;
import org.antlr.v4.runtime.Vocabulary;

/**
 *
 * @author Kason Yang
 */
public class TokenUtil {
    public static KaTokenId[] getAllTokens(){
        int tokenSize = 200;
        KaTokenId[] tokens = new KaTokenId[tokenSize];
        Vocabulary v = KalangLexer.VOCABULARY;
        String keywords = "override,abstract,assert,boolean,break,byte,case,catch,char,class,const,continue,constructor,default,do,double,else,enum,extends,final,finally,float,for,if,goto,implements,import,instanceof,int,interface,long,native,new,package,private,protected,public,return,short,static,strictfp,super,switch,synchronized,this,throw,throws,transient,try,void,volatile,while,null,var,val,foreach,as,true,false";
        String operators = "+,-,*,/,%,=,==,!=,+=,-=,*=,/=,%=,++,--";
        HashMap<String,String> cmap = new HashMap();
        for(String k:keywords.split(",")){
            cmap.put(k, "keyword");
        }
        for(String o:operators.split(",")){
            cmap.put(o, "operator");
        }
        cmap.put("identifier", "identifier");
        cmap.put("comment","comment");
        cmap.put("line_comment","comment");
        cmap.put("stringliteral","string");
        cmap.put("multilinestringliteral", "string");
        cmap.put("integerliteral", "number");
        cmap.put("floatingpointliteral", "number");
        cmap.put("booleanliteral", "keyword");
        for(int i=0;i<tokenSize;i++){
            String ln = v.getLiteralName(i);
            if(ln!=null){
                ln = ln.toLowerCase();
                if(ln.startsWith("'") && ln.endsWith("'")){
                    ln = ln.substring(1,ln.length()-1);
                }
            }
            String sn = v.getSymbolicName(i);
            if(sn==null) {
                sn = ln!=null?ln:("unknown" + i);
            } else {
                sn = sn.toLowerCase();
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
