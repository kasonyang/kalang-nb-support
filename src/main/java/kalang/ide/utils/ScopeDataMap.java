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
public class ScopeDataMap<K extends Comparable,V> {
    
    TreeMap<K,ScopeData<K,V>> datas = new TreeMap<K, ScopeData<K, V>>();
    
    
    public void put(K lowOffset,K highOffset,V data){
        ScopeData<K, V> sd = new ScopeData<K, V>(lowOffset, highOffset, data);
        datas.put(lowOffset, sd);
    }
    
    public V get(K offset){
        Map.Entry<K, ScopeData<K, V>> e = datas.floorEntry(offset);
        if(e==null) return null;
        ScopeData<K, V> v = e.getValue();
        if(offset.compareTo(v.highOffset)>0) return null;
        return v.getData();
    }
    

}
