package myToolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.pom.Navigatable;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.Query;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import com.jetbrains.php.lang.psi.elements.impl.MethodReferenceImpl;
import myToolWindow.Actions.CollapseTreeAction;
import myToolWindow.Actions.ExpandTreeAction;
import myToolWindow.Actions.FindUsagesAction;
import myToolWindow.Nodes.ClassNode;
import myToolWindow.Nodes.ClassNodeSet;
import myToolWindow.Nodes.UsageNodeFactory;
import myToolWindow.Nodes.UsageNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class MyToolWindow {
    private final JPanel generalPanel;
    private final JPanel bottomPanel;
    private final MyRenderer renderer;
    private final ToolWindow toolWindow;
    private final ClassNodeSet classNodeSet = new ClassNodeSet();
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
        classNodeSet.clear();
        ClassNode classNode = (ClassNode) UsageNodeFactory.createMethodNode(element);
        classNodeSet.add(classNode);
        DefaultMutableTreeNode topElement = new DefaultMutableTreeNode(classNode);

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

            if (mel != null) {
                if (!classNodeSet.contains(mel)) {
                    ClassNode caller = (ClassNode) UsageNodeFactory.createMethodNode(mel);
                    DefaultMutableTreeNode callerNode = new DefaultMutableTreeNode(caller);

                    root.add(callerNode);
                    classNodeSet.add(caller);

                    generateUsageTree(mel, callerNode);
                } else {
                    ClassNode classNode = classNodeSet.find(element);
                    if (classNode != null) {
                        classNode.setIsCyclic();
                    }
                }
            } else {
                MethodReferenceImpl mer = PsiTreeUtil.findElementOfClassAtOffset(file, offset, MethodReferenceImpl.class, false);

                UsageNode caller = UsageNodeFactory.createFileNode(mer);
                DefaultMutableTreeNode callerNode = new DefaultMutableTreeNode(caller);

                root.add(callerNode);
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

            UsageNode mn = (UsageNode) selected.getUserObject();
            NavigatablePsiElement methodImpl = mn.getElement();

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
