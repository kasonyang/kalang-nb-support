
package kalang.ide.parser;
import java.io.*;
import java.util.*;
import kalang.compiler.KalangCompiler;
import kalang.compiler.KalangSource;
import kalang.ide.Logger;
import kalang.ide.compiler.NBKalangCompiler;
import kalang.tool.FileSystemCompiler;
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
//TODO virtual source does not work
@org.openide.util.lookup.ServiceProvider(service=org.netbeans.modules.java.preprocessorbridge.spi.VirtualSourceProvider.class)
public class KalangVirtualSource implements org.netbeans.modules.java.preprocessorbridge.spi.VirtualSourceProvider{

    @Override
    public Set<String> getSupportedExtensions() {
        //return Collections.singleton("kl");
        
        return new HashSet<String>(
                Arrays.asList("kl","kalang")
        );
    }

    @Override
    public boolean index() {
        return false;
    }

    @Override
    public void translate(Iterable<File> files, File root, Result result) {
        Logger.log("virtual source root:" + root);
        Map<String,File> class2File = new HashMap();
        FileObject rootFO = FileUtil.toFileObject(root);
        FileSystemCompiler cpler = NBKalangCompiler.createKalangCompiler(rootFO);
        for(File f:files){
            try {                
                KalangSource s = KalangSourceUtil.create(root, f);
                class2File.put(s.getClassName(),f);
                cpler.addSource(s);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        Collection<File> javaFiles = FileUtils.listFiles(root, new String[]{"java"},true);
        if(javaFiles!=null){
            for(File j:javaFiles){
                try {
                    cpler.addSource(root, j);
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
        for(String c:classes){
            byte[] bs = om.getBytes(c);
            assert bs!=null;
            String code = new String(bs);
            Logger.log(c + ":" + code);
            result.add(class2File.get(c), AstUtil.getPackageName(c), AstUtil.getClassNameWithoutPackage(c), code);
        }
//        
//        Logger.log("v root:" + file);
//        int i=0;
//        Logger.log(itrbl.toString());
//        for(File f:itrbl){
//            i++;
//            //FileObject fo = FileUtil.toFileObject(f);
//            //f = FileUtil.normalizeFile(f);
//            String pk = "javaapplication1";
//            FileObject fo = FileUtil.toFileObject(f);
//            String clsName = fo.getName();
//            String code = "package " + pk + " ; \n"
//                    + "public class " + clsName + " {\n" +
//                        "    public static void main(String[] args) {\n" +
//                        "        \n" +
//                        "    }\n" +
//                        "}";
//            Logger.log("translated:"+code);
//            result.add(f, pk,clsName, code);
            //    Logger.log("translated:" + i);
 
        }
}
