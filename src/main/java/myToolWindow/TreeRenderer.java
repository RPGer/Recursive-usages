package myToolWindow;

import com.intellij.ide.util.treeView.NodeRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.tree.TreeUtil;
import myToolWindow.Nodes.UsageNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TreeRenderer extends NodeRenderer {

    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        UsageNode node = (UsageNode) TreeUtil.getUserObject(value);
        if (node != null) {
            setIcon(node.getIcon());

            try {
                append(node.getMainText(), SimpleTextAttributes.REGULAR_ATTRIBUTES, true);
                append(node.getAdditionalText(), SimpleTextAttributes.GRAYED_ATTRIBUTES, false);
            } catch (NullPointerException e) {
                append("Error", SimpleTextAttributes.ERROR_ATTRIBUTES);
            }
        }
    }
}
