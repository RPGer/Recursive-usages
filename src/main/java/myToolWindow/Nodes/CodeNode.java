package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;

import javax.swing.*;

public abstract class CodeNode {
    public String bookName;
    public String bookURL;

    public CodeNode(String book, String filename) {
        bookName = book;
        bookURL = filename;
    }

    public String toString() {
        return bookName;
    }

    public Icon getIcon() { return AllIcons.Actions.Amend; }
}
