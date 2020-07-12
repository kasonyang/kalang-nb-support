
package kalang.ide.utils;
import kalang.compiler.ast.AstNode;
import kalang.compiler.ast.AstVisitor;
import kalang.compiler.ast.ClassNode;
import kalang.compiler.compile.CompilationUnit;
import kalang.compiler.compile.semantic.AstBuilder;
import kalang.ide.parser.KaParser;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class AstNodeHelper {
    
    public static AstNode getAstNodeByCaretOffset(KaParser.KaParserResult result,final  int caret){
        CompilationUnit cu = result.getCompiler().getCompilationUnit(result.getClassName());
        if(cu==null) return null;
        AstBuilder astBuilder = cu.getAstBuilder();
        //if(astBuilder==null) return null;
        ClassNode clazz = astBuilder.getAst();
        class MyAstVisitor extends AstVisitor<Object>{
            public AstNode result;
            @Override
            public Object visit(AstNode node) {
                if(node==null) return null;
                int start = node.offset.startOffset;
                int stop = node.offset.stopOffset;
                if(caret>=start && caret<=stop){
                    result = node;
                }
                return super.visit(node);
            }
        }
        MyAstVisitor myAstVisitor = new MyAstVisitor();
        myAstVisitor.visit(clazz);
        return myAstVisitor.result;
    }

}
