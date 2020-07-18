package kalang.ide.project;

import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.spi.java.classpath.ClassPathProvider;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.filesystems.FileObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KasonYang
 */
public class KlShellClassProvider implements ClassPathProvider {

    private Map<String, ClassPath> classPathMap = new HashMap<String, ClassPath>(5);
    private Map<String, URL[]> urlsMap = new HashMap<String, URL[]>(5);

    @Override
    public ClassPath findClassPath(FileObject file, String type) {
        ClassPath cp = classPathMap.get(type);
        if (cp == null) {
            URL[] urls = urlsMap.get(type);
            cp = ClassPathSupport.createClassPath(urls == null ? new URL[0] : urls);
            classPathMap.put(type, cp);
        }
        return cp;
    }

    public void setClassPaths(String type, URL[] urls) {
        urlsMap.put(type, urls);
        classPathMap.remove(type);
    }

}
