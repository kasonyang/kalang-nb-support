package kalang.ide.compiler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import kalang.antlr.KalangParser;
import kalang.ast.AstNode;
import kalang.ast.ErrorousExpr;
import kalang.ast.ExprNode;
import kalang.ast.ExprStmt;
import kalang.compiler.AstBuilder;
import kalang.compiler.AstLoader;
import kalang.compiler.CompilationUnit;
import kalang.compiler.DefaultCompileConfiguration;
import kalang.compiler.JavaAstLoader;
import kalang.compiler.KalangCompiler;
import kalang.compiler.KalangSource;
import kalang.compiler.SourceLoader;
import kalang.ide.Logger;
import kalang.ide.utils.ClassPathHelper;
import kalang.tool.FileSystemCompiler;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class NBKalangCompiler {

    private static String JAVA_OUT_DIR = "build/java";

    public static FileSystemCompiler createKalangCompiler(FileObject fo) {
        final ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
        ClassPath compilePath = ClassPath.getClassPath(fo, ClassPath.COMPILE);
        Logger.log("CompilePath:" + compilePath);
        List<ClassPath> cps = new ArrayList(2);
        if(sourcePath!=null) cps.add(sourcePath);
        if(compilePath!=null) cps.add(compilePath);
        ClassLoader classLoader = ClassPathHelper.createClassLoader(cps.toArray(new ClassPath[0]));
        final JavaAstLoader astLoader = new JavaAstLoader(classLoader);
        final FileSystemCompiler compiler = new FileSystemCompiler(
                new DefaultCompileConfiguration() {
            @Override
            public SourceLoader getSourceLoader() {
                return new SourceLoader() {
                    @Override
                    public KalangSource loadSource(String clsName) {
                        if(sourcePath==null) return null;
                        String fileName = clsName.replace(".", "/") + ".kl";
                        FileObject fo = sourcePath.findResource(fileName);
                        if (fo == null) {
                            return null;
                        }
                        Logger.log("source of " + clsName + " found");
                        try {
                            //TODO fix file name
                            return new KalangSource(clsName,fo.asText(),fo.getName());
                        } catch (IOException ex) {
                            Logger.warn("failed to load source:" + clsName);
                            return null;
                        }
                    }
                };
            }

            @Override
            public AstLoader getAstLoader() {
                return astLoader;
            }

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
                        if(ast instanceof AstNode){
                            return new ExprStmt(new ErrorousExpr((AstNode)ast));
                        }else return null;
                    }

                };
            }
        }
        );
        return compiler;
        
    }

}
