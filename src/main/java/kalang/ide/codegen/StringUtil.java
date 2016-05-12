
package kalang.ide.codegen;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class StringUtil {

    public static String insert(String source, int caret, String insertStr) {
        if(caret<0) caret = 0;
        int strLen = source.length();
        if(caret>=strLen) caret = strLen-1;
        return source.substring(0,caret) + insertStr + source.substring(caret);
    }

}
