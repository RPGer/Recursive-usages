package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;

import javax.swing.*;

public class MethodNode extends CodeNode {

    public MethodNode(String book, String filename) {
        super(book, filename);
    }

    public Icon getIcon() {
        return AllIcons.Actions.ModuleDirectory;
    }
}
