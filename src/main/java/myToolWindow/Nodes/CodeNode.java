package myToolWindow.Nodes;

import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import javax.swing.*;

public interface CodeNode {
    String toString();

    Icon getIcon();

    MethodImpl getMethodImpl();
}
