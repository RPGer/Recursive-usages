package myToolWindow.Actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import myToolWindow.MyToolWindow;
import org.jetbrains.annotations.NotNull;

public class ExpandTreeAction extends AnAction {
    public MyToolWindow mtw;
    private boolean enabled = true;

    @SuppressWarnings("unused")
    public ExpandTreeAction() {
    }

    public ExpandTreeAction(MyToolWindow tw) {
        super("Expand All", "Expand all", AllIcons.Actions.Expandall);
        mtw = tw;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(enabled);
        super.update(e);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (mtw.tree != null) {
            for (int i = 0; i < mtw.tree.getRowCount(); i++) {
                mtw.tree.expandRow(i);
            }
        }
    }
}
