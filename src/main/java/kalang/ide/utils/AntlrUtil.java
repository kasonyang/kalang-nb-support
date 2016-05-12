
package kalang.ide.utils;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import kalang.ide.Logger;
import org.antlr.v4.runtime.Token;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class AntlrUtil {
    
    public static int getTokenIndexByCaretOffset(Token[] tokens,int caretOffset){
        int[] startOffset = new int[tokens.length];
        for (int i=0;i<tokens.length;i++) {
            startOffset[i] = tokens[i].getStartIndex();
        }
        Logger.log(Arrays.toString(startOffset));
        int idx = Arrays.<Token>binarySearch(startOffset,caretOffset);
        Logger.log("search idx:"+idx);
        if(idx<0){
            idx = -idx - 2;
            if(caretOffset>tokens[idx].getStopIndex()){
                return -1;
            }
        }
        return idx;
    }
    
    public static Token[] filterTokenByChannel(Token[] tokens,int channel){
        ArrayList<Token> list = new ArrayList<Token>(tokens.length);
        for(Token t:tokens){
            if(t.getChannel()==channel){
                list.add(t);
            }
        }
        return list.toArray(new Token[0]);
    }

}
