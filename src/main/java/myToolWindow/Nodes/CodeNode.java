package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;
import com.intellij.ui.RowIcon;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import javax.swing.*;

public class CodeNode implements HasIcon {
    private final HasIcon wrappee;
    private final MethodImpl element;
    private boolean isCyclic = false;

    public CodeNode(HasIcon cn, MethodImpl e) {
        this.wrappee = cn;
        this.element = e;
    }

    @Override
    public Icon getIcon() {
        if (isCyclic){
            return new RowIcon(wrappee.getIcon(), AllIcons.Gutter.RecursiveMethod);
        } else {
            return wrappee.getIcon();
        }
    }

    public void setIsCyclic(){
        isCyclic = true;
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
