package kalang.ide.completion;
import java.util.*;
import kalang.compiler.core.ClassType;
import kalang.compiler.core.FieldDescriptor;
import kalang.compiler.core.MethodDescriptor;
import kalang.compiler.core.Type;
import kalang.compiler.util.AstUtil;
import org.netbeans.modules.csl.api.CompletionProposal;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class TypeCompletion {
    public static List<CompletionProposal> complete(CompletionRequest request,Type ast,boolean inStatic){
        List<CompletionProposal> list = new LinkedList();
        if(ast==null) return list;
        if(!(ast instanceof ClassType)) return list;
        ClassType clazz = (ClassType) ast;
        //TODO require caller from params
        FieldDescriptor[] fs = clazz.getFieldDescriptors(clazz.getClassNode());
        String prefix = request.prefix==null ? "":request.prefix;
        if(fs!=null){
            for(FieldDescriptor f:fs){
                if(f.getName()==null || !f.getName().startsWith(prefix)) continue;
                if(inStatic != AstUtil.isStatic(f.getModifier())) continue;
                FieldCompleteItem fi = new FieldCompleteItem(request, f);
                list.add(fi);
            }
        }
        //TODO require caller type
        MethodDescriptor[] ms = clazz.getMethodDescriptors(clazz.getClassNode(), true,true);
        if(ms!=null){
            for(MethodDescriptor m:ms){
                String name = m.getName();
                if(name==null || !name.startsWith(prefix)) continue;
                if(name.startsWith("<")) continue;
                if(inStatic != AstUtil.isStatic(m.getModifier())) continue;
                KalangCompletionItem ci = new MethodCompleteItem(m,request);
                  list.add(ci);
            }
        }
        return list;
    }
}
