package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;

import javax.swing.*;

public class MethodNode implements HasIcon {
    public MethodNode() {
    }

    @Override
    public Icon getIcon() {
        return AllIcons.Nodes.Method;
    }
}
