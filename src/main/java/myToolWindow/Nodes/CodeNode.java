package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.util.List;

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

    public Icon getIcon() { return AllIcons.Actions.Checked; }

    public List<CodeNode> callers;
}
