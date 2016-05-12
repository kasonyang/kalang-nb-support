package kalang.ide.utils;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class NBClassNameUtil {
    
    public static List<String> getClassesNames(File path,String suffix){
        List<File> files = FileSystemUtil.listFiles(path, suffix);
        return getClassNames(path, files, suffix);
    }
    
    public static String getClassName(File path,File file,String suffix){
        String fp = file.getAbsolutePath();
            String pp = path.getAbsolutePath();
            String fn = fp.substring(pp.length() + 1);
            if (!fn.endsWith(suffix)) {
                return null;
            }
            String baseName = fn.substring(0, fn.length() - suffix.length());
            return (baseName.replace('\\', '.')); 
    }

    public static List<String> getClassNames(File path, List<File> files, String suffix) {
        List<String> names = new LinkedList();
        for (File f : files) {
            String className = getClassName(path, f, suffix);
            if(className!=null){
                names.add(className);
            }
        }
        return names;
    }
}
