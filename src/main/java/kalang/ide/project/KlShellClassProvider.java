package kalang.ide.project;

import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.spi.java.classpath.ClassPathProvider;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.filesystems.FileObject;

import java.net.URL;

/**
 * @author KasonYang
 */
public class KlShellClassProvider implements ClassPathProvider {

    private URL[] classPaths = new URL[0];

    private URL[] sourcePaths = new URL[0];

    @Override
    public ClassPath findClassPath(FileObject file, String type) {
        URL[] urls;
        if (ClassPath.SOURCE.equals(type)) {
            urls = sourcePaths;
        } else if (ClassPath.COMPILE.equals(type)) {
            urls = classPaths;
        } else {
            urls = new URL[0];
        }
        return ClassPathSupport.createClassPath(urls);
    }

    public void setClassPaths(URL[] urls) {
        this.classPaths = urls;
    }

    public void setSourcePaths(URL[] urls) {
        this.sourcePaths = urls;
    }

}
