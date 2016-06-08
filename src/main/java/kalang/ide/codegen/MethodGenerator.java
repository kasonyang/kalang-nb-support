
package kalang.ide.codegen;
import java.awt.Dialog;
import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.*;
import java.net.*;
import java.util.*;
import javax.swing.JTree;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import kalang.antlr.KalangBaseVisitor;
import kalang.antlr.KalangParser;
import kalang.antlr.KalangVisitor;
import kalang.ast.ClassNode;
import kalang.ast.MethodNode;
import kalang.ast.ParameterNode;
import kalang.compiler.CompilationUnit;
import kalang.core.ClassType;
import kalang.core.Type;
import kalang.ide.Logger;
import kalang.ide.compiler.NBKalangCompiler;
import kalang.ide.utils.ClassPathHelper;
import kalang.ide.utils.DocumentUtil;
import kalang.ide.utils.FileObjectUtil;
import kalang.tool.JointFileSystemCompiler;
import kalang.util.AstUtil;
import org.antlr.v4.runtime.ParserRuleContext;
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
        JointFileSystemCompiler cp = NBKalangCompiler.createKalangCompiler(fo);
        cp.addSource(className, textComponent.getText(), fo.getName());
        cp.compile();
        ClassNode ast = cp.getAst(className);
        CompilationUnit cunit = cp.getCompilationUnit(className);
        if(cunit==null){
            Logger.log("compilation is null");
            return;
        }
        ParserRuleContext unitTree = cunit.getAstBuilder().getParseTree();
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
            insertMethods(selectedMethods,indent,unitTree);
        }
    }
    
    protected void insertMethods(List<MethodNode> mds,String indent,ParserRuleContext unit){
        String code = "";
        Set<Type> referenceClassName = new HashSet();
        for(MethodNode m:mds){
            List<String> params = new LinkedList();
            for(ParameterNode p:m.parameters){
                Type pt = p.type;
                referenceClassName.add(pt);
                params.add(String.format("%s %s",AstUtil.getClassNameWithoutPackage(pt.getName()),p.name));
            }
            Type type = m.type;
            referenceClassName.add(type);
            String mdDecl = String.format("%s %s %s(%s)", Modifier.toString(m.modifier & ~Modifier.ABSTRACT),AstUtil.getClassNameWithoutPackage(type.getName()),m.name,String.join(",", params));
            code += "@Override\n" + indent + mdDecl + "{\n" + indent + indent + "throw new UnsupportedOperationException();" + "\n" + indent + "}\n" + indent;
        }
        textComponent.replaceSelection(code);
        ImportVisitor importVisitor = new ImportVisitor();
        importVisitor.visit(unit);
        List<String> importedSimpleName = importVisitor.getImported();
        List<String> importCodeList = new LinkedList();
        for(Type ip:referenceClassName){
            if(!(ip instanceof ClassType)) continue;
            String fullClassName = ip.getName();
            while(fullClassName.endsWith("[]")){
                fullClassName = fullClassName.substring(0,fullClassName.length()-2);
            }
            String ipSimpleName = AstUtil.getClassNameWithoutPackage(fullClassName);
            //TODO remove classes in default packages
            //TODO what about same simple class name with different package?
            if(!importedSimpleName.contains(ipSimpleName)){
                importCodeList.add("import " + fullClassName + ";");
            }
        }
        int importInsertPos = importVisitor.getImportEnd() + 1;
        if(importInsertPos<0) importInsertPos = 0;
        textComponent.setCaretPosition(importInsertPos);
        textComponent.replaceSelection("\n" + String.join("\n", importCodeList) + "\n");
    }
    
    protected abstract List<MethodNode> getMethodNodes(ClassNode clazz);

}
