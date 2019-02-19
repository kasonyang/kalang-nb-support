package kalang.ide.completion;

import kalang.compiler.compile.CompilationUnit;
import kalang.compiler.compile.KalangCompiler;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class CompletionRequest {
    
    public int anchorOffset;
    public String prefix;
    public KalangCompiler compiler;
    public CompilationUnit compilationUnit;

}
