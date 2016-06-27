/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalang.ide.completion;

import kalang.ast.MethodNode;
import kalang.ast.VarObject;
import java.util.*;
import javax.swing.ImageIcon;
import kalang.core.FieldDescriptor;
import kalang.core.MethodDescriptor;
import kalang.core.ParameterDescriptor;
import kalang.core.Type;
import kalang.ide.Logger;
import kalang.ide.utils.ModifierUtil;
import org.netbeans.modules.csl.api.CompletionProposal;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.HtmlFormatter;
import org.netbeans.modules.csl.api.Modifier;

/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public abstract class KalangCompletionItem implements CompletionProposal {
    
    private final CompletionRequest request;

    public KalangCompletionItem(CompletionRequest request) {
        this.request = request;
    }

    @Override
    public int getAnchorOffset() {
        return request.anchorOffset;
    }

    @Override
    public ElementHandle getElement() {
        return null;
    }

    @Override
    public abstract String getName();

    @Override
    public String getInsertPrefix() {
        return getName();
    }

    @Override
    public String getSortText() {
        return getName();
    }

    @Override
    public String getLhsHtml(HtmlFormatter hf) {
        return getName();
    }

    @Override
    public String getRhsHtml(HtmlFormatter hf) {
        return getName();
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.METHOD;
    }

    @Override
    public ImageIcon getIcon() {
        return null;
    }

    @Override
    public Set<Modifier> getModifiers() {
        return Collections.emptySet();
    }

    @Override
    public boolean isSmart() {
        return true;
    }

    @Override
    public int getSortPrioOverride() {
        return 0;
    }

    @Override
    public String getCustomInsertTemplate() {
        return null;
    }
    
    

}
