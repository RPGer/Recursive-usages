package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import javax.swing.*;

public class RouteNode implements CodeNode {
    MethodImpl element = null;

    public RouteNode(MethodImpl e) {
        this.element = e;
    }

    public Icon getIcon() {
        return AllIcons.Nodes.Enum;
    }

    public String toString() {
        return this.element.getName() + " <- " + this.element.getOriginalElement().getContainingFile().getName();
    }

    public MethodImpl getMethodImpl() {
        return this.element;
    }
}
