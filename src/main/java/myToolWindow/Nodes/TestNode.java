package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;

import javax.swing.*;

public class TestNode implements HasIcon {
    public TestNode() {
    }

    @Override
    public Icon getIcon() {
        return AllIcons.Actions.StartDebugger;
    }
}
