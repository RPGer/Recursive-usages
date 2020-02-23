package myToolWindow.Nodes;

import com.intellij.psi.NavigatablePsiElement;
import myToolWindow.Nodes.Icons.HasIcon;

public interface UsageNode extends HasIcon {
    NavigatablePsiElement getElement() throws NullPointerException;

    String getMainText() throws NullPointerException;

    String getAdditionalText() throws NullPointerException;
}
