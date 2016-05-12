/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalang.ide.test;

import kalang.ide.utils.ScopeDataMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class ScopeDataMapTest {
    
    public ScopeDataMapTest() {
    }
    
    @Test
    public void test(){
        ScopeDataMap<Integer,String> sdm = new ScopeDataMap();
        sdm.put(0, 59, "unpassed");
        sdm.put(60, 79, "passed");
        sdm.put(80, 100, "good");
        
        assertEquals( "unpassed",sdm.get(30));
        assertEquals("passed", sdm.get(60));
        assertEquals("passed", sdm.get(70));
        assertEquals("good", sdm.get(80));
        assertEquals("good", sdm.get(100));
        assertNull(sdm.get(-1));
        assertNull(sdm.get(101));
    }
    
}
