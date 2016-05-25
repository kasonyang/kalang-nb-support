package kalang.ide.compiler;

import java.io.*;
import kalang.antlr.KalangParser;
import kalang.ast.AstNode;
import kalang.ast.ErrorousExpr;
import kalang.ast.ExprStmt;
import kalang.compiler.AstBuilder;
import kalang.compiler.CompilationUnit;
import kalang.ide.Logger;
import kalang.tool.JointFileSystemCompiler;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class NBKalangCompiler {

    public static JointFileSystemCompiler createKalangCompiler(FileObject fo) {
        final ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
        ClassPath compilePath = ClassPath.getClassPath(fo, ClassPath.COMPILE);
        final JointFileSystemCompiler compiler = new JointFileSystemCompiler() {
            @Override
            public AstBuilder createAstBuilder(CompilationUnit compilationUnit, KalangParser parser) {
                return new AstBuilder(compilationUnit, parser) {
                    
                    @Override
                    public Object visitErrorousMemberExpr(KalangParser.ErrorousMemberExprContext emec) {
                        super.visitErrorousMemberExpr(emec);
                        return visit(emec.expression());
                    }

                    @Override
                    public Object visitErrorousStat(KalangParser.ErrorousStatContext esc) {
                        super.visitErrorousStat(esc);
                        Object ast = visit(esc.expression());
                        if (ast instanceof AstNode) {
                            return new ExprStmt(new ErrorousExpr((AstNode) ast));
                        } else {
                            return null;
                        }
                    }

                };
            }
        };
        if(sourcePath!=null){
            for(FileObject sp:sourcePath.getRoots()){
                File spf = FileUtil.toFile(sp);
                if(spf!=null){
                    compiler.addJavaSourcePath(spf);
                    compiler.addSourcePath(spf);
                }
            }
        }
        if(compilePath!=null){
            String cp = compilePath.toString(ClassPath.PathConversionMode.PRINT);
            Logger.log("classpath:" + cp);
            if(cp!=null && !cp.isEmpty()){
                for(String p:cp.split(";")){
                    File f = new File(p);
                    if(f.exists()) compiler.addClassPath(f);
                }
            }
        }
        return compiler;
    }

}
