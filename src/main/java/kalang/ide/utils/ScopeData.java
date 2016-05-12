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
public class ScopeData<K,V> {
    
    K lowOffset;
    
    K highOffset;
    
    V data;

    public ScopeData(K lowOffset, K highOffset, V data) {
        this.lowOffset = lowOffset;
        this.highOffset = highOffset;
        this.data = data;
    }

    public K getLowOffset() {
        return lowOffset;
    }

    public K getHighOffset() {
        return highOffset;
    }

    public V getData() {
        return data;
    }

}
