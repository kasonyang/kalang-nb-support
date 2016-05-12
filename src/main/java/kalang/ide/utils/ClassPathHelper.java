/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalang.ide.utils;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import kalang.ide.Logger;
import kalang.ide.compiler.MultiClassLoader;
import kalang.util.ClassNameUtil;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;

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

}
