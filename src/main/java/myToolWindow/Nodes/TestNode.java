package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;

import javax.swing.*;

public class TestNode extends CodeNode {

    public TestNode(String book, String filename) {
        super(book, filename);
    }

    public Icon getIcon() {
        return AllIcons.Actions.Back;
    }
}
