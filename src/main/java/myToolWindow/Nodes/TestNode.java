package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import javax.swing.*;

public class TestNode extends CodeNode {

    public TestNode(MethodImpl e) {
        super(e);
    }

    public Icon getIcon() {
        return AllIcons.Actions.Back;
    }
}
