/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kalang.ide.completion;
import kalang.ast.MethodNode;
import kalang.ast.VarObject;
import java.util.*;
import kalang.core.ClassType;
import kalang.core.MethodDescriptor;
import kalang.core.Type;
import kalang.util.AstUtil;
import org.netbeans.modules.csl.api.CompletionProposal;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class TypeCompletion {
    public static List<CompletionProposal> complete(CompletionRequest request,Type ast,boolean inStatic){
        List<CompletionProposal> list = new LinkedList<CompletionProposal>();
        if(ast==null) return list;
        if(!(ast instanceof ClassType)) return list;
        ClassType clazz = (ClassType) ast;
        VarObject[] fs = ast.getFields();
        String prefix = request.prefix==null ? "":request.prefix;
        if(fs!=null){
            for(VarObject f:fs){
                if(f.name==null || !f.name.startsWith(prefix)) continue;
                if(inStatic != AstUtil.isStatic(f.modifier)) continue;
                KalangCompletionItem.FieldCompleteItem fi = new KalangCompletionItem.FieldCompleteItem(request, f);
                list.add(fi);
            }
        }
        //TODO require caller type
        MethodDescriptor[] ms = clazz.getMethodDescriptors(clazz.getClassNode(), true);
        if(ms!=null){
            for(MethodDescriptor m:ms){
                String name = m.getName();
                if(name==null || !name.startsWith(prefix)) continue;
                if(name.startsWith("<")) continue;
                if(inStatic != AstUtil.isStatic(m.getModifier())) continue;
                KalangCompletionItem ci = new KalangCompletionItem.MethodCompleteItem(m,request);
                  list.add(ci);
            }
        }
        return list;
    }
}
