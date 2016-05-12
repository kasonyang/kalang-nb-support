
package kalang.ide.codegen;
import java.awt.Dialog;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import kalang.ide.Logger;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class OverrideGen implements CodeGenerator{

    OverrideGen(Lookup context) {
        
    }

    @Override
    public String getDisplayName() {
        return "Override method...";
    }

    @Override
    public void invoke() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.add(new DefaultMutableTreeNode("toString"));
        root.add(new DefaultMutableTreeNode("toString1"));
        root.add(new DefaultMutableTreeNode("toString2"));
        root.add(new DefaultMutableTreeNode("toString3"));
        root.add(new DefaultMutableTreeNode("toString4"));
        root.add(new DefaultMutableTreeNode("toString5"));
        JTree jTree = new JTree(root);
        DialogDescriptor desc = new DialogDescriptor(jTree, "Override method");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);
        dialog.setVisible(true);
        dialog.dispose();
        if(desc.getValue()==DialogDescriptor.OK_OPTION){
            //TODO do logic
            Logger.log("pressed ok");
        }
    }

}
