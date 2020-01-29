package myToolWindow;

import myToolWindow.Nodes.CodeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class MyRenderer extends DefaultTreeCellRenderer {
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;

        CodeNode node = (CodeNode) treeNode.getUserObject();
        setIcon(node.getIcon());

        return this;
    }
}
