
package kalang.ide.utils;
import java.io.*;
import static java.lang.reflect.Modifier.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import org.netbeans.modules.csl.api.Modifier;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class ModifierUtil {

    public static Set<Modifier> getModifier(int modifier) {
        HashSet set = new HashSet();
        if(isStatic(modifier)){
            set.add(Modifier.STATIC);
        }
        if(isPublic(modifier)){
            set.add(Modifier.PUBLIC);
        }else if(isProtected(modifier)){
            set.add(Modifier.PROTECTED);
        }else if(isPrivate(modifier)){
            set.add(Modifier.PRIVATE);
        }
        return set;
    }

}
