package kalang.ide.compiler;

import kalang.compiler.compile.Configuration;
import kalang.compiler.compile.KalangCompiler;
import kalang.compiler.compile.SourceLoader;
import kalang.compiler.compile.jvm.JvmAstLoader;
import kalang.compiler.tool.FileSystemSourceLoader;
import kalang.ide.utils.ClassPathHelper;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kason Yang
 */
public class NBKalangCompiler {
    
    private static Map<ClassPath,ExtendKalangCompiler> cachedCompilers = new HashMap();

    public static KalangCompiler compile(FileObject fo) throws IOException {
        KalangCompiler cp = createKalangCompiler(fo);
        String className = ClassPathHelper.getClassName(fo);
        cp.addSource(className, fo.asText(), fo.getName());
        cp.compile();
        return cp;
    }

    public static ExtendKalangCompiler createKalangCompiler(FileObject fo) {
        final ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
        ClassPath compilePath = ClassPath.getClassPath(fo, ClassPath.COMPILE);
        return createKalangCompiler(sourcePath, compilePath);
    }
    
    public static ExtendKalangCompiler createKalangCompiler(ClassPath sourcePath,ClassPath compilePath) {
        //TODO sourcePath may change?
        ExtendKalangCompiler compiler = cachedCompilers.get(compilePath);
        if (compiler==null) {
            ClassLoader classLoader = NBKalangCompiler.class.getClassLoader();
            if (compilePath != null) {
                classLoader = ClassPathHelper.createClassLoader(compilePath);
            }
            Configuration conf = new Configuration();
            conf.setAstLoader(new JvmAstLoader(null, classLoader));
            compiler = new ExtendKalangCompiler(conf);
            if (sourcePath != null) {
                File[] srcRoots = ClassPathHelper.getRootFiles(sourcePath);
                SourceLoader sourceLoader = new FileSystemSourceLoader(srcRoots, new String[]{"kl", "kalang"}, "utf8");
                compiler.setSourceLoader(sourceLoader);
            }
            cachedCompilers.put(compilePath, compiler);
        }
        return compiler;
    }

}
