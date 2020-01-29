package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;

import javax.swing.*;

public class RouteNode extends CodeNode {

    public RouteNode(String book, String filename) {
        super(book, filename);
    }

    public Icon getIcon() {
        return AllIcons.Actions.Amend;
    }
}
