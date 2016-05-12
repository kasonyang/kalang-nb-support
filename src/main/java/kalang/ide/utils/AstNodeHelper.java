
package kalang.ide.utils;
import kalang.ast.AstNode;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import kalang.ast.AstVisitor;
import kalang.ast.ClassNode;
import kalang.compiler.AstBuilder;
import kalang.compiler.CompilationUnit;
import kalang.ide.Logger;
import kalang.ide.parser.KaParser;
import kalang.util.ParseTreeNavigator;
import kalang.util.TokenNavigator;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
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
