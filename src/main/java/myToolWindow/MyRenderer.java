package myToolWindow;

import com.intellij.ide.util.treeView.NodeRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.tree.TreeUtil;
import myToolWindow.Nodes.CodeNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MyRenderer extends NodeRenderer {

    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        CodeNode node = (CodeNode) TreeUtil.getUserObject(value);
        if (node != null) {
            setIcon(super.fixIconIfNeeded(node.getIcon(), selected, hasFocus));

            append(node.getMethodName(), SimpleTextAttributes.REGULAR_ATTRIBUTES, true);
            append(" <- " + node.getFileName(), SimpleTextAttributes.GRAYED_ATTRIBUTES, false);
        }
    }
}
