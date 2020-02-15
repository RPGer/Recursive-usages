package myToolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.wm.ToolWindow;
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
import myToolWindow.Actions.CollapseTreeAction;
import myToolWindow.Actions.ExpandTreeAction;
import myToolWindow.Actions.FindUsagesAction;
import myToolWindow.Nodes.CodeNode;
import myToolWindow.Nodes.CodeNodeFactory;
import myToolWindow.Nodes.CodeNodeSet;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.HashSet;

public class MyToolWindow {
    private final JPanel generalPanel;
    private final JPanel bottomPanel;
    private final MyRenderer renderer;
    private final ToolWindow toolWindow;
    private CodeNodeSet codeNodeSet = new CodeNodeSet();
    public Tree tree;

    public MyToolWindow(ToolWindow tw) {
        toolWindow = tw;
        renderer = new MyRenderer();
        generalPanel = new JPanel(new BorderLayout());

        JComponent toolbarPanel = createToolbarPanel();
        generalPanel.add(toolbarPanel, BorderLayout.NORTH);

        bottomPanel = new JPanel(new BorderLayout());
        generalPanel.add(bottomPanel, BorderLayout.CENTER);

        bottomPanel.removeAll();
        bottomPanel.add(new JBScrollPane());
    }

    @NotNull
    private JComponent createToolbarPanel() {
        DefaultActionGroup result = new DefaultActionGroup();

        result.add(new FindUsagesAction(this));
        result.add(new ExpandTreeAction(this));
        result.add(new CollapseTreeAction(this));

        return ActionManager.getInstance().createActionToolbar(ActionPlaces.STRUCTURE_VIEW_TOOLBAR, result, true).getComponent();
    }

    public void createAndRenderTree(MethodImpl element) {
        codeNodeSet.clear();
        CodeNode codeNode = CodeNodeFactory.createNode(element);
        codeNodeSet.add(codeNode);
        DefaultMutableTreeNode topElement = new DefaultMutableTreeNode(codeNode);

        DefaultMutableTreeNode usageTree = generateUsageTree(element, topElement);

        tree = this.configureTree(usageTree);

        JBScrollPane treeView = new JBScrollPane(tree);

        bottomPanel.removeAll();
        bottomPanel.add(treeView);

        toolWindow.getComponent().updateUI();
    }

    public DefaultMutableTreeNode generateUsageTree(MethodImpl element, DefaultMutableTreeNode root) {
        Query<PsiReference> q = ReferencesSearch.search(element);

        for (PsiReference r : q) {
            PsiElement el = r.getElement();
            PsiFile file = el.getContainingFile();
            final int offset = el.getTextOffset();

            MethodImpl mel = PsiTreeUtil.findElementOfClassAtOffset(file, offset, MethodImpl.class, false);

            if (mel != null && !codeNodeSet.contains(mel)) {
                CodeNode caller = CodeNodeFactory.createNode(mel);
                DefaultMutableTreeNode callerNode = new DefaultMutableTreeNode(caller);

                root.add(callerNode);
                codeNodeSet.add(caller);

                generateUsageTree(mel, callerNode);
            } else {
                CodeNode codeNode = codeNodeSet.find(element);
                if (codeNode != null) {
                    codeNode.setIsCyclic();
                }
            }
        }

        return root;
    }

    public Tree configureTree(DefaultMutableTreeNode top) {
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

        return tree;
    }

    public JPanel getContent() {
        return generalPanel;
    }
}
