package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import javax.swing.*;

public class RouteNode extends CodeNode {

    public RouteNode(MethodImpl e) {
        super(e);
    }

    public Icon getIcon() {
        return AllIcons.Actions.CheckOut;
    }
}
