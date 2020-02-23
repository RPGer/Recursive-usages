package myToolWindow.Nodes.Icons.ClassNodes;

import com.intellij.icons.AllIcons;
import myToolWindow.Nodes.Icons.HasIcon;

import javax.swing.*;

public class MethodNode implements HasIcon {
    public MethodNode() {
    }

    @Override
    public Icon getIcon() {
        return AllIcons.Nodes.Method;
    }
}
