package kalang.ide.test;

import kalang.ide.codegen.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class StringUtilTest {
    public StringUtilTest() {
        
    }
    
    @Test
    public void test(){
        assertEquals("abc", StringUtil.insert("ac", 1, "b"));
    }
    
}
