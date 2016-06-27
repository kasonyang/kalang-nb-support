package kalang.ide.completion;

import java.util.Objects;
import java.util.Set;
import kalang.core.MethodDescriptor;
import kalang.core.ParameterDescriptor;
import kalang.ide.Logger;
import kalang.ide.utils.ModifierUtil;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.HtmlFormatter;
import org.netbeans.modules.csl.api.Modifier;

/**
 *
 * @author Kason Yang
 */
public class MethodCompleteItem extends KalangCompletionItem {

    private final MethodDescriptor method;

    public MethodCompleteItem(MethodDescriptor method, CompletionRequest request) {
        super(request);
        this.method = method;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public String getLhsHtml(HtmlFormatter hf) {
        hf.appendText(getName());
        hf.parameters(true);
        hf.appendText("(");
        ParameterDescriptor[] params = method.getParameterDescriptors();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    hf.appendText(",");
                }
                ParameterDescriptor p = params[i];
                hf.appendText(p.getType() + " " + p.getName());
            }
        }
        hf.appendText(")");
        hf.parameters(false);
        Logger.log("lhs text:" + hf.getText());
        return hf.getText();
    }

    @Override
    public String getInsertPrefix() {
        Logger.log("calling getInsertPrefix");
        return method.getName();
        //return method.getName();
    }

    @Override
    public String getRhsHtml(HtmlFormatter hf) {
        return Objects.toString(method.getReturnType(), "");
    }

    @Override
    public Set<Modifier> getModifiers() {
        return ModifierUtil.getModifier(method.getModifier());
    }

    @Override
    public String getCustomInsertTemplate() {
        return null;
        //TODO why it insert a indent automatically
//        Logger.log("calling getCustomInsertTemplate");
//        StringBuilder sb = new StringBuilder();
//        sb.append(getInsertPrefix());
//        sb.append("(");
//        ParameterDescriptor[] pds = method.getParameterDescriptors();
//        for (int i = 0; i < pds.length; i++) {
//            if (i > 0) {
//                sb.append(",");
//            }
//            sb.append(String.format("${%s}", pds[i].getName()));
//        }
//        sb.append(")${cursor}");
//        Logger.log("insertTemplate:" + sb.toString());
//        return sb.toString();
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.METHOD;
    }
    
    

}
