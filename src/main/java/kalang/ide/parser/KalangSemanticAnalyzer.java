
package kalang.ide.parser;
import java.util.*;
import kalang.compiler.antlr.KalangLexer;
import kalang.compiler.ast.AstNode;
import kalang.compiler.ast.AstVisitor;
import kalang.compiler.ast.ClassNode;
import kalang.compiler.ast.FieldExpr;
import kalang.compiler.ast.FieldNode;
import kalang.compiler.ast.MethodNode;
import kalang.compiler.ast.ParameterExpr;
import kalang.compiler.ast.ParameterNode;
import kalang.compiler.ast.VarExpr;
import kalang.compiler.ast.VarObject;
import kalang.compiler.compile.CompilationUnit;
import kalang.compiler.util.TokenNavigator;
import kalang.ide.Logger;
import org.antlr.v4.runtime.Token;
import org.netbeans.modules.csl.api.ColoringAttributes;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.SemanticAnalyzer;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
/**
 *
 * @author Kason Yang
 */
public class KalangSemanticAnalyzer extends SemanticAnalyzer<KaParser.KaParserResult>{

    private boolean canceled;
    private final Map<OffsetRange, Set<ColoringAttributes>> highlights = new HashMap();

    @Override
    public Map<OffsetRange, Set<ColoringAttributes>> getHighlights() {
        Logger.log("semantic highlight:" + highlights);
        return highlights;
    }

    @Override
    public void run(KaParser.KaParserResult result, SchedulerEvent arg1) {
        Logger.log("event:" + arg1);
        CompilationUnit cunit = result.getCompiler().getCompilationUnit(result.getClassName());
        highlights.clear();
        if(cunit==null) return;
        ClassNode ast = cunit.getAst();
        final TokenNavigator tokenNav = new TokenNavigator(cunit.getTokens().getTokens());
        new AstVisitor<Object>(){
            
            //TODO highlight parameter node
            @Override
            public Object visitParameterExpr(ParameterExpr node) {
                highlights.put(getNodeOffset(node), ColoringAttributes.PARAMETER_SET);
                return super.visitParameterExpr(node); 
            }

            @Override
            public Object visitParameterNode(ParameterNode parameterNode) {
                highlights.put(getIdOffset(parameterNode,-1), ColoringAttributes.PARAMETER_SET);
                return super.visitParameterNode(parameterNode);
            }
            
            //TODO highlight field node
            @Override
            public Object visitFieldExpr(FieldExpr node) {
                highlights.put(getIdOffset(node,-1), ColoringAttributes.FIELD_SET);
                return super.visitFieldExpr(node);
            }
                                  
            @Override
            public Object visitMethodNode(MethodNode node) {
                return super.visitMethodNode(node);
            }

            @Override
            public Object visitVarExpr(VarExpr node) {
                highlights.put(getNodeOffset(node),Collections.singleton(ColoringAttributes.LOCAL_VARIABLE));
                return super.visitVarExpr(node);
            }
            
            //TODO change to local var
            public Object visitVarObject(VarObject node) {
                Set<ColoringAttributes> ca;
                if(node instanceof FieldNode){
                    ca = ColoringAttributes.FIELD_SET;
                }else if(node instanceof ParameterNode){
                    ca = ColoringAttributes.PARAMETER_SET;
                }else{
                    ca =Collections.singleton(ColoringAttributes.LOCAL_VARIABLE);
                }
                highlights.put(getNodeOffset(node), ca);
                return null;
            }

            private OffsetRange getNodeOffset(AstNode node) {
                if(node.offset.startOffset<0 || node.offset.stopOffset<0){
                    return OffsetRange.NONE;
                }
                return new OffsetRange(node.offset.startOffset, node.offset.stopOffset+1);
            }
            
            private OffsetRange getIdOffset(AstNode node,int idOffset){
                int startOffset = node.offset.startOffset;
                int stopOffset = node.offset.stopOffset;
                try{
                    tokenNav.move(startOffset);
                }catch(Exception ex){
                    return OffsetRange.NONE;
                }
                int offset = startOffset;
                List<Token> ids = new ArrayList(idOffset+1);
                while(
                        (idOffset>=0 && ids.size()<=idOffset && offset<=stopOffset)
                        || offset<=stopOffset
                        ){
                    Token token = tokenNav.getCurrentToken();
                    if(token.getType()==KalangLexer.Identifier){
                        ids.add(token);
                    }
                    offset = token.getStopIndex();
                    if(!tokenNav.hasNext()) break;
                    tokenNav.next();
                }
                if(idOffset<0) idOffset = ids.size() + idOffset;
                if(idOffset>=0 && ids.size()>idOffset){
                    Token tk = ids.get(idOffset);
                    return new OffsetRange(tk.getStartIndex(), tk.getStopIndex()+1);
                }
                return OffsetRange.NONE;
            }
        }.visit(ast);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {
        canceled = true;
    }

}
