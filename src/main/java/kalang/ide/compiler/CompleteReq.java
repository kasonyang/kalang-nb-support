package kalang.ide.compiler;

/**
 * @author KasonYang
 */
public class CompleteReq {

    public String className;

    public int caret;

    public CompleteReq(String className, int caret) {
        this.className = className;
        this.caret = caret;
    }

}
