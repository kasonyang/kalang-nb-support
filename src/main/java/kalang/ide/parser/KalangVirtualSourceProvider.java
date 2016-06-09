package kalang.ide.parser;

import java.io.*;
import java.util.*;
import kalang.compiler.KalangCompiler;
import kalang.compiler.KalangSource;
import kalang.ide.Logger;
import kalang.ide.compiler.NBKalangCompiler;
import kalang.tool.FileSystemCompiler;
import kalang.tool.JointFileSystemCompiler;
import kalang.tool.MemoryOutputManager;
import kalang.util.AstUtil;
import kalang.util.KalangSourceUtil;
import org.apache.commons.io.FileUtils;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */

@org.openide.util.lookup.ServiceProvider(service = org.netbeans.modules.java.preprocessorbridge.spi.VirtualSourceProvider.class)
public class KalangVirtualSourceProvider implements org.netbeans.modules.java.preprocessorbridge.spi.VirtualSourceProvider {

    @Override
    public Set<String> getSupportedExtensions() {
        return new HashSet<String>(
                Arrays.asList("kl", "kalang")
        );
    }

    @Override
    public boolean index() {
        return false;
    }

    @Override
    public void translate(Iterable<File> files, File root, Result result) {
        Logger.log("virtual source root:" + root);
        Map<String, File> class2File = new HashMap();
        FileObject rootFO = FileUtil.toFileObject(root);
        JointFileSystemCompiler cpler = NBKalangCompiler.createKalangCompiler(rootFO);
        for (File f : files) {
            try {
                KalangSource s = KalangSourceUtil.create(root, f);
                class2File.put(s.getClassName(), f);
                cpler.addSource(s);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        Collection<File> javaFiles = FileUtils.listFiles(root, new String[]{"java"}, true);
        if (javaFiles != null) {
            for (File j : javaFiles) {
                try {
                    cpler.addJavaSource(root, j);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        MemoryOutputManager om = new MemoryOutputManager();
        //cpler.setOutputManager(om);
        //cpler.compile();
        cpler.generateJavaStub(om);
        String[] classes = om.getClassNames();
        for (String c : classes) {
            byte[] bs = om.getBytes(c);
            assert bs != null;
            String code = new String(bs);
            Logger.log(c + ":" + code);
            result.add(class2File.get(c), AstUtil.getPackageName(c), AstUtil.getClassNameWithoutPackage(c), code);
        }
    }
}
