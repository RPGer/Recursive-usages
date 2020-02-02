package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import javax.swing.*;

public class TestNode implements CodeNode {
    MethodImpl element = null;

    public TestNode(MethodImpl e) {
        this.element = e;
    }

    public Icon getIcon() {
        return AllIcons.Actions.Back;
    }

    public String toString() {
        return this.element.getName() + " <- " + this.element.getOriginalElement().getContainingFile().getName();
    }

    public MethodImpl getMethodImpl() {
        return this.element;
    }
}
