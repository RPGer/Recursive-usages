package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import javax.swing.*;
import java.util.List;

public abstract class CodeNode {
    private MethodImpl element;

    public CodeNode(MethodImpl e) {
        element = e;
    }

    public String toString() {
        return element.getName();
    }

    public Icon getIcon() { return AllIcons.Actions.Checked; }

    public MethodImpl getMethodImpl() { return element; }
}
