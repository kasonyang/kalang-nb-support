package kalang.ide.compiler;

import java.io.*;
import kalang.antlr.KalangParser;
import kalang.ast.AstNode;
import kalang.ast.ErrorousExpr;
import kalang.ast.ExprStmt;
import kalang.compiler.AstBuilder;
import kalang.compiler.CompilationUnit;
import kalang.compiler.JavaAstLoader;
import kalang.compiler.KalangCompiler;
import kalang.compiler.SourceLoader;
import kalang.ide.utils.ClassPathHelper;
import kalang.tool.FileSystemSourceLoader;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class NBKalangCompiler {

    public static KalangCompiler compile(FileObject fo) throws IOException {
        KalangCompiler cp = createKalangCompiler(fo);
        String className = ClassPathHelper.getClassName(fo);
        cp.addSource(className, fo.asText(), fo.getName());
        cp.compile();
        return cp;
    }

    public static KalangCompiler createKalangCompiler(FileObject fo) {
        final ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
        ClassPath compilePath = ClassPath.getClassPath(fo, ClassPath.COMPILE);
        ClassLoader classLoader = NBKalangCompiler.class.getClassLoader();
        if (compilePath != null) {
            classLoader = ClassPathHelper.createClassLoader(compilePath);
        }
        final KalangCompiler compiler = new KalangCompiler(new JavaAstLoader(null, classLoader)) {
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
        if (sourcePath != null) {
            SourceLoader sourceLoader = new FileSystemSourceLoader(ClassPathHelper.getRootFiles(sourcePath), new String[]{"kl", "kalang"});
            compiler.setSourceLoader(sourceLoader);
        }
        return compiler;
    }

}
