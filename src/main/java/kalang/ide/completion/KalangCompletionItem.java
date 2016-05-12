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
    
    public static class MethodCompleteItem extends KalangCompletionItem{

        private final MethodNode method;

        public MethodCompleteItem(MethodNode method,CompletionRequest request) {
            super(request);
            this.method =  method;
        }

        @Override
        public String getName() {
            return method.name;
        }

        @Override
        public String getLhsHtml(HtmlFormatter hf) {
            hf.appendText(getName());
            hf.parameters(true);
            hf.appendText("(");
            if(method.parameters!=null){
                for(int i=0;i<method.parameters.size();i++){
                    if(i>0) hf.appendText(",");
                    VarObject p = method.parameters.get(i);
                    hf.appendText(p.type + " " + p.name);
                }
            }
            hf.appendText(")");
            hf.parameters(false);
            Logger.log("lhs text:" + hf.getText());
            return hf.getText();
        }

        @Override
        public String getInsertPrefix() {
            StringBuilder sb = new StringBuilder();
            sb.append(method.name);
            sb.append("(");
            return sb.toString();
        }

        @Override
        public String getRhsHtml(HtmlFormatter hf) {
            return method.type==null?"":method.type.getName();
        }

        @Override
        public Set<Modifier> getModifiers() {
            return ModifierUtil.getModifier(method.modifier);
        }
        
    }
    
    static class FieldCompleteItem extends KalangCompletionItem{

        private final VarObject field;

        public FieldCompleteItem(CompletionRequest request,VarObject field) {
            super(request);
            this.field = field;
        }

        @Override
        public String getName() {
            return this.field.name;
        }

        @Override
        public String getRhsHtml(HtmlFormatter hf) {
            return field.type!=null?field.type.getName():"";
        }

        @Override
        public Set<Modifier> getModifiers() {
            return ModifierUtil.getModifier(field.modifier);
        }

        @Override
        public ElementKind getKind() {
            return ElementKind.FIELD;
        }
        
    }

}
