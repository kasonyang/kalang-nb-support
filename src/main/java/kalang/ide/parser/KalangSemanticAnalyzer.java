
package kalang.ide.parser;
import java.util.*;
import kalang.ast.AstNode;
import kalang.ast.AstVisitor;
import kalang.ast.ClassNode;
import kalang.ast.FieldExpr;
import kalang.ast.FieldNode;
import kalang.ast.InvocationExpr;
import kalang.ast.MethodNode;
import kalang.ast.ParameterExpr;
import kalang.ast.ParameterNode;
import kalang.ast.VarObject;
import kalang.compiler.CompilationUnit;
import kalang.ide.Logger;
import org.netbeans.modules.csl.api.ColoringAttributes;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.SemanticAnalyzer;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class KalangSemanticAnalyzer extends SemanticAnalyzer<KaParser.KaParserResult>{

    private boolean canceled;
    private Map<OffsetRange, Set<ColoringAttributes>> highlights;

    @Override
    public Map<OffsetRange, Set<ColoringAttributes>> getHighlights() {
        highlights = new HashMap<OffsetRange, Set<ColoringAttributes>>();
        highlights.put(new OffsetRange(10, 12),ColoringAttributes.FIELD_SET);
        Logger.log("semantic highlight:" + highlights);
        return highlights;
    }

    @Override
    public void run(KaParser.KaParserResult result, SchedulerEvent arg1) {
        Logger.log("event:" + arg1);
        CompilationUnit cunit = result.getCompiler().getCompilationUnit(result.getClassName());
        highlights = new HashMap<OffsetRange, Set<ColoringAttributes>>();
        if(cunit==null) return;
        ClassNode ast = cunit.getAst();
        new AstVisitor<Object>(){
            @Override
            public Object visitParameterExpr(ParameterExpr node) {
                highlights.put(getVarOffset(node), ColoringAttributes.PARAMETER_SET);
                return super.visitParameterExpr(node); 
            }

            @Override
            public Object visitInvocationExpr(InvocationExpr node) {
                highlights.put(getVarOffset(node), ColoringAttributes.METHOD_SET);
                return super.visitInvocationExpr(node);
            }

            @Override
            public Object visitFieldExpr(FieldExpr node) {
                highlights.put(getVarOffset(node), ColoringAttributes.FIELD_SET);
                return super.visitFieldExpr(node);
            }

            @Override
            public Object visitMethodNode(MethodNode node) {
                return super.visitMethodNode(node); //To change body of generated methods, choose Tools | Templates.
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
                highlights.put(getVarOffset(node), ca);
                return null;
            }
            
            

            private OffsetRange getVarOffset(AstNode node) {
                if(node.offset.startOffset<0 || node.offset.stopOffset<0){
                    return OffsetRange.NONE;
                }
                return new OffsetRange(node.offset.startOffset, node.offset.stopOffset);
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
