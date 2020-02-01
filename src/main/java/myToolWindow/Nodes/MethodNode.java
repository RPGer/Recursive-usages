package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import javax.swing.*;

public class MethodNode extends CodeNode {

    public MethodNode(MethodImpl e) {
        super(e);
    }

    public Icon getIcon() {
        return AllIcons.Actions.ModuleDirectory;
    }
}
