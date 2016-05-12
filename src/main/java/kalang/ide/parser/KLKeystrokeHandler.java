
package kalang.ide.parser;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.modules.csl.api.KeystrokeHandler;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.spi.ParserResult;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class KLKeystrokeHandler implements KeystrokeHandler {

    @Override
    public boolean beforeCharInserted(Document dcmnt, int i, JTextComponent jtc, char c) throws BadLocationException {
        return false;
    }

    @Override
    public boolean afterCharInserted(Document dcmnt, int i, JTextComponent jtc, char c) throws BadLocationException {
        return false;
    }

    @Override
    public boolean charBackspaced(Document dcmnt, int i, JTextComponent jtc, char c) throws BadLocationException {
        return false;
    }

    @Override
    public int beforeBreak(Document dcmnt, int i, JTextComponent jtc) throws BadLocationException {
        return -1;
    }

    @Override
    public OffsetRange findMatching(Document dcmnt, int i) {
        return OffsetRange.NONE;
    }

    @Override
    public List<OffsetRange> findLogicalRanges(ParserResult pr, int i) {
        int len = pr.getSnapshot().getText().length();
        OffsetRange rg = new OffsetRange(1, len-2);
        return Collections.singletonList(rg);
    }

    @Override
    public int getNextWordOffset(Document dcmnt, int i, boolean bln) {
        return -1;
    }

}
