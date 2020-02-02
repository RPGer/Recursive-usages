package myToolWindow;

import com.intellij.icons.AllIcons;
import com.intellij.ide.CommonActionsManager;
import com.intellij.ide.dnd.aware.DnDAwareTree;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.PsiNavigateUtil;
import com.intellij.util.Query;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import myToolWindow.Actions.FindUsagesAction;
import myToolWindow.Nodes.MethodNode;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

public class MyToolWindow {
    private JPanel generalPanel;
//    private JPanel topPanel;
    private JPanel bottomPanel;
    private MyRenderer renderer;
//    private Project project;
    private ToolWindow toolWindow;

    public MyToolWindow(Project p, ToolWindow tw) {
//        project = p;
        toolWindow = tw;
        renderer = new MyRenderer();
        generalPanel = new JPanel(new BorderLayout());


        DefaultActionGroup result = new DefaultActionGroup();
        result.add(new FindUsagesAction(this, AllIcons.Actions.Resume));
        JComponent c = ActionManager.getInstance().createActionToolbar(ActionPlaces.STRUCTURE_VIEW_TOOLBAR, result, true).getComponent();

//        generalPanel.
        generalPanel.add(c, BorderLayout.NORTH);


        bottomPanel = new JPanel(new BorderLayout());

//        generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));
//        generalPanel.add(topPanel, BorderLayout.NORTH);
        generalPanel.add(bottomPanel, BorderLayout.CENTER);


    }

    public void createAndRenderTree(MethodImpl element) {
        MethodNode methodNode = new MethodNode(element);
        DefaultMutableTreeNode topElement = new DefaultMutableTreeNode(methodNode);

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
                MethodNode caller = new MethodNode(mel);
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

            MethodNode node = (MethodNode) currentElement.getUserObject();
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
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                TreePath tp = treeSelectionEvent.getPath();
                DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tp.getLastPathComponent();

                MethodNode mn = (MethodNode) selected.getUserObject();
                MethodImpl methodImpl = mn.getMethodImpl();

                PsiNavigateUtil.navigate(methodImpl);
            }
        });

        JBScrollPane treeView = new JBScrollPane(tree);

        bottomPanel.removeAll();
        bottomPanel.add(treeView);
    }

    public JPanel getContent() {
        return generalPanel;
    }
}
