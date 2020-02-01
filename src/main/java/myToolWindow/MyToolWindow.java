package myToolWindow;

import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.OpenSourceUtil;
import com.intellij.util.PsiNavigateUtil;
import com.intellij.util.Query;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import myToolWindow.Nodes.MethodNode;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

public class MyToolWindow {
    private JPanel myToolWindowContent;
    private MyRenderer renderer;
    private Project project;

    public MyToolWindow(Project p) {
        project = p;
        renderer = new MyRenderer();
        myToolWindowContent = new JPanel(new BorderLayout());

        if (p != null) {
            EditorEventMulticaster multicaster = EditorFactory.getInstance().getEventMulticaster();
            multicaster.addCaretListener(new MyCaretListener(this), p);
        }
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
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                TreePath tp = treeSelectionEvent.getPath();
                DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tp.getLastPathComponent();

                MethodNode mn = (MethodNode) selected.getUserObject();
                MethodImpl methodImpl = mn.getMethodImpl();

                gotoPsiElement(methodImpl);
            }
        });

        JBScrollPane treeView = new JBScrollPane(tree);

        myToolWindowContent.removeAll();
        myToolWindowContent.add(treeView);
    }

    public void gotoPsiElement(PsiElement psiElement) {
        VirtualFile virtualFile = psiElement.getContainingFile().getVirtualFile();

        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, virtualFile);
        FileEditorManager.getInstance(project).openTextEditor(descriptor, true);

        final int offset = psiElement.getStartOffsetInParent();
        final int offset2 = psiElement.getTextOffset();

        PsiNavigateUtil.navigate(psiElement);

//        psiElement.getContext().getNavigationElement();
//        System.err.println(offset);
//        System.err.println(offset2);
//        gotoLine(offset);
    }

    public boolean gotoLine(int lineNumber) {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        if (editor == null)
            return false;

        CaretModel caretModel = editor.getCaretModel();
        int totalLineCount = editor.getDocument().getLineCount();

        if (lineNumber > totalLineCount)
            return false;

        //Moving caret to line number
        caretModel.moveToLogicalPosition(new LogicalPosition(lineNumber - 1, 0));

        //Scroll to the caret
        ScrollingModel scrollingModel = editor.getScrollingModel();
        scrollingModel.scrollToCaret(ScrollType.CENTER);

        return true;
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
