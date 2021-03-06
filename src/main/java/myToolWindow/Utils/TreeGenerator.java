package myToolWindow.Utils;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.util.ProgressIndicatorUtils;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.Query;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.PhpDocRefImpl;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import com.jetbrains.php.lang.psi.elements.impl.MethodReferenceImpl;
import myToolWindow.MyToolWindow;
import myToolWindow.TreeRenderer;
import myToolWindow.Nodes.ClassNode;
import myToolWindow.Nodes.ClassNodeSet;
import myToolWindow.Nodes.UsageNode;
import myToolWindow.Nodes.UsageNodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.util.HashSet;

public class TreeGenerator extends Task.Backgroundable {
    private final ClassNodeSet classNodeSet = new ClassNodeSet();
    private final TreeRenderer renderer;
    private final MethodImpl element;
    private final MyToolWindow mtw;
    private ProgressIndicator indicator;

    public TreeGenerator(MyToolWindow tw, @Nullable Project project, MethodImpl e) {
        super(project, "Generating Tree Of Usages", false);
        mtw = tw;
        renderer = new TreeRenderer();
        element = e;
    }

    public void run(@NotNull ProgressIndicator progressIndicator) {
        indicator = progressIndicator;
        indicator.setFraction(0.0);

        while (!ProgressIndicatorUtils.runInReadActionWithWriteActionPriority(this::process)) {
            ProgressIndicatorUtils.yieldToPendingWriteActions();
        }

        indicator.setFraction(1.0);
    }

    private void process() {
        try {
            Tree tree = generateUsageTree(element);

            mtw.finishCreatingTree(tree);
        } catch (ProcessCanceledException e) {
            if (mtw.forcedCancel) {
                mtw.forcedCancel = false;
            } else {
                throw e;
            }
        }
    }

    private Tree generateUsageTree(MethodImpl element) throws ProcessCanceledException {
        classNodeSet.clear();
        ClassNode classNode = (ClassNode) UsageNodeFactory.createMethodNode(element, indicator);
        classNodeSet.add(classNode);
        DefaultMutableTreeNode topElement = new DefaultMutableTreeNode(classNode);

        DefaultMutableTreeNode usageTree = recursiveGenerator(element, topElement);

        return configureTree(usageTree);
    }

    private DefaultMutableTreeNode recursiveGenerator(MethodImpl element, DefaultMutableTreeNode root) throws ProcessCanceledException {
        Query<PsiReference> query = ReferencesSearch.search(element);
        // I'm using set in this place to get references to unique methods
        HashSet<PhpPsiElement> set = new HashSet<>();

        for (PsiReference psiReference : query) {
            indicator.checkCanceled();
            PsiElement el = psiReference.getElement();
            PsiFile file = el.getContainingFile();
            final int offset = el.getTextOffset();

            MethodImpl methodImpl = PsiTreeUtil.findElementOfClassAtOffset(file, offset, MethodImpl.class, false);

            if (methodImpl != null) {
                set.add(methodImpl);
            } else {
                MethodReferenceImpl methodReferenceImpl = PsiTreeUtil.findElementOfClassAtOffset(file, offset, MethodReferenceImpl.class, false);

                if (methodReferenceImpl != null) {
                    set.add(methodReferenceImpl);
                } else {
                    if (el instanceof PhpDocRefImpl) {

                    } else {
                        System.out.println("Not recognized element");
                    }
                }
            }
        }

        for (PhpPsiElement setElement : set) {
            indicator.checkCanceled();
            if (setElement instanceof MethodImpl) {
                MethodImpl methodImpl = (MethodImpl) setElement;
                if (!classNodeSet.contains(methodImpl)) {
                    ClassNode caller = (ClassNode) UsageNodeFactory.createMethodNode(methodImpl, indicator);
                    DefaultMutableTreeNode callerNode = new DefaultMutableTreeNode(caller);

                    root.add(callerNode);
                    classNodeSet.add(caller);

                    recursiveGenerator(methodImpl, callerNode);
                } else {
                    ClassNode classNode = classNodeSet.find(element);
                    if (classNode != null) {
                        classNode.setIsCyclic();
                    }
                }
            } else {
                MethodReferenceImpl methodReferenceImpl = (MethodReferenceImpl) setElement;

                UsageNode caller = UsageNodeFactory.createFileNode(methodReferenceImpl);
                DefaultMutableTreeNode callerNode = new DefaultMutableTreeNode(caller);

                root.add(callerNode);
            }
        }

        return root;
    }

    private Tree configureTree(DefaultMutableTreeNode top) {
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
}
