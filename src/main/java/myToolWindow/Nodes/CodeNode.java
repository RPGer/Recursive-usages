package myToolWindow.Nodes;

import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import javax.swing.*;

public class CodeNode implements HasIcon {
    private HasIcon wrappee;
    MethodImpl element = null;

    public CodeNode(HasIcon cn, MethodImpl e){
        this.wrappee = cn;
        this.element = e;
    }

    @Override
    public Icon getIcon() {
        return wrappee.getIcon();
    }

    public MethodImpl getMethodImpl() {
        return this.element;
    }

    public String getMethodName() {
        return getMethodImpl().getName();
    }

    public String getFileName() {
        return getMethodImpl().getOriginalElement().getContainingFile().getName();
    }
}
