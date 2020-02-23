package myToolWindow.Nodes.Icons.ClassNodes;

import com.intellij.icons.AllIcons;
import myToolWindow.Nodes.Icons.HasIcon;

import javax.swing.*;

public class TestNode implements HasIcon {
    public TestNode() {
    }

    @Override
    public Icon getIcon() {
        return AllIcons.Actions.StartDebugger;
    }
}
