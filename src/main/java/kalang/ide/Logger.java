/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kalang.ide;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import org.openide.windows.IOProvider;
import org.openide.windows.OutputWriter;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class Logger {

    private static OutputWriter out;
    
    private static OutputWriter getOut(){
        if(out==null){
            out = IOProvider.getDefault().getIO("kalang", true).getOut();
        }
        return out;
    }
    
    public static void log(String msg){
        getOut().println(new Date() +":"+msg);
    }
    
    public static void warn(Exception ex){
        ex.printStackTrace(out);
    }

    public static void warn(String msg) {
        log(msg);
    }

    public static void error(String string) {
        log(string);
    }

}
