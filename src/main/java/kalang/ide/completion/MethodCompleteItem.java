package kalang.ide.completion;

import kalang.compiler.core.MethodDescriptor;
import kalang.compiler.core.ParameterDescriptor;
import kalang.compiler.util.NameUtil;
import kalang.ide.Logger;
import kalang.ide.utils.ModifierUtil;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.HtmlFormatter;
import org.netbeans.modules.csl.api.Modifier;

import java.util.Set;

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
                String pType = NameUtil.getSimpleClassName(p.getType().getName());
                hf.appendText(pType);
                hf.appendText(" ");
                hf.appendText(p.getName());
            }
        }
        hf.appendText(")");
        hf.parameters(false);
        return hf.getText();
    }

    @Override
    public String getInsertPrefix() {
        return method.getName();
    }

    @Override
    public String getRhsHtml(HtmlFormatter hf) {
        return NameUtil.getSimpleClassName(method.getReturnType().getName());
    }

    @Override
    public Set<Modifier> getModifiers() {
        return ModifierUtil.getModifier(method.getModifier());
    }

    @Override
    public String getCustomInsertTemplate() {
        StringBuilder sb = new StringBuilder();
        sb.append(getInsertPrefix());
        sb.append("(${cursor})");
        return sb.toString();
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.METHOD;
    }
    
    

}
