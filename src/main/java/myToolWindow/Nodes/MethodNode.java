package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import javax.swing.*;

public class MethodNode implements CodeNode {
    MethodImpl element = null;

    public MethodNode(MethodImpl e) {
        this.element = e;
    }

    public Icon getIcon() {
        return AllIcons.Nodes.Method;
    }

    public String toString() {
        return this.element.getName() + " <- " + this.element.getOriginalElement().getContainingFile().getName();
    }

    public MethodImpl getMethodImpl() {
        return this.element;
    }
}
