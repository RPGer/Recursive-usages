package myToolWindow;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.Query;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import myToolWindow.Nodes.MethodNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.Enumeration;

public class MyToolWindow {
    private JPanel myToolWindowContent;
    private MyRenderer renderer;

    public MyToolWindow(Project project) {
        renderer = new MyRenderer();
        myToolWindowContent = new JPanel(new BorderLayout());

        if (project != null) {
            EditorEventMulticaster multicaster = EditorFactory.getInstance().getEventMulticaster();
            multicaster.addCaretListener(new MyCaretListener(this), project);
        }
    }

    public void createAndRenderTree(MethodImpl element){
        MethodNode methodNode = new MethodNode(element);
        DefaultMutableTreeNode topElement = new DefaultMutableTreeNode(methodNode);

        DefaultMutableTreeNode tree = generateUsageTree(element, topElement);

        renderTree(tree);
    }

    public DefaultMutableTreeNode generateUsageTree(MethodImpl element, DefaultMutableTreeNode root){
        Query<PsiReference> q = ReferencesSearch.search(element);

        for (PsiReference r : q){
            PsiElement el = r.getElement();
            PsiFile file = el.getContainingFile();
            final int offset = el.getTextOffset();

            MethodImpl mel = PsiTreeUtil.findElementOfClassAtOffset(file, offset, MethodImpl.class, false);
            if (mel != null){
                MethodNode caller = new MethodNode(mel);
                DefaultMutableTreeNode callerNode = new DefaultMutableTreeNode(caller);

                if (!elementExist(root, mel)){
                    root.add(callerNode);
                }

                generateUsageTree(mel, callerNode);
            }
        }

        return root;
    }

    private Boolean elementExist(DefaultMutableTreeNode root, MethodImpl mel){
        Enumeration<TreeNode> e = root.children();

        while (e.hasMoreElements()) {
            DefaultMutableTreeNode currentElement = (DefaultMutableTreeNode)e.nextElement();

            MethodNode node = (MethodNode)currentElement.getUserObject();
            MethodImpl callerPsiElement = node.getMethodImpl();
            if (mel.equals(callerPsiElement)){
                return true;
            }
        }

        return false;
    }

    public void renderTree(DefaultMutableTreeNode top){
        Tree tree = new Tree(top);

        tree.setCellRenderer(renderer);

        JBScrollPane treeView = new JBScrollPane(tree);

        myToolWindowContent.removeAll();
        myToolWindowContent.add(treeView);
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
