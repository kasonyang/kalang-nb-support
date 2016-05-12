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

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class FileSystemUtil {

    public static List<File> listFiles(File path, final String suffix) {

        List<File> list = new LinkedList();
        File[] fs = path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (dir.isFile() && suffix != null && suffix.length() > 0) {
                    return name.endsWith(suffix);
                } else {
                    return true;
                }

            }
        });
        for (File f : fs) {
            if (f.isDirectory()) {
                List<File> subFs = listFiles(f, suffix);
                list.addAll(subFs);
            } else if (f.isFile()) {
                list.add(f);
            }
        }
        return list;
    }
}
