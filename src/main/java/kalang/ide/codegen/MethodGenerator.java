
package kalang.ide.codegen;
import java.awt.Dialog;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import javax.swing.JTree;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import kalang.ast.ClassNode;
import kalang.ast.MethodNode;
import kalang.ide.Logger;
import kalang.ide.compiler.NBKalangCompiler;
import kalang.ide.utils.ClassPathHelper;
import kalang.ide.utils.DocumentUtil;
import kalang.ide.utils.FileObjectUtil;
import kalang.tool.JointFileSystemCompiler;
import kalang.util.AstUtil;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public abstract class MethodGenerator implements CodeGenerator{

    private final JTextComponent textComponent;
    private String menuTitle;

    MethodGenerator(JTextComponent jTextComponent,String menuTitle) {
        this.textComponent = jTextComponent;
        this.menuTitle = menuTitle;
    }

    @Override
    public String getDisplayName() {
        return menuTitle;
    }

    @Override
    public void invoke() {
        FileObject fo = FileObjectUtil.getFileObject(textComponent.getDocument());
        if(fo==null){
            Logger.log("FileObject is null");
            return;
        }
        //TODO what about inner class?
        String className = ClassPathHelper.getClassName(fo);
        JointFileSystemCompiler cp;
        try {
            cp = NBKalangCompiler.compile(fo);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return;
        }
        ClassNode ast = cp.getAst(className);
        if(ast==null){
            Logger.log("ast is null:" + className);
            return;
        }
        List<MethodNode> methods = getMethodNodes(ast);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        for(MethodNode m:methods){
            root.add(new DefaultMutableTreeNode(new MethodItem(m)));
        }
        JTree jTree = new JTree(root);
        DialogDescriptor desc = new DialogDescriptor(jTree, menuTitle);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);
        dialog.setVisible(true);
        dialog.dispose();
        if(desc.getValue()==DialogDescriptor.OK_OPTION){
            TreePath[] selectedPaths = jTree.getSelectionPaths();
            List<MethodNode> selectedMethods = new LinkedList();
            for(TreePath p:selectedPaths){
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
                Object m = node.getUserObject();
                if(m instanceof MethodItem){
                    selectedMethods.add(((MethodItem)m).getMethod());
                }
            }
            String indent = DocumentUtil.getIndent(textComponent);
            textComponent.replaceSelection(getCode(selectedMethods,indent));
        }
    }
    
    protected String getCode(List<MethodNode> mds,String indent){
        String code = "";
        for(MethodNode m:mds){
            //TODO change method code
            code += m.toString() + "{\n" + indent + indent + "\n" + indent + "}\n" + indent;
        }
        return code;
    }
    
    protected abstract List<MethodNode> getMethodNodes(ClassNode clazz);

}
