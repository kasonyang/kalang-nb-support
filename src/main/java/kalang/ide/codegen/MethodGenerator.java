
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
import kalang.core.ArrayType;
import kalang.core.ClassType;
import kalang.core.GenericType;
import kalang.core.MethodDescriptor;
import kalang.core.ParameterDescriptor;
import kalang.core.ParameterizedType;
import kalang.core.PrimitiveType;
import kalang.core.Type;
import kalang.core.WildcardType;
import kalang.ide.Logger;
import kalang.ide.compiler.NBKalangCompiler;
import kalang.ide.utils.ClassPathHelper;
import kalang.ide.utils.DocumentUtil;
import kalang.ide.utils.FileObjectUtil;
import kalang.tool.JointFileSystemCompiler;
import kalang.util.AstUtil;
import kalang.util.NameUtil;
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
        List<MethodDescriptor> methods = getMethodNodes(ast);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        for(MethodDescriptor m:methods){
            root.add(new DefaultMutableTreeNode(new MethodItem(m)));
        }
        JTree jTree = new JTree(root);
        DialogDescriptor desc = new DialogDescriptor(jTree, menuTitle);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);
        dialog.setVisible(true);
        dialog.dispose();
        if(desc.getValue()==DialogDescriptor.OK_OPTION){
            TreePath[] selectedPaths = jTree.getSelectionPaths();
            List<MethodDescriptor> selectedMethods = new LinkedList();
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
    
    protected void insertMethods(List<MethodDescriptor> mds,String indent,ParserRuleContext unit){
        String code = "";
        Set<String> referenceClasses = new HashSet();
        for(MethodDescriptor m:mds){
            List<String> params = new LinkedList();
            for(ParameterDescriptor p:m.getParameterDescriptors()){
                String pt =simplifyTypeName(p.getType(),referenceClasses);
                params.add(String.format("%s %s",pt,p.getName()));
            }
            String returnType = simplifyTypeName(m.getReturnType(), referenceClasses);
            String mdDecl = String.format("%s %s %s(%s)", Modifier.toString(m.getModifier() & ~Modifier.ABSTRACT),returnType,m.getName(),String.join(",", params));
            code += "override " + indent + mdDecl + "{\n" + indent + indent + "throw new UnsupportedOperationException();" + "\n" + indent + "}\n" + indent;
        }
        textComponent.replaceSelection(code);
        ImportVisitor importVisitor = new ImportVisitor();
        importVisitor.visit(unit);
        List<String> importedSimpleName = importVisitor.getImported();
        List<String> importCodeList = new LinkedList();
        for(String ip:referenceClasses){
            //TODO remove classes in default packages
            //TODO what about same simple class name with different package?
            if(!importedSimpleName.contains(ip)){
                importCodeList.add("import " + ip + ";");
            }
        }
        int importInsertPos = importVisitor.getImportEnd() + 1;
        if(importInsertPos<0) importInsertPos = 0;
        textComponent.setCaretPosition(importInsertPos);
        textComponent.replaceSelection("\n" + String.join("\n", importCodeList) + "\n");
    }
    
    protected abstract List<MethodDescriptor> getMethodNodes(ClassNode clazz);
    
    protected String[] simplifyTypeName(Type[] type,Set<String> importList){
        String[] list = new String[type.length];
        for(int i=0;i<type.length;i++){
            list[i] = simplifyTypeName(type[i], importList);
        }
        return list;
    }
    
    protected String simplifyTypeName(Type type,Set<String> importList){
        if(type instanceof PrimitiveType){
            return type.getName();
        }else if(type instanceof ArrayType){
            String st = simplifyTypeName(((ArrayType)type).getComponentType(), importList);
            return st + "[]";
        }else if(type instanceof GenericType){
            return type.getName();
        }else if(type instanceof ParameterizedType){
            ParameterizedType pt = (ParameterizedType) type;
            return String.format("%s<%s>", simplifyTypeName(pt.getRawType(), importList) ,String.join(",",Arrays.asList(simplifyTypeName(pt.getParameterTypes(), importList))));
                    
        }else if(type instanceof ClassType){
            String name = type.getName();
            //TODO remove default class
            importList.add(name);
            return NameUtil.getClassNameWithoutPackage(name);
        }else if(type instanceof WildcardType){
            WildcardType wt = (WildcardType) type;
            Type[] lbs = wt.getLowerBounds();
            Type[] ubs = wt.getUpperBounds();
            if(lbs!=null && lbs.length>0){
                return "? super " + simplifyTypeName(lbs[0], importList);
            }else if(ubs!=null && ubs.length>0){
                return "? extends " + simplifyTypeName(ubs[0], importList);
            }else{
                return "?";
            }
        }else{
            Logger.warn(new IllegalArgumentException("unknown type:" + type));
            return type.getName();
        }
    }

}
