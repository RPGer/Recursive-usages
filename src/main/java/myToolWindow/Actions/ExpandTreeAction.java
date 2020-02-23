package myToolWindow.Actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import myToolWindow.MyToolWindow;
import org.jetbrains.annotations.NotNull;

public class ExpandTreeAction extends AnAction {
    public MyToolWindow mtw;

    @SuppressWarnings("unused")
    public ExpandTreeAction() {
    }

    public ExpandTreeAction(MyToolWindow tw) {
        super("Expand All", "Expand all", AllIcons.Actions.Expandall);
        mtw = tw;
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
