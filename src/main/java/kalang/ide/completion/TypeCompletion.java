package kalang.ide.completion;

import java.util.*;

import kalang.compiler.core.*;
import kalang.compiler.util.AstUtil;
import org.netbeans.modules.csl.api.CompletionProposal;

import javax.annotation.Nullable;

/**
 * @author Kason Yang
 */
public class TypeCompletion {

    public static List<CompletionProposal> complete(CompletionRequest request, ObjectType objectType,@Nullable Boolean staticMember) {
        List<CompletionProposal> list = new LinkedList<CompletionProposal>();
        FieldDescriptor[] fs = objectType.getFieldDescriptors(request.compilationUnit.getAst());
        String prefix = request.prefix == null ? "" : request.prefix;
        if (fs != null) {
            for (FieldDescriptor f : fs) {
                if (f.getName() == null || !f.getName().startsWith(prefix)) {
                    continue;
                }
                if (staticMember != null && staticMember != AstUtil.isStatic(f.getModifier())) {
                    continue;
                }
                list.add(new FieldCompleteItem(request, f));
            }
        }
        MethodDescriptor[] ms = objectType.getMethodDescriptors(request.compilationUnit.getAst(), true, true);
        if (ms != null) {
            for (MethodDescriptor m : ms) {
                String name = m.getName();
                if (name == null || !name.startsWith(prefix)) {
                    continue;
                }
                if (name.startsWith("<")) {
                    continue;
                }
                if (staticMember != null && staticMember != AstUtil.isStatic(m.getModifier())) {
                    continue;
                }
                list.add(new MethodCompleteItem(m, request));
            }
        }
        return list;
    }

}
