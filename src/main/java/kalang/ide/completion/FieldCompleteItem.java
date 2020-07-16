package kalang.ide.completion;

import java.util.Set;
import kalang.compiler.core.FieldDescriptor;
import kalang.compiler.util.NameUtil;
import kalang.ide.utils.ModifierUtil;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.HtmlFormatter;
import org.netbeans.modules.csl.api.Modifier;

/**
 *
 * @author Kason Yang
 */
class FieldCompleteItem extends KalangCompletionItem {

    private final FieldDescriptor field;

    public FieldCompleteItem(CompletionRequest request, FieldDescriptor field) {
        super(request);
        this.field = field;
    }

    @Override
    public String getName() {
        return this.field.getName();
    }

    @Override
    public String getRhsHtml(HtmlFormatter hf) {
        return NameUtil.getSimpleClassName(field.getType().getName());
    }

    @Override
    public Set<Modifier> getModifiers() {
        return ModifierUtil.getModifier(field.getModifier());
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.FIELD;
    }

}
