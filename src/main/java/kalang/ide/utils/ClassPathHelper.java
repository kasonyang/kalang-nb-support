/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalang.ide.utils;

import java.io.*;
import kalang.compiler.util.ClassNameUtil;
import kalang.ide.Logger;
import kalang.ide.compiler.MultiClassLoader;

import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class ClassPathHelper {
    
    public static String getSourceRoot(FileObject fo){
        ClassPath classPath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
        String srcPath;
        if(classPath==null){
            srcPath = fo.getParent().getPath();
        }else{
            FileObject owner = classPath.findOwnerRoot(fo);
            if(owner==null){
                srcPath = fo.getParent().getPath();
            }else{
                srcPath = owner.getPath();
            }
        }
        return srcPath;
    }

    public static String getClassName(FileObject fo) {
        String srcPath = getSourceRoot(fo);
        Logger.log("Source path:" + srcPath);
        String fpath = fo.getPath();
        Logger.log("file path:" + fpath);
        return ClassNameUtil.getClassName(new File(srcPath), new File(fpath));
    }
    
    public static ClassLoader createClassLoader(ClassPath... classPaths){
        ClassLoader[] classLoaders = new ClassLoader[classPaths.length];
        for(int i=0;i<classPaths.length;i++){
            classLoaders[i] = classPaths[i].getClassLoader(true);
        }
        return new MultiClassLoader(classLoaders);
    }
    
    public static File[] getRootFiles(ClassPath path){
        FileObject[] roots = path.getRoots();
        File[] files = new File[roots.length];
        for(int i=0;i<roots.length;i++){
            files[i] = FileUtil.toFile(roots[i]);
        }
        return files;
    }

}
