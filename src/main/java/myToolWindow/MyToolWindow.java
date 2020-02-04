package myToolWindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.Query;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import myToolWindow.Actions.FindUsagesAction;
import myToolWindow.Nodes.CodeNode;
import myToolWindow.Nodes.CodeNodeFactory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.Enumeration;

public class MyToolWindow {
    private final JPanel generalPanel;
    private final JPanel bottomPanel;
    private final MyRenderer renderer;

    public MyToolWindow() {
        renderer = new MyRenderer();
        generalPanel = new JPanel(new BorderLayout());

        DefaultActionGroup result = new DefaultActionGroup();
        result.add(new FindUsagesAction(this, AllIcons.RunConfigurations.TestState.Run));
        JComponent toolbarPanel = ActionManager.getInstance().createActionToolbar(ActionPlaces.STRUCTURE_VIEW_TOOLBAR, result, true).getComponent();

        bottomPanel = new JPanel(new BorderLayout());

        generalPanel.add(toolbarPanel, BorderLayout.NORTH);
        generalPanel.add(bottomPanel, BorderLayout.CENTER);

        renderTree(null);
    }

    public void createAndRenderTree(MethodImpl element) {
        CodeNode codeNode = CodeNodeFactory.createNode(element);
        DefaultMutableTreeNode topElement = new DefaultMutableTreeNode(codeNode);

        DefaultMutableTreeNode tree = generateUsageTree(element, topElement);

        renderTree(tree);
    }

    public DefaultMutableTreeNode generateUsageTree(MethodImpl element, DefaultMutableTreeNode root) {
        Query<PsiReference> q = ReferencesSearch.search(element);

        for (PsiReference r : q) {
            PsiElement el = r.getElement();
            PsiFile file = el.getContainingFile();
            final int offset = el.getTextOffset();

            MethodImpl mel = PsiTreeUtil.findElementOfClassAtOffset(file, offset, MethodImpl.class, false);
            if (mel != null) {
                CodeNode caller = CodeNodeFactory.createNode(mel);
                DefaultMutableTreeNode callerNode = new DefaultMutableTreeNode(caller);

                if (!elementExist(root, mel)) {
                    root.add(callerNode);
                }

                generateUsageTree(mel, callerNode);
            }
        }

        return root;
    }

    private Boolean elementExist(DefaultMutableTreeNode root, MethodImpl mel) {
        Enumeration<TreeNode> e = root.children();

        while (e.hasMoreElements()) {
            DefaultMutableTreeNode currentElement = (DefaultMutableTreeNode) e.nextElement();

            CodeNode node = (CodeNode) currentElement.getUserObject();
            MethodImpl callerPsiElement = node.getMethodImpl();
            if (mel.equals(callerPsiElement)) {
                return true;
            }
        }

        return false;
    }

    public void renderTree(DefaultMutableTreeNode top) {
        Tree tree = new Tree(top);

        tree.setCellRenderer(renderer);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.addTreeSelectionListener(treeSelectionEvent -> {
            TreePath tp = treeSelectionEvent.getPath();
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tp.getLastPathComponent();

            CodeNode mn = (CodeNode) selected.getUserObject();
            MethodImpl methodImpl = mn.getMethodImpl();

            final PsiElement navigationElement = methodImpl.getNavigationElement();
            Navigatable navigatable = (Navigatable) navigationElement;
            navigatable.navigate(false);
        });

        JBScrollPane treeView = new JBScrollPane(tree);

        bottomPanel.removeAll();
        bottomPanel.add(treeView);
    }

    public JPanel getContent() {
        return generalPanel;
    }
}
