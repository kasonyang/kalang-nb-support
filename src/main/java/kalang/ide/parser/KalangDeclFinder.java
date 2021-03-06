
package kalang.ide.parser;

import javax.swing.text.Document;
import kalang.compiler.antlr.KalangLexer;
import kalang.compiler.ast.AstNode;
import kalang.compiler.ast.ClassNode;
import kalang.compiler.ast.ClassReference;
import kalang.compiler.ast.FieldExpr;
import kalang.compiler.ast.FieldNode;
import kalang.compiler.ast.InvocationExpr;
import kalang.compiler.ast.MethodNode;
import kalang.compiler.ast.NewObjectExpr;
import kalang.compiler.ast.VarExpr;
import kalang.compiler.ast.VarObject;

import kalang.ide.Logger;
import kalang.ide.lexer.KaTokenId;
import kalang.ide.utils.AstNodeHelper;
import kalang.ide.utils.FileObjectUtil;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.csl.api.DeclarationFinder;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.spi.ParserResult;
import org.openide.filesystems.FileObject;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class KalangDeclFinder implements DeclarationFinder{

    @Override
    public DeclarationLocation findDeclaration(ParserResult pr, int caretOffset) {
        Logger.log("finding location");
        KaParser.KaParserResult result = (KaParser.KaParserResult) pr;
        String type = result.getClassName();
        AstNode astNode = AstNodeHelper.getAstNodeByCaretOffset(result, caretOffset);
        if(astNode==null) return DeclarationLocation.NONE;
        Logger.log("ast found:" + astNode.getClass().getName());
        if(astNode instanceof VarExpr){
            VarObject var = ((VarExpr)astNode).getVar();
            return getDeclaration(result, type,var);
        }else if(astNode instanceof InvocationExpr){
            MethodNode method = ((InvocationExpr)astNode).getMethod().getMethodNode();
            return getDeclaration(result, method);
        }else if(astNode instanceof FieldExpr){
            FieldExpr fieldExpr = (FieldExpr) astNode;
            FieldNode field = fieldExpr.getField().getFieldNode();
            return getDeclaration(result,field.getClassNode().name,field);
        }else if(astNode instanceof ClassReference){
            ClassNode clazz = ((ClassReference) astNode).getReferencedClassNode();
            return getDeclaration(result, clazz.name,clazz);
        }else if(astNode instanceof NewObjectExpr){
            MethodNode md = ((NewObjectExpr)astNode).getConstructor().getMethod().getMethodNode();
            return getDeclaration(result, md);
        }
        return DeclarationLocation.NONE;
    }

    @Override
    public OffsetRange getReferenceSpan(Document dcmnt, int caretOffset) {
        //Logger.log("finding span");
        TokenHierarchy<Document> hierarchy = TokenHierarchy.get(dcmnt);
        TokenSequence<?> tokenSeq = hierarchy.tokenSequence();
        tokenSeq.move(caretOffset);
        if(!tokenSeq.movePrevious() || !tokenSeq.moveNext()){
            return OffsetRange.NONE;
        }
        Token<KaTokenId> token = (Token<KaTokenId>) tokenSeq.token();
        KaTokenId tokenId = token.id();
        if(tokenId.ordinal()==KalangLexer.Identifier){
            return new OffsetRange(tokenSeq.offset(),tokenSeq.offset()+token.length());
        }
        return OffsetRange.NONE;
    }
    
    static DeclarationLocation getDeclaration(KaParser.KaParserResult result,MethodNode method){
        ClassNode classNode = method.getClassNode();
        String type = classNode.name;
        FileObject srcFo = result.getSnapshot().getSource().getFileObject();
        ClassPath path = ClassPath.getClassPath(srcFo, ClassPath.SOURCE);
        FileObject fo = FileObjectUtil.getFileObject(path, type);
        if(fo==null) return DeclarationLocation.NONE;
        return new DeclarationLocation( fo,method.offset.startOffset<0 ? 0 : method.offset.startOffset);
    }

    static DeclarationLocation getDeclaration(KaParser.KaParserResult result, String type,AstNode ast){
        Logger.log("getDeclaratin for type:" + type); 
        if(type==null) return DeclarationLocation.NONE;
        FileObject srcFo = result.getSnapshot().getSource().getFileObject();
        ClassPath path = ClassPath.getClassPath(srcFo, ClassPath.SOURCE);
        FileObject file = FileObjectUtil.getFileObject(path,type);
        if(file==null) return DeclarationLocation.NONE;
        Logger.log("file object of type found:" + file);
        //TODO get class start offset
        return new DeclarationLocation(file,ast==null ? 0 : ast.offset.startOffset);
    }
    
}
