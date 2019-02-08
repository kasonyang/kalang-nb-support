package kalang.ide.compiler;

import java.io.*;
import kalang.compiler.antlr.KalangParser;
import kalang.compiler.ast.AstNode;
import kalang.compiler.ast.ErrorousExpr;
import kalang.compiler.ast.ExprStmt;
import kalang.compiler.compile.AstBuilder;
import kalang.compiler.compile.CodeGenerator;
import kalang.compiler.compile.CompilationUnit;
import kalang.compiler.compile.JavaAstLoader;
import kalang.compiler.compile.KalangCompiler;
import kalang.compiler.compile.SourceLoader;
import kalang.compiler.compile.codegen.Ast2JavaStub;
import kalang.compiler.tool.FileSystemSourceLoader;
import kalang.ide.utils.ClassPathHelper;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Kason Yang
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

          @Override
          public CodeGenerator createCodeGenerator(CompilationUnit compilationUnit) {
              return new Ast2JavaStub();
          }
        };
        if (sourcePath != null) {
            SourceLoader sourceLoader = new FileSystemSourceLoader(ClassPathHelper.getRootFiles(sourcePath), new String[]{"kl", "kalang"});
            compiler.setSourceLoader(sourceLoader);
        }
        return compiler;
    }

}
