package kalang.ide.project;

import kalang.compiler.shell.ShellOptionParser;
import kalang.compiler.util.FilePathUtil;
import kalang.ide.Logger;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import java.io.*;
import java.net.URL;

/**
 * @author KasonYang
 */
public class KlShellProjectOpenedHook extends ProjectOpenedHook {

    private KlShellProject project;

    public KlShellProjectOpenedHook(KlShellProject project) {
        this.project = project;
    }

    @Override
    protected void projectOpened() {
        FileObject pd = project.getProjectDirectory();
        Logger.log("open project:" +  pd);
        ProgressHandle handle = ProgressHandleFactory.createHandle("Loading " + project.getName());
        handle.start();
        ShellOptionParser sop = new ShellOptionParser();
        FileObject optionsFile = pd.getFileObject(KlShellProject.PROJECT_FILE);
        FileReader optionsReader;
        try {
            optionsReader = new FileReader(FileUtil.toFile(optionsFile));
            sop.parse(new StringReader(""), optionsReader, true);
            File[] sourcePaths = sop.getSourcePaths();
            URL[] sourceUrls = new URL[sourcePaths.length + 1];
            sourceUrls[0] = pd.toURL();
            for (int i = 1; i < sourceUrls.length; i++) {
                sourceUrls[i] = FilePathUtil.toURL(sourcePaths[i - 1]);
            }
            URL[] classPaths = sop.getClassPaths();
            for (int i = 0; i < classPaths.length; i++) {
                if (classPaths[i].getFile().endsWith(".jar")) {
                    String cpUrl = classPaths[i].toString();
                    if (!cpUrl.startsWith("jar:")) {
                        cpUrl = "jar:" + cpUrl;
                    }
                    cpUrl += "!/";
                    classPaths[i] = new URL(cpUrl);
                }
            }
            KlShellClassProvider classPathProvider = project.getLookup().lookup(KlShellClassProvider.class);
            classPathProvider.setClassPaths(ClassPath.COMPILE, classPaths);
            classPathProvider.setClassPaths(ClassPath.SOURCE, sourceUrls);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handle.finish();
    }

    @Override
    protected void projectClosed() {
        Logger.log("close project:" +  project.getProjectDirectory());
    }
}
