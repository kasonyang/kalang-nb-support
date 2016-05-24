package kalang.ide.utils;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import javax.swing.text.Document;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class FileObjectUtil {
    
    public static FileObject getFileObject(ClassPath cp,String className){
        String fn = className.replace(".", "/");
        FileObject res = cp.findResource(fn + ".kl");
        return res;
    }

    public static FileObject getFileObject(Document doc) {
        Object stremDesc = doc.getProperty(Document.StreamDescriptionProperty);
        if (stremDesc instanceof FileObject) {
            return (FileObject) stremDesc;
        } else if (stremDesc instanceof DataObject) {
            return ((DataObject) stremDesc).getPrimaryFile();
        } else {
            return null;
        }
    }

}
