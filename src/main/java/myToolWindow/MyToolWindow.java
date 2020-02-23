package myToolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.ui.components.JBScrollPane;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import myToolWindow.Actions.CollapseTreeAction;
import myToolWindow.Actions.ExpandTreeAction;
import myToolWindow.Actions.FindUsagesAction;
import myToolWindow.Utils.TreeGenerator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MyToolWindow {
    private final JPanel generalPanel;
    private final JPanel bottomPanel;
    private final TreeGenerator treeGenerator;
    private final ToolWindow toolWindow;
    public Tree tree;

    public MyToolWindow(ToolWindow tw) {
        toolWindow = tw;
        treeGenerator = new TreeGenerator();
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
        tree = treeGenerator.generateUsageTree(element);

        JBScrollPane treeView = new JBScrollPane(tree);

        bottomPanel.removeAll();
        bottomPanel.add(treeView);

        toolWindow.getComponent().updateUI();
    }

    public JPanel getContent() {
        return generalPanel;
    }
}
